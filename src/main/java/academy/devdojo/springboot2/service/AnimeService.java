package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnimeService {
    //private final AnimeRepository animeRepository;

    private static List<Anime> animes;

    static{
        animes = new ArrayList<>(List.of( new Anime(1L,"DBZ"), new Anime(2L,"One Piece")));
    }

    public List<Anime> listAll() {
        return animes;
    }

    public Anime findById(long id) {
        return animes.stream()//Varre a lista
                .filter(anime -> anime.getId().equals(id))//filtro passando o ID
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime não encontrado"));// Em caso de erro  Retorna Bad Request e mensagem.
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(3,100000)); //gera ID randomico automatico
        animes.add(anime); //adiciona o anime
        return anime; //retorna o anime adicionado
    }

    public void delete(long id) {
        animes.remove(findById(id));//remove (metodo padrao do List) o item de acordo com o ID encontrado pelo metodo FindByID
    }
}
