package academy.devdojo.springboot2.client;


import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8081/animes/{id}", Anime.class,1);
        log.info(entity);

        //##########################################//
        //                 GET                      //
        //##########################################//

        Anime object = new RestTemplate().getForObject("http://localhost:8081/animes/{id}", Anime.class,1);
        log.info(object);

        //Listando tudo Utilizando Array
        Anime[] animes = new RestTemplate().getForObject("http://localhost:8081/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        //##########################################//
        //         Exchange com GET                 //
        //##########################################//

        //Chamada com Exchange para pegar a resposta e converter automaticamente para uma lista
        //@formatter:off
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8081/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        //@formatter:on
        log.info(exchange.getBody());


        //##########################################//
        //               POST                       //
        //##########################################//

        Anime kingdom = Anime.builder().name("Kingdom").build();
        Anime kingdomSalvo = new RestTemplate().postForObject("http://localhost:8081/animes/", kingdom, Anime.class);
        log.info("Anime Salvo: {}", kingdomSalvo);
    }
}
