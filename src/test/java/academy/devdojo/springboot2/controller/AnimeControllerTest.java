package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

//Para informar que sera utilizado o JUnit com Spring
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks //Utilizar quando quer testar a classe em si
    private AnimeController animeController;

    @Mock //Utilizada em todas as classes dentro da classe que quer testar
    private AnimeService animeServiceMock;



    @BeforeEach//Sera utilizado sempre antes da execução dos testes
    void setUp(){
        PageImpl<Anime> animePage= new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);
    }

    @Test
    @DisplayName("Retorna lista de animes com paginação - quando sucesso")
    void list_ReturnListOfAnimesInsidPageObject_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.animes(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(nomeEsperado);

    }

}