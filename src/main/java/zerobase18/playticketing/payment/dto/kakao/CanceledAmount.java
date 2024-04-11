package zerobase18.playticketing.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CanceledAmount {

    private int total;              // 취소된 전체 누적 금액
    private int tax_free;           // 취소된 비과세 누적 금액
    private int vat;                // 취소된 부가세 누적 금액
    private int point;              // 취소된 포인트 누적 금액
    private int discount;           // 취소된 할인 누적 금액
    private int green_deposit;      // 취소된 누적 컵 보증금

}
