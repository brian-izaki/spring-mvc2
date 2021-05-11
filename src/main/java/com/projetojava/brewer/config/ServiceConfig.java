package com.projetojava.brewer.config;

import com.projetojava.brewer.storage.FotoStorage;
import com.projetojava.brewer.storage.local.FotoStorageLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.projetojava.brewer.service.CadastroCervejaService;

@Configuration
@ComponentScan(basePackageClasses = CadastroCervejaService.class)
public class ServiceConfig {

    @Bean
    public FotoStorage fotoStorage() {
        return new FotoStorageLocal();
    }

}
