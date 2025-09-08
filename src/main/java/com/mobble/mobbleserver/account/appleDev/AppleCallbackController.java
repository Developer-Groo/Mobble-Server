package com.mobble.mobbleserver.account.appleDev;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/apple")
public class AppleCallbackController {

    @PostMapping("/callback")
    public ResponseEntity<String> appleCallback(
            @RequestParam String id_token
    ) {
        log.info("🍏 Apple id_token: {}", id_token);
        return ResponseEntity.status(HttpStatus.OK)
                .body("✅ code & id_token 수신 완료\n\n " + id_token);
    }
}
