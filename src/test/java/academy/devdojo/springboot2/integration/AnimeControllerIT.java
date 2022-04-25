package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.DevDojoUser;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.repository.DevDojoUserRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

//Inicia o SpringBoot para teste
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Passa porta aleatoria para não travar a 8080
@AutoConfigureTestDatabase//utiliza os testes com banco de dados em memoria
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)//Apaga completamente o banco em memoria antes de iniciar um teste dessa classe
class AnimeControllerIT {

    @Autowired
    AnimeRepository animeRepository;
    @Autowired
    DevDojoUserRepository devDojoUserRepository;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    //outra forma de obter a porta que esta sendo utilizada nos testes
//    @LocalServerPort
//    private int port;

    //Usuario Criado no Banco de Dados em Memoria sempre antes de executar os testes
    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("DevDojo Academy")
            .password("{bcrypt}$2a$10$QcgqYHnLg6za6SNs8K0jeOG.7X9GTf3W/vNFuHJZA3GxUW7IUDHIq")
            .username("devdojo")
            .authorities("ROLE_USER")
            .build();
    //Usuario Criado no Banco de Dados em Memoria sempre antes de executar os testes
    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("Deividson Gonini")
            .password("{bcrypt}$2a$10$QcgqYHnLg6za6SNs8K0jeOG.7X9GTf3W/vNFuHJZA3GxUW7IUDHIq")
            .username("gonini")
            .authorities("ROLE_ADMIN,ROLE_USER")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int porta) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + porta)
                    .basicAuthentication("devdojo", "teste123");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int porta) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + porta)
                    .basicAuthentication("gonini", "teste123");
            return new TestRestTemplate(restTemplateBuilder);
        }

    }

    @Test
    @DisplayName("List - Retorna lista de animes COM paginação - quando sucesso")
    void list_ReturnListOfAnimesInsidPageObject_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        String nomeEsperado = animeSalvo.getName();

        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null,
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
    void listAll_ReturnListOfAnimes_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        String nomeEsperado = animeSalvo.getName();

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null,
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
    void findById_ReturnsAnime_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        Long idEsperado = animeSalvo.getId();

        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, idEsperado);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    @DisplayName("FindByName - Retorna lista de animes pelo nome - quando sucesso")
    void findByName_ReturnsListOfAnime_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        String nomeEsperado = animeSalvo.getName();

        String url = String.format("/animes/find?name=%s", nomeEsperado);

        List<Anime> animes = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
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
    void findByName_ReturnsListOfAnime_WhenAnimeIsNotFound() {
        devDojoUserRepository.save(USER);

        List<Anime> animes = testRestTemplateRoleUser.exchange("/animes/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }


    @Test
    @DisplayName("Save - Retorna animes - quando sucesso")
    void save_ReturnsAnime_WhenSucessful() {
        devDojoUserRepository.save(USER);

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Update(replace) - Retorna animes - quando sucesso")
    void update_ReturnsAnime_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        animeSalvo.setName("Novo nome");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes",
                HttpMethod.PUT, new HttpEntity<>(animeSalvo), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Delete - Remove animes - quando sucesso")
    void delete_RemovesAnime_WhenSucessful() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(ADMIN);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, animeSalvo.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete - Retorna 403 - quando usuario nao é Admin")
    void delete_Returns403_WhenUserIsNotAdimin() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        devDojoUserRepository.save(USER);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, animeSalvo.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
