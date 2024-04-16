package zerobase18.playticketing.payment.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoOrderResponseDto {

    private String tid;                                             // 결제 고유번호
    private String cid;                                             // 가맹점 코드
    private String status;                                          // 결제 상태
    private String partner_order_id;                                // 가맹점 주문번호
    private String partner_user_id;                                 // 가맹점 회원 id
    private String payment_method_type;                             // 결제 수단
    private Amount amount;                                          // 결제 금액
    private CanceledAmount canceled_amount;                         // 취소된 금액
    private CancelAvailableAmount cancel_available_amount;          // 취소 가능 금액
    private String item_name;                                       // 상품 이름
    private String item_code;                                       // 상품 코드
    private int quantity;                                           // 상품 수량
    private LocalDateTime created_at;                               // 결제 준비 요청 시각
    private LocalDateTime approved_at;                              // 결제 승인 시각
    private LocalDateTime canceled_at;                              // 결제 취소 시각
    private SelectedCardInfo selected_card_info;                    // 결제 카드 정보
    private PaymentActionDetails[] payment_action_details;          // 결제/취소 상세


}
