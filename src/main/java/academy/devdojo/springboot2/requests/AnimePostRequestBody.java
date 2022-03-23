package academy.devdojo.springboot2.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AnimePostRequestBody {

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    private String name;
}
