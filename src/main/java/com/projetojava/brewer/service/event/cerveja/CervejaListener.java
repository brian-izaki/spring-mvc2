package com.projetojava.brewer.service.event.cerveja;

import com.projetojava.brewer.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CervejaListener {

    @Autowired
    private FotoStorage fotoStorage;

    // condition fala que esse evento será chamado apenas se a condição for verdadeira. uma forma de diminuir linha
    // e não colocar if
    @EventListener(condition = "#evento.temFoto()")
    public void cervejaSalva(CervejaSalvaEvent evento) {
        fotoStorage.salvar(evento.getCerveja().getFoto());
    }
}
