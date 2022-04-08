package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.PaisOrigem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaisOrigemRepository extends JpaRepository<PaisOrigem, Integer> {

    List<PaisOrigem> findByNomePais(String nomePais);
}
