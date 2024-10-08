package zerobase18.playticketing.company.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase18.playticketing.auth.dto.SignInDto;
import zerobase18.playticketing.auth.dto.CompanySignUpDto;
import zerobase18.playticketing.auth.security.TokenProvider;
import zerobase18.playticketing.auth.service.AuthService;
import zerobase18.playticketing.company.dto.CompanyInfo;
import zerobase18.playticketing.company.dto.DeleteCompany;
import zerobase18.playticketing.company.dto.SearchCompany;
import zerobase18.playticketing.company.dto.UpdateCompanyDto;
import zerobase18.playticketing.company.entity.Company;
import zerobase18.playticketing.company.service.CompanyService;
import zerobase18.playticketing.global.dto.SendMailRequest;
import zerobase18.playticketing.global.dto.VerifyMailRequest;
import zerobase18.playticketing.global.service.MailService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    private final TokenProvider tokenProvider;

    private final AuthService authService;

    private final RedisTemplate<String, String> redisTemplate;

    private final MailService mailService;

    /**
     * 극장 업체 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> companySignUp(@RequestBody @Valid CompanySignUpDto signUpDto) {

        return ResponseEntity.ok().body(
                signUpDto.from(companyService.signUp(signUpDto))
        );
    }

    /**
     * 이메일 인증 번호 전송
     */
    @PostMapping("/mail/certification")
    public ResponseEntity<?> sendCertificationMail(
            @RequestBody SendMailRequest request
    ) {

        mailService.sendAuthMail(request.getEmail());

        return ResponseEntity.ok().body("인증번호가 전송되었습니다");
    }


    /**
     * 이메일 인증
     */
    @PostMapping("/mail/verify")
    public ResponseEntity<?> sendVerifyMail(@RequestBody VerifyMailRequest request) {

        mailService.companyVerifyEmail(request.getEmail(), request.getCode());

        return ResponseEntity.ok(HttpStatus.OK);

    }


    /**
     * 극장 업체 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<?> companySignIn(@RequestBody @Valid SignInDto signInDto) {

        Company company = authService.authenticatedCompany(signInDto);

        String token = tokenProvider.createToken(company.getLoginId(), company.getUserType());

        redisTemplate.opsForValue().set("JWT_TOKEN:" + company.getLoginId(), token, tokenProvider.getTokenValidTime());


        return ResponseEntity.ok(
                token
        );
    }

    /**
     * 극장 로그 아웃
     */

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public ResponseEntity<Void> logout(@RequestParam @Valid Integer companyId) {

        companyService.logout(companyId);
        return ResponseEntity.ok().build();
    }

    /**
     * 극장 업체 정보 조회
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public List<CompanyInfo> SearchCompany(
            @RequestBody @Valid SearchCompany.Request request
    ) {

        return companyService.searchCompany(request)
                .stream().map(
                        companyDto -> CompanyInfo.builder()
                                .name(companyDto.getName())
                                .company(companyDto.getCompany())
                                .phone(companyDto.getPhone())
                                .email(companyDto.getEmail())
                                .address(companyDto.getAddress()).build()
                ).collect(Collectors.toList());
    }

    /**
     * 정보 수정
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public UpdateCompanyDto.Response updateTroupe(
            @RequestParam @Valid String loginId,
            @RequestParam @Valid String password,
            @RequestBody @Valid UpdateCompanyDto.Request request
    ) {
        return UpdateCompanyDto.Response.fromEntity(
                companyService.updateCompany(loginId, password, request)
        );
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_COMPANY')")
    public DeleteCompany.Response deleteTroupe(
            @RequestBody @Valid DeleteCompany.Request request
    ) {
        return DeleteCompany.Response.from(
                companyService.deleteCompany(request.getLoginId(), request.getPassword())
        );
    }

}
