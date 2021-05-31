package com.projetojava.brewer.controller;

import com.projetojava.brewer.repository.Vendas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private Vendas vendas;

    @GetMapping
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("Dashboard");

        mv.addObject("vendasNoAno", vendas.valorTotalNoAno());
        mv.addObject("vendasNoMes", vendas.valorTotalNoMes());
        mv.addObject("ticketMedio", vendas.valorTicketMedioNoAno());

        return mv;
    }

}
