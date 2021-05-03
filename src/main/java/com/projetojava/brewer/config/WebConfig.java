package com.projetojava.brewer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.projetojava.brewer.controller.CervejasController;

@Configuration // diz que é uma classe de configuração
@ComponentScan(basePackageClasses = { CervejasController.class } ) // faz a leitura das nossas controllers
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
	
}
