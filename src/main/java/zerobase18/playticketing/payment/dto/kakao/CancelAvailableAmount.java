package zerobase18.playticketing.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelAvailableAmount {

    private int total;              // 전체 취소 가능 금액
    private int tax_free;           // 취소 가능한 비과세 금액
    private int vat;                // 취소 가능한 부가세 금액
    private int point;              // 취소 가능한 포인트 금액
    private int discount;           // 취소 가능한 할인 금액
    private int green_deposit;      // 취소 가능한 컵 보증금

}
