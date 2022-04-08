package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByNomeCategoria(String nomeCategoria);
}
