package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;


    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }


    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }


    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime não encontrado"));// Em caso de erro  Retorna Bad Request e mensagem.
    }


    public List<Anime> findByCategoriaPais(List<String> categoria, String paisOrigem){
        return animeRepository.findByCategoriaAndPaisOrigem(converteListaStringParaInt(categoria),converteStringParaInt(paisOrigem));
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


    public static <T, U> List<U>
    convertStringToIntegerList(List<T> listOfString,
                               Function<T, U> function)
    {
        return listOfString.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public List<Integer> converteListaStringParaInt(List<String> listaString){
    List<Integer> listaInteiros = convertStringToIntegerList( listaString, Integer::parseInt);
    return listaInteiros;
    }

    public Integer converteStringParaInt(String string){
      return Integer.parseInt(string);
    }


}
