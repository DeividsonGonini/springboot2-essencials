package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.domain.PaisOrigem;
import academy.devdojo.springboot2.repository.PaisOrigemRepository;
import academy.devdojo.springboot2.requests.PaisOrigemPostRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaisOrigemService {

    PaisOrigemRepository paisOrigemRepository;

    public List<PaisOrigem> listAll(){
        return paisOrigemRepository.findAll();

    }

    public List <PaisOrigem> findByName(String name){
        return paisOrigemRepository.findByNomePais(name);
    }

//    public Anime save(PaisOrigemPostRequestBody paisOrigemPostRequestBody){
//        return paisOrigemRepository.save();
//    }

}
