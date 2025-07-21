package com.mobble.mobbleserver.account.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthDetailsInfoController {

    @GetMapping("/signup/details-info")
    public String signupDetailsPage(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String socialProvider,
            @RequestParam String socialId,
            Model model
    ) {
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        model.addAttribute("socialProvider", socialProvider);
        model.addAttribute("socialId", socialId);

        return "signup-details";
    }

}
