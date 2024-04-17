package zerobase18.playticketing.play.entity;

import jakarta.persistence.*;
import lombok.*;
import zerobase18.playticketing.payment.entity.Reservation;
import zerobase18.playticketing.theater.entity.Seat;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleAndSeat_id")
    private int id;

    // 스케줄 고유번호
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    // 좌석 고유번호
    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    // 예약 고유번호
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}