package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestBody {

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    @Schema(description = "Este é o nome do anime", example = "Resident Evil", required = false)
    private String name;
}
