package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Para informar que sera utilizado o JUnit com Spring
@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks //Utilizar quando quer testar a classe em si
    private AnimeService animeService;

    @Mock //Utilizada em todas as classes dentro da classe que quer testar
    private AnimeRepository animeRepositoryMock;



    @BeforeEach
//Sera utilizado sempre antes da execução dos testes
    void setUp(){
        PageImpl<Anime> animePage= new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        //Mockito para List COM Pageble
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        //Mockito para List SEM Pageble
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //Mockito para FindByID
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        //Mockito para FindByName
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //Mockito para Save
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

          //Mockito para Delete
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));


    }

    @Test
    @DisplayName("ListAll - Retorna lista de animes COM paginação - quando sucesso")
    void listAll_ReturnListOfAnimesInsidPageObject_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("listAllNonPageable - Retorna lista de animes SEM paginação - quando sucesso")
    void listAllNonPageable_ReturnListOfAnimes_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException - Retorna animes pelo id - quando sucesso")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSucessful(){
        Long idEsperado = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException - throws BadRequestException - quando quando anime nao encontrado")
    void findByIdOrThrowBadRequestException_ThrowBadRequestException_WhenAnimeIsNotFound(){
        //Mockito para FindByID
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());


        Assertions.assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(() -> this.animeService.findByIdOrThrowBadRequestException(1));
    }


    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando sucesso")
    void findByName_ReturnsListOfAnime_WhenSucessful(){
        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.findByName("anime");

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
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        String nomeEsperado = AnimeCreator.createValidAnime().getName();
        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save - Retorna animes - quando sucesso")
    void save_ReturnsAnime_WhenSucessful(){
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("Update(replace) - Retorna animes - quando sucesso")
    void update_ReturnsAnime_WhenSucessful(){

        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
         }


    @Test
    @DisplayName("Delete - Remove animes - quando sucesso")
    void Delete_RemovesAnime_WhenSucessful(){

        Assertions.assertThatCode(() -> animeService.delete(1))
                .doesNotThrowAnyException();
    }



}