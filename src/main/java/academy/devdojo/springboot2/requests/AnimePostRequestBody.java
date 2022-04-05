package academy.devdojo.springboot2.requests;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class AnimePostRequestBody {

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    private String name;

}
