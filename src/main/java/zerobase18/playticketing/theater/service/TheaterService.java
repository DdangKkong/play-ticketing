package zerobase18.playticketing.theater.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase18.playticketing.company.entity.Company;
import zerobase18.playticketing.company.repository.CompanyRepository;
import zerobase18.playticketing.global.exception.CustomException;
import zerobase18.playticketing.global.type.ErrorCode;
import zerobase18.playticketing.theater.dto.CreateTheater;
import zerobase18.playticketing.theater.dto.TheaterDto;
import zerobase18.playticketing.theater.dto.UpdateTheater;
import zerobase18.playticketing.theater.entity.Seat;
import zerobase18.playticketing.theater.entity.Theater;
import zerobase18.playticketing.theater.repository.SeatRepository;
import zerobase18.playticketing.theater.repository.TheaterRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final CompanyRepository companyRepository;
    private final SeatRepository seatRepository;


    private Company findCompany(int companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_INVALID));
    }

    private Theater findTheater(int theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_INVALID));
    }

    // 좌석 생성 메소드
    private List<Seat> createSeat(int theaterId, String[] seatTypeArr, char[] seatRowArr, int[] seatPriceArr, int[][] seatNumArr) {
        Theater theater = findTheater(theaterId);
        int[] seatTypeIdArr = new int[seatTypeArr.length];

        List<Seat> seatList = new ArrayList<>();

        for (int i = 0; i < seatTypeIdArr.length; i++) {

            // 시작번호부터 끝번호까지 좌석 생성
            int startNum = seatNumArr[0][i];
            int endNum = seatNumArr[1][i];
            for (int j = startNum; j <= endNum; j++) {

                // 이미 해당 극장에 생성된 좌석이라면 생성불가
                if (seatRepository.existsSeatBySeatNumAndSeatRow(j, seatRowArr[i])) {
                    List<Seat> foundSeats = seatRepository.findAllBySeatNumAndSeatRow(j, seatRowArr[i]);
                    int size = foundSeats.size();
                    for (int k = 0; k < size; k++) {
                        Seat foundSeat = foundSeats.get(k);
                        int foundTheaterId = foundSeat.getTheater().getId();
                        if (theaterId == foundTheaterId) {
                            throw new CustomException(ErrorCode.SEAT_CONFLICT);
                        }
                    }
                }

                Seat seat = seatRepository.save(Seat.builder()
                        .seatRow(seatRowArr[i])
                        .seatNum(j)
                        .seatType(seatTypeArr[i])
                        .seatPrice(seatPriceArr[i])
                        .theater(theater)
                        .build());

                // 생성된 좌석을 List 화 한다.
                seatList.add(seat);
            }

        }

        return seatList;
    }

    // 극장 및 좌석 생성
    @Transactional
    public TheaterDto createTheater(CreateTheater.Request request) {

        Company company = findCompany(request.getCompanyId());

        // theater 미리 생성 (theaterId 값을 받아오기 위해)
        Theater theater = Theater.builder()
                        .theaterName(request.getTheaterName())
                        .theaterAdress(request.getTheaterAdress())
                        .seatTotalCount(request.getSeatTotalCount())
                        .seatRowCount(request.getSeatRowCount())
                        .company(company)
                        .build();
        theaterRepository.save(theater);

        // 좌석 타입 생성 -> 좌석 생성 -> 좌석 리스트 작성
        List<Seat> seatList = createSeat(
                theater.getId(), request.getSeatTypeArr(), request.getSeatRowArr(), request.getSeatPriceArr(), request.getSeatNumArr());

        // 생성 시간 넣기
        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 날짜와 시간 형식을 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        // 타입을 LocalDateTime 으로
        LocalDateTime createdAt = LocalDateTime.parse(formattedNow, formatter);

        // 다시 저장
        theater.createTheater(createdAt);
        theaterRepository.save(theater);

        TheaterDto theaterDto = TheaterDto.fromEntity(theater);
        theaterDto.setSeatList(seatList);

        return theaterDto;
    }

    // 극장 및 좌석 조회 - 누구나 가능
    public TheaterDto readTheater(int theaterId) {

        // 극장 정보와 좌석 정보 불러오기
        Theater theater = findTheater(theaterId);
        List<Seat> seatList = seatRepository.findAllByTheater(theater);

        TheaterDto theaterDto = TheaterDto.fromEntity(theater);
        theaterDto.setSeatList(seatList);

        return theaterDto;
    }

    // 극장 및 좌석 수정
    @Transactional
    public TheaterDto updateTheater(UpdateTheater.Request request) {

        // 극장 업체 정보와 극장 정보 불러오기
        Company company = findCompany(request.getCompanyId());
        Theater theater = findTheater(request.getTheaterId());

        // 좌석 정보를 제외한 모든 내용 업데이트
        // 수정 시간 넣기
        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 날짜와 시간 형식을 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        // 타입을 LocalDateTime 으로
        LocalDateTime updatedAt = LocalDateTime.parse(formattedNow, formatter);
        theater.changeTheater(request, updatedAt);

        // 좌석 모두 삭제 후 다시 생성
        seatRepository.deleteAllByTheater(theater);
        List<Seat> seatList = createSeat(
                theater.getId(), request.getSeatTypeArr(), request.getSeatRowArr(), request.getSeatPriceArr(), request.getSeatNumArr());
        TheaterDto theaterDto = TheaterDto.fromEntity(theater);
        theaterDto.setSeatList(seatList);

        return theaterDto;
    }

    // 극장 및 좌석 삭제 - deletedAt 만 넣어주고 나머지 데이터는 보관한다, 프론트에서 deletedAt 에 데이터가 있는것을 보고 안보이게 처리
    @Transactional
    public TheaterDto deleteTheater(int theaterId, int companyId) {

        // 극장 업체 정보와 극장 정보, 좌석 정보 불러오기
        Company company = findCompany(companyId);
        Theater theater = findTheater(theaterId);
        List<Seat> seatList = seatRepository.findAllByTheater(theater);

        // 삭제 시간 넣기
        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 날짜와 시간 형식을 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        // 타입을 LocalDateTime 으로
        LocalDateTime deletedAt = LocalDateTime.parse(formattedNow, formatter);
        theater.deleteTheater(deletedAt);

        TheaterDto theaterDto = TheaterDto.fromEntity(theater);
        theaterDto.setSeatList(seatList);

        return theaterDto;
    }

}
