package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController//Especifica que a classe é um controller e adiciona o @ResponseBody(o retorno dos metodos dessa classe sao apenas String)
@RequestMapping("anime")//URL da Classe
@Log4j2
public class AnimeController {

    private DateUtil dateUtil;

    public AnimeController(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    //localhost:8080/anime/list
    @GetMapping("list")//Metodo passando URL do Metodo
    public List<Anime> animes(){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return List.of( new Anime("DBZ"), new Anime("One Piece"));
    }
}
