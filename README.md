# 연극의 민족
## play-ticketing (연극 예매 시스템)
### 설계 / 개발

**사용 기술**

- Back-end : Java, Spring Boot, Spring Data Jpa, Spring Security, Kakao Payment API, Toss Payment API, AWS, Docker
- Database : Mysql, Redis
- Tools : IntelliJ IDEA, Postman
- Collaboration : GitHub, Notion, Slack, Gather
- **ERD**
![image](https://github.com/DdangKkong/play-ticketing/assets/131670203/d5c74c24-e1a5-43a3-bc63-b26ccca865ce)

### 구현 기능

- 로그인 및 회원가입 / 권한 체크
    - 고객, 연극업체, 극장업체, 관리자 회원가입(로그인 아이디 중복 불가)
    - 로그인 - 로그인 아이디, 비밀번호 입력 후 로그인(JWT 토큰 발급)

- Q&A게시판 기능
    - 질문 작성(고객 권한을 가진 유저만 가능)
    - 답변 작성(관리자 권한을 가진 유저만 가능)
        - 이미 답변이 등록되어 있으면 답변 등록 불가
    - 질문 목록 전체 조회 페이징처리
    - 질문 상세 조회(답변이 등록되어 있다면 답변도 같이 조회)
    - 질문 수정
    - 질문 삭제 시 답변도 같이 삭제.(삭제는 고객 권한을 가지고 질문 등록자만 가능)

- 리뷰 게시판 기능
    - 리뷰
        - 리뷰 작성 시 제목, 내용, 별점(1~5점으로 제한) 작성(ReservationState 확인 후 SUCCESS일 경우에만 작성 가능)
        - 리뷰 상세 조회 기능: 연극 제목, 이름, 제목, 내용, 좋아요 수, 댓글(댓글은 리스트로 반환), 리뷰 상세 조회 시 레디스에 조회수 저장
        - 리뷰 전체 목록 조회 페이징 처리
        - 리뷰 수정 기능
        - 리뷰 삭제(삭제 시 댓글, 좋아요, 조회수도 같이 삭제)
    - 댓글
        - 댓글 CRUD
        - 리뷰 확인 후 작성
    - 좋아요
        - 작성한 리뷰에 좋아요 기능
        - 이미 좋아요를 누른 상태에서, 다시 요청 시 좋아요 취소
    - 조회수
        - 리뷰 상세 조회 시 레디스에 저장
        - 같은 고객 고유 번호로 다시 상세 조회 시 유효성 검사 후 조회수 증가 X
        - 리뷰 삭제 시 조회수 정보 레디스에서 전부 삭제

- 극장 및  좌석 기능
    - 극장 업체 권한을 가진 유저만 생성, 수정, 삭제 가능
    - 누구나 조회 가능
    - 극장 생성시 좌석이 함께 생성됨
    - 극장 및 좌석 수정시 극장 정보는 업데이트 되며, 좌석은 모두 삭제 후 다시 생성됨
    - 극장 및 좌석 삭제시 deletedAt 데이터를 넣어줌 (모든 데이터 DB 에 보관), 프론트에서 보이지 않게 처리

- 연극 및 스케줄 기능
    - 연극 업체 권한을 가진 유저만 생성, 수정, 삭제 가능
    - 누구나 조회 가능
    - 연극 생성시 스케줄과 스케줄 별 좌석이 함께 생성됨
    - 연극 및 스케줄 수정시 연극 정보는 업데이트 되며, 스케줄과 스케줄 별 좌석은 모두 삭제 후 다시 생성됨
    - 연극 및 스케줄 삭제시 deletedAt 데이터를 넣어줌 (모든 데이터 DB 에 보관), 프론트에서 보이지 않게 처리

- 예약 신청 및 신청 취소 기능
    - 예약 취소 시 기간에 따른 환불 금액 설정
    - 연극 상영 날짜가 지나거나 당일 취소 불가

- 결제 및 결제 취소 기능
    - 카카오 페이와 토스 페이먼츠 API를 통한 결제
    - 설정된 환불 금액에 따라 전액 및 부분 환불
    - 이미 결제했거나 결제 취소한 경우 결제 불가능

  결제 과정 [(링크)](https://www.notion.so/168b61a27d34472a9744b7df1860d646?pvs=21)

### 연극의 민족 자세히 보기 [(링크)](https://www.notion.so/ae811d0103104c3fafe6c971a4c59760)