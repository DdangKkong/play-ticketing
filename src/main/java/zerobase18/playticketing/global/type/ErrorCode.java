package zerobase18.playticketing.global.type;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * common error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),


    /**
     * customer, seller
     */
    ALREADY_USE_LOGIN_ID(HttpStatus.BAD_REQUEST, "이미 사용중인 아이디 입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),

    /**
     * play
     */
    THEATER_NOT_FOUND(HttpStatus.BAD_REQUEST, "극장을 찾을 수 없습니다."),
    SCHEDULE_DATE_INVALID(HttpStatus.BAD_REQUEST, "잘못된 연극 스케줄의 날짜입니다."),
    SCHEDULE_TIME_INVALID(HttpStatus.BAD_REQUEST, "잘못된 연극 스케줄의 시간입니다."),
    THEATER_CONNECT_DENIED(HttpStatus.FORBIDDEN, "접근할 수 없는 극장 정보입니다."),
    PLAY_NOT_FOUND(HttpStatus.BAD_REQUEST, "연극을 찾을 수 없습니다.")



    ;

    private final HttpStatus httpStatus;
    private final String description;
}
