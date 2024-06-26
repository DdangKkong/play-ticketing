package zerobase18.playticketing.theater.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase18.playticketing.company.entity.Company;
import zerobase18.playticketing.theater.dto.UpdateTheater;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private int id;

    // 극장명
    @Column(name = "theater_name")
    private String theaterName;

    // 극장 주소
    @Column(name = "theater_adress")
    private String theaterAdress;

    // 총 좌석 수
    @Column(name = "seat_total_count")
    private int seatTotalCount;

    // 좌석의 열 수 (가로)
    @Column(name = "seat_row_count")
    private int seatRowCount;

    // 생성 일시
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 수정 일시
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 삭제 일시
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 극장 업체 고유번호
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public void createTheater(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void changeTheater(UpdateTheater.Request request, LocalDateTime updatedAt){
        this.theaterName = request.getTheaterName();
        this.theaterAdress = request.getTheaterAdress();
        this.seatTotalCount = request.getSeatTotalCount();
        this.seatRowCount = request.getSeatRowCount();
        this.updatedAt = updatedAt;
    }

    public void deleteTheater(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }

}
