package academy.devdojo.springboot2.requests;

import javax.validation.constraints.NotEmpty;

public class PaisOrigemPostRequestBody {

    @NotEmpty(message = "O nome do Pais Origem não pode ser vazio nem nulo")
    private String nomePais;

}
