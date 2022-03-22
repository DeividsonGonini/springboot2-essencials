package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

//Informar a classe e o tipo de atributo usado no ID
public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
