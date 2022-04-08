package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//Informar a classe e o tipo de atributo usado no ID
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    //Criando busca por name (tem que ser o mesmo atributo da Entidade)
    List<Anime> findByName(String name);

    //Consulta por nome e categorias e pais
    @Query(value = "SELECT a FROM anime a " +
            " LEFT JOIN categoria c " +
            " ON a.idCategoria = c.id " +
            " LEFT JOIN paisOrigem p " +
            " ON a.idPaisOrigem = p.id " +
            " WHERE c.id IN :idCategoria ");
    List<Anime> findByNameCategoriaPais(@Param(idCategoria) List<Integer> idCategoria,
                                        @Param(idPaisOrigem) Integer idPaisOrigem);


//    SELECT a.name, c.nome_categoria, p.nome_pais
//    FROM anime a
//    LEFT JOIN categoria c
//    ON a.id_categoria = c.id
//    LEFT JOIN pais_origem p
//    ON a.id_pais_origem = p.id
//    WHERE c.id IN (1, 3)
//    AND p.id IN (NULL OR (SELECT COALESCE(NULL, NULL)))


}
