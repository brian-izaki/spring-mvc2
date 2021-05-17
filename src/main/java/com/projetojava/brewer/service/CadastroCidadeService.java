package com.projetojava.brewer.service;

import com.projetojava.brewer.model.Cidade;
import com.projetojava.brewer.repository.Cidades;
import com.projetojava.brewer.service.exception.NomeCidadeJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroCidadeService {

    @Autowired
    private Cidades cidades;

    @Transactional
    public void salvar(Cidade cidade) {
        Optional<Cidade> cidadeOptional = cidades.findByNomeAndEstadoCodigo(cidade.getNome(), cidade.getEstado().getCodigo());

        if (cidadeOptional.isPresent()){
            throw new NomeCidadeJaCadastradoException("Nome da cidade já está cadastrado para este estado");
        }

        cidades.save(cidade);
    }

}
