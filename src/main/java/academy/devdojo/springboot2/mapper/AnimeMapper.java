package academy.devdojo.springboot2.mapper;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCIA = Mappers.getMapper(AnimeMapper.class);

    @Mapping( source = "name" , target = "name" )
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}
