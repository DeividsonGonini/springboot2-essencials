package academy.devdojo.springboot2.requests;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class CategoriaPostRequestBody {

    @NotEmpty(message = "Nome da categoria nao pode ser nulo")
    private String nomeCategoria;

}
