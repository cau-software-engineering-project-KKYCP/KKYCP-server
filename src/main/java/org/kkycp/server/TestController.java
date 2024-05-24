package org.kkycp.server;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Object principal) {
        System.out.println("!!!! it is" + principal);
        return "hi";
    }
}

