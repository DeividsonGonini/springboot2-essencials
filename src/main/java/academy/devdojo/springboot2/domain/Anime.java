package academy.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor //cria construtor vazio
@Entity //transforma numa entidade para o spring
@Builder
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//gera valor automatico
    private Long id;
    private String name;


    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    private String idCategoria;

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    private String idPaisOrigem;

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    public Anime(String name) {
        this.name = name;
    }



}
