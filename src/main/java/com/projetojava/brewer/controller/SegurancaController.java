package com.projetojava.brewer.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class SegurancaController {

    @GetMapping
    public String login(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/cervejas";
        }
        return "Login";
    }
}
