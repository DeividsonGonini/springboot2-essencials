package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

//Informar a classe e o tipo de atributo usado no ID
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("Anime");
    EntityManager em = emf.createEntityManager();

    //Criando busca por name (tem que ser o mesmo atributo da Entidade)
    List<Anime> findByName(String name);

    //Consulta por nome e categorias e pais
//    Query consulta1 = em.createQuery("SELECT a FROM anime a " +
//            " LEFT JOIN categoria c " +
//            " ON a.categoria = c.id " +
//            " LEFT JOIN paisOrigem p " +
//            " ON a.paisOrigem = p.id " +
//            " WHERE a.categoria IN :categoria ");
//    List<Anime> findByCategoriaAndPaisOrigem(@Param("categoria") List<Integer> categoria,
//                                    @Param("paisOrigem") Integer paisOrigem);


     Query("SELECT a FROM anime a "+
                          " LEFT JOIN categoria c "+
                          " ON a.categoria = c.id "+
                          " LEFT JOIN paisOrigem p "+
                          " ON a.paisOrigem = p.id "+
                          " WHERE a.categoria IN :categoria ")
     List<Anime> findByCategoriaAndPaisOrigem(@Param("categoria") List<Integer> categoria,
                                             @Param("paisOrigem") Integer paisOrigem);


//    SELECT a.name, c.nome_categoria, p.nome_pais
//    FROM anime a
//    LEFT JOIN categoria c
//    ON a.id_categoria = c.id
//    LEFT JOIN pais_origem p
//    ON a.id_pais_origem = p.id
//    WHERE c.id IN (1, 3)
//    AND p.id IN (NULL OR (SELECT COALESCE(NULL, NULL)))


}
