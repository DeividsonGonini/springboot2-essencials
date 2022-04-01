package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
//Adiciona um nome para a classe de testes
@DisplayName("Teste para AnimeRepository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Salvar anime - quando sucesso")
    void save_PersistirtAnime_QuandoSucesso(){
        Anime animeParaSalvar = createAnime();
        Anime animeSalvo = this.animeRepository.save(animeParaSalvar);

        Assertions.assertThat(animeSalvo).isNotNull();

        Assertions.assertThat(animeSalvo.getId()).isNotNull();

        Assertions.assertThat(animeSalvo.getName()).isEqualTo(animeParaSalvar.getName());

    }

    //criar anime
    private Anime createAnime(){
        return Anime.builder()
                .name("Hajjime no Ippo")
                .build();
    }

}