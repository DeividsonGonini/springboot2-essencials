package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Informar a classe e o tipo de atributo usado no ID
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    //Criando busca por name (tem que ser o mesmo atributo da Entidade)
    List<Anime> findByName(String name);

}
