package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria atualizar(Long codigo, Categoria categoria){
        Categoria categoriaSalva = categoriaRepository.findOne(codigo);
        if(categoriaSalva == null){
            throw new EmptyResultDataAccessException(1);
        }

        //transfere dados de pessoa para pessoaSalva, ignorando o campo codigo
        BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");

        return categoriaRepository.save(categoriaSalva);
    }
}
