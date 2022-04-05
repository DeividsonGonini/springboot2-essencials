package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

    //Criar anime sem ID
    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Hajjime no Ippo")
                .build();
    }

    //Criar anime com ID valido
    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Hajjime no Ippo")
                .id(1L)
                .build();
    }

    //Atualiza anime
    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .name("Naruto")
                .id(1L)
                .build();
    }

}
