package academy.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor //cria construtor vazio
@Entity //transforma numa entidade para o spring
@Builder
@Table(name = "anime")
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//gera valor automatico
    private Long id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_pais_origem")
    private PaisOrigem paisOrigem;

    @NotEmpty(message = "O nome do Anime não pode ser vazio nem nulo")
    public Anime(String name) {
        this.name = name;
    }



}
