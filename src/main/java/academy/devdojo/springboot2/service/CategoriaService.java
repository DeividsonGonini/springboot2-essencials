package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Categoria;
import academy.devdojo.springboot2.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    CategoriaRepository categoriaRepository;

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    public List<Categoria> findByName(String name){
        return categoriaRepository.findByNomeCategoria(name);
    }

}
