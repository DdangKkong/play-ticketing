package zerobase18.playticketing.troupe.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zerobase18.playticketing.auth.dto.TroupeSignInDto;
import zerobase18.playticketing.auth.dto.TroupeSignUpDto;
import zerobase18.playticketing.auth.security.TokenProvider;
import zerobase18.playticketing.auth.service.AuthService;
import zerobase18.playticketing.customer.dto.DeleteCustomer;
import zerobase18.playticketing.troupe.dto.DeleteTroupe;
import zerobase18.playticketing.troupe.dto.SearchTroupe;
import zerobase18.playticketing.troupe.dto.TroupeInfo;
import zerobase18.playticketing.troupe.dto.UpdateTroupeDto;
import zerobase18.playticketing.troupe.entity.Troupe;
import zerobase18.playticketing.troupe.service.TroupeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/troupe")
public class TroupeController {

    private final TroupeService troupeService;

    private final TokenProvider tokenProvider;

    private final AuthService authService;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> sellerSignUp(@RequestBody @Valid TroupeSignUpDto signUpDto) {

        return ResponseEntity.ok().body(
                signUpDto.from(troupeService.signUp(signUpDto))
        );
    }

    /**
     * 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<?> sellerSignIn(@RequestBody @Valid TroupeSignInDto sign) {

        Troupe troupe = authService.authenticatedTroupe(sign);

        return ResponseEntity.ok(
                tokenProvider.createToken(
                        troupe.getLoginId(),
                        troupe.getUserType()
                )
        );
    }

    /**
     * 정보 조회
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_TROUPE')")
    public List<TroupeInfo> SearchTroupe(
            @RequestBody @Valid SearchTroupe.Request request
    ) {

        return troupeService.searchTroupe(request)
                .stream().map(
                        troupeDto -> TroupeInfo.builder()
                                .name(troupeDto.getName())
                                .company(troupeDto.getCompany())
                                .phone(troupeDto.getPhone())
                                .email(troupeDto.getEmail())
                                .address(troupeDto.getAddress()).build()
                ).collect(Collectors.toList());
    }

    /**
     * 정보 수정
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_TROUPE')")
    public UpdateTroupeDto.Response updateTroupe(
            @RequestParam @Valid String loginId,
            @RequestParam @Valid String password,
            @RequestBody @Valid UpdateTroupeDto.Request request
    ) {
        return UpdateTroupeDto.Response.fromEntity(
                troupeService.updateTroupe(loginId, password, request)
        );
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_TROUPE')")
    public DeleteTroupe.Response deleteTroupe(
            @RequestBody @Valid DeleteTroupe.Request request
    ) {
        return DeleteTroupe.Response.from(
                troupeService.deleteTroupe(request.getLoginId(), request.getPassword())
        );
    }

}
