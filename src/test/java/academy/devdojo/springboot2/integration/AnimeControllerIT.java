package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

//Inicia o SpringBoot para teste
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//Passa porta aleatoria para não travar a 8080
@AutoConfigureTestDatabase//utiliza os testes com banco de dados em memoria
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)//Apaga completamente o banco em memoria antes de iniciar um teste dessa classe
class AnimeControllerIT {

    @Autowired
    AnimeRepository animeRepository;

    @Autowired
    private TestRestTemplate testRestTemplate; //Encontra a porta que é inicializada

    //outra forma de obter a porta que esta sendo utilizada nos testes
    @LocalServerPort
    private int port;


    @Test
    @DisplayName("List - Retorna lista de animes COM paginação - quando sucesso")
    void list_ReturnListOfAnimesInsidPageObject_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String nomeEsperado = animeSalvo.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("ListAll - Retorna lista de animes SEM paginação - quando sucesso")
    void listAll_ReturnListOfAnimes_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String nomeEsperado = animeSalvo.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("FindById - Retorna animes pelo id - quando sucesso")
    void findById_ReturnsAnime_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        Long idEsperado = animeSalvo.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}",Anime.class,idEsperado);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando sucesso")
    void findByName_ReturnsListOfAnime_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String nomeEsperado = animeSalvo.getName();

        String url =  String.format("/animes/find?name=%s", nomeEsperado);

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando anime Não Encontrado")
    void findByName_ReturnsListOfAnime_WhenAnimeIsNotFound(){

        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save - Retorna animes - quando sucesso")
    void save_ReturnsAnime_WhenSucessful(){

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Update(replace) - Retorna animes - quando sucesso")
    void update_ReturnsAnime_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        animeSalvo.setName("Novo nome");

       ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
               HttpMethod.PUT, new HttpEntity<>(animeSalvo), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete - Remove animes - quando sucesso")
    void Delete_RemovesAnime_WhenSucessful(){
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE,null, Void.class, animeSalvo.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
