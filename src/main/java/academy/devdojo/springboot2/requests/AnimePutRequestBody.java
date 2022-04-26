package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestBody {
    @Schema(description = "Este é o id do anime que deseja atualizar", example = "1", required = true)
    private Long id;
    @Schema(description = "Este é o novo nome do anime", example = "Resident Evil", required = true)
    private String name;
}
