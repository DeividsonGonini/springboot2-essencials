package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {
    //private final AnimeRepository animeRepository;

    private List<Anime> animes =List.of( new Anime(1L,"DBZ"), new Anime(2L,"One Piece"));

    public List<Anime> listAll() {
        return animes;
    }

    public Anime findById(long id) {
        return animes.stream()//Varre a lista
                .filter(anime -> anime.getId().equals(id))//filtro passando o ID
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime não encontrado"));// Em caso de erro  Retorna Bad Request e mensagem.
    }

}
