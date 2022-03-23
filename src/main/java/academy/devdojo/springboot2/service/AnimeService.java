package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;


    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime não encontrado"));// Em caso de erro  Retorna Bad Request e mensagem.
    }

    @Transactional//Só executa o commit apos a finalização do metodo, caso ocorra alguma exceção ele faz o rollback
    public Anime save( AnimePostRequestBody animePostRequestBody){
        return animeRepository.save(AnimeMapper.INSTANCIA.toAnime(animePostRequestBody)); //retorna o anime adicionado
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));//remove (metodo padrao do List) o item de acordo com o ID encontrado pelo metodo FindByID
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
       //verifica se o Anime existe no banco de dados
        Anime animeSalvo = findByIdOrThrowBadRequestException(animePutRequestBody.getId());
        Anime anime = AnimeMapper.INSTANCIA.toAnime(animePutRequestBody);
        anime.setId(animeSalvo.getId());
        animeRepository.save(anime);
    }
}
