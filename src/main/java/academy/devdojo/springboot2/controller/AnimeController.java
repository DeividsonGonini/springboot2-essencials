package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//Especifica que a classe é um controller e adiciona o @ResponseBody(o retorno dos metodos dessa classe sao apenas String)
@RequestMapping("animes")//URL da Classe
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;


    //localhost:8080/animes
    @GetMapping//Metodo passando URL do Metodo
    public ResponseEntity<Page<Anime>> animes(Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll() {
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")//Metodo passando URL do Metodo
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    //@RequestParam - Extrai o parametro da consulta que sera executada
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/filtro")
    public ResponseEntity<List<Anime>>findByCategoriaPais(
            @RequestParam  List<String> idCategoria,
           @RequestParam String idPaisOrigem){
        return ResponseEntity.ok(animeService.findByCategoriaPais(idCategoria, idPaisOrigem));
    };


    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody) {
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")//Metodo passando URL do Metodo
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping//Metodo passando URL do Metodo
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
