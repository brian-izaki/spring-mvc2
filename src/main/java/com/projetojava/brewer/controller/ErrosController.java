package com.projetojava.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrosController {

    @GetMapping("/404")
    public String paginaNaoEncontrada() {
        return "404";
    }

}
