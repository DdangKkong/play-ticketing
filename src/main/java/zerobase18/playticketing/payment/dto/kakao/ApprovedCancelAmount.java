package zerobase18.playticketing.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovedCancelAmount {

    private int total;              // 이번 요청으로 취소된 전체 결제 금액
    private int tax_free;           // 이번 요청으로 취소된 비과세 금액
    private int vat;                // 이번 요청으로 취소된 부가세 금액
    private int point;              // 이번 요청으로 취소된 사용한 포인트 금액
    private int discount;           // 이번 요청으로 취소된 할인 금액
    private int green_deposit;      // 이번 요청으로 취소된 컵 보증금

}
