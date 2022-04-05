package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
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

         //Mockito para List COM Pageble
         BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        //Mockito para List SEM Pageble
        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //Mockito para FindByID
        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        //Mockito para FindByName
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //Mockito para Save
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        //Mockito para Update
        //Como é void o doNothing é para ele não fazer nada
        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        //Mockito para Delete
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());


    }

    @Test
    @DisplayName("List - Retorna lista de animes COM paginação - quando sucesso")
    void list_ReturnListOfAnimesInsidPageObject_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeController.animes(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("ListAll - Retorna lista de animes SEM paginação - quando sucesso")
    void listAll_ReturnListOfAnimes_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("FindById - Retorna animes pelo id - quando sucesso")
    void findById_ReturnsAnime_WhenSucessful(){
        Long idEsperado = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando sucesso")
    void findByName_ReturnsListOfAnime_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando anime Não Encontrado")
    void findByName_ReturnsListOfAnime_WhenAnimeIsNotFound(){

        //Sobrescrevendo o metodo do BeforEach
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save - Retorna animes - quando sucesso")
    void save_ReturnsAnime_WhenSucessful(){

        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Update(replace) - Retorna animes - quando sucesso")
    void update_ReturnsAnime_WhenSucessful(){

        Assertions.assertThatCode(() ->animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                        .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test
    @DisplayName("Delete - Remove animes - quando sucesso")
    void Delete_RemovesAnime_WhenSucessful(){

        Assertions.assertThatCode(() ->animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}