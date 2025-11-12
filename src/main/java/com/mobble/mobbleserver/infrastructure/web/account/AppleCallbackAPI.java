package com.mobble.mobbleserver.infrastructure.web.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apple")
public class AppleCallbackAPI {

    @PostMapping("/callback")
    public ResponseEntity<String> appleCallback(
            @RequestParam String id_token,
            @RequestParam String code // code 를 받지 않으면 apple 에서 거절
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
