package academy.devdojo.springboot2.client;


import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
        //         GET com Exchange                 //
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

        //com postForObject
//        Anime kingdom = Anime.builder().name("Kingdom2").build();
//        Anime kingdomSalvo = new RestTemplate().postForObject("http://localhost:8081/animes/", kingdom, Anime.class);
//        log.info("Anime Salvo: {}", kingdomSalvo);

         //##########################################//
        //           POST com Exchange              //
       //##########################################//
        Anime samuraiChamploo = Anime.builder().name("SamuraiChamploo").build();

        ResponseEntity<Anime> samuraiChamplooSalvo = new RestTemplate().exchange("http://localhost:8081/animes/",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo, createJsonHeader()),
                Anime.class);
        log.info("Anime Salvo: {}", samuraiChamplooSalvo);

         //##########################################//
        //           PUT com Exchange               //
       //##########################################//
        Anime animeAtualizacao = samuraiChamplooSalvo.getBody();
        animeAtualizacao.setName("Samurai Champloo 2" );

        ResponseEntity<Void> samuraiChamplooAtualizado = new RestTemplate().exchange("http://localhost:8081/animes/",
                HttpMethod.PUT,
                new HttpEntity<>(animeAtualizacao, createJsonHeader()),
                Void.class);
        log.info("Anime Atualizado: {}", samuraiChamplooAtualizado);


         //##########################################//
        //           DELETE com Exchange            //
       //##########################################//

        ResponseEntity<Void> samuraiChamplooDeletado = new RestTemplate().exchange("http://localhost:8081/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeAtualizacao.getId());
        log.info("Anime Atualizado: {}", samuraiChamplooAtualizado);
    }


    //Metodo para Enviar Headers na requisição
    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
