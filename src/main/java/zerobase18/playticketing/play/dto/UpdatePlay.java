package zerobase18.playticketing.play.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import zerobase18.playticketing.global.type.PlayGenre;
import zerobase18.playticketing.global.type.Ratings;
import zerobase18.playticketing.global.type.ReservationYN;
import zerobase18.playticketing.play.entity.Schedule;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class UpdatePlay {

    @Getter
    public static class Request {

        @NotNull
        private int playId;
        @NotNull
        private int troupeId;
        @NotNull
        private int theaterId;
        @NotBlank
        private String playName;
        @NotBlank
        private String playDetails;
        @NotBlank
        private String posterUrl;
        @NotNull
        private Ratings ratings;
        @NotNull
        private PlayGenre playGenre;
        @NotNull
        private Date playStartDate;
        @NotNull
        private Date playEndDate;
        @NotBlank
        private String runtime;
        @NotBlank
        private String actors;
        @NotNull
        private ReservationYN reservationYN;

        /**
         * 스케줄은 상영날짜, 상영시간이 각각 들어있는 이차원 배열로 받아온다
         * 상영날짜의 형식은 "yyyymmdd"(integer), 상영시간의 형식은 "hhmm"(integer) 이다
         *
         * ex)  {20240415, 1700, 1930, 2200},
         *      {20240417, 1900, 2100},
         *      {20240419, 1700, 1930, 2200},
         *      {20240420, 1430, 1700, 1930, 2200}
         */
        @NotNull
        private int[][] schedule;

    }

    @Builder
    @Getter
    public static class Response {

        private int playId;
        private int theaterId;
        private int troupeId;
        private String playName;
        private String playDetails;
        private String posterUrl;
        private Ratings ratings;
        private PlayGenre playGenre;
        private Date playStartDate;
        private Date playEndDate;
        private String runtime;
        private String actors;
        private ReservationYN reservationYN;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Schedule> scheduleList;
        private boolean scheduleSeatYN;

        public static Response fromDto(PlayDto playDto){
            return Response.builder()
                    .playId(playDto.getPlayId())
                    .theaterId(playDto.getTheaterId())
                    .troupeId(playDto.getTroupeId())
                    .playName(playDto.getPlayName())
                    .playDetails(playDto.getPlayDetails())
                    .posterUrl(playDto.getPosterUrl())
                    .ratings(playDto.getRatings())
                    .playGenre(playDto.getPlayGenre())
                    .playStartDate(playDto.getPlayStartDate())
                    .playEndDate(playDto.getPlayEndDate())
                    .runtime(playDto.getRuntime())
                    .actors(playDto.getActors())
                    .reservationYN(playDto.getReservationYN())
                    .createdAt(playDto.getCreatedAt())
                    .updatedAt(playDto.getUpdatedAt())
                    .scheduleList(playDto.getScheduleList())
                    .scheduleSeatYN(playDto.isScheduleSeatYN())
                    .build();
        }

    }

}
