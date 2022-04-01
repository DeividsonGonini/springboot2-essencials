package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
//Adiciona um nome para a classe de testes
@Log4j2
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


    @Test
    @DisplayName("Atualizar anime - quando sucesso")
    void save_AtualizarAnime_QuandoSucesso(){
        Anime animeParaSalvar = createAnime();

        Anime animeSalvo = this.animeRepository.save(animeParaSalvar);

        animeSalvo.setName("Overlod");

        Anime animeAtualizado = this.animeRepository.save(animeSalvo);

        Assertions.assertThat(animeAtualizado).isNotNull();

        Assertions.assertThat(animeAtualizado.getId()).isNotNull();

        Assertions.assertThat(animeAtualizado.getName()).isEqualTo(animeSalvo.getName());
    }


    @Test
    @DisplayName("Remover anime - quando sucesso")
    void delete_RemoverAnime_QuandoSucesso(){
        Anime animeParaSalvar = createAnime();

        Anime animeSalvo = this.animeRepository.save(animeParaSalvar);

        this.animeRepository.delete(animeSalvo);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSalvo.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }


    @Test
    @DisplayName("Busca por Nome - quando sucesso")
    void BuscaPorNome_RetornaListaDeAnime_QuandoSucesso(){
        Anime animeParaSalvar = createAnime();

        Anime animeSalvo = this.animeRepository.save(animeParaSalvar);

        String name = animeSalvo.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty();

        Assertions.assertThat(animes).contains(animeSalvo);
    }

    @Test
    @DisplayName("Busca por Nome retorna Lista vazia - quando Anime não é encontrado")
    void BuscaPorNome_RetornaListaVazia_QuandoAnimeNaoEncontrado(){

        List<Anime> animes = this.animeRepository.findByName("Nao Existe");

        Assertions.assertThat(animes).isEmpty();
    }



    //criar anime
    private Anime createAnime(){
        return Anime.builder()
                .name("Hajjime no Ippo")
                .build();
    }

}