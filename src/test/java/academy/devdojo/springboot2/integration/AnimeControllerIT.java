package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
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
import org.springframework.http.HttpMethod;

//Inicia o SpringBoot para teste
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//Passa porta aleatoria para não travar a 8080
@AutoConfigureTestDatabase//utiliza os testes com banco de dados em memoria
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


}
