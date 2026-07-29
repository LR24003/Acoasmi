package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.TasasCreditos;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TasaCreditosRepository extends AcoasmiRepository<TasasCreditos, Long> {

    List<TasasCreditos> findByNombreProductoIgnoreCaseAndEstadoTrue(String nombreProducto);


    @Query("SELECT DISTINCT t FROM TasasCreditos t JOIN t.frecuenciasPago f " +
            "WHERE LOWER(t.nombreProducto) LIKE LOWER(CONCAT('%', :nombreProducto, '%')) " +
            "AND LOWER(f) = LOWER(:frecuenciaPago) AND t.estado = true")
    List<TasasCreditos> findByNombreProductoYFrecuencia(
            @Param("nombreProducto") String nombreProducto,
            @Param("frecuenciaPago") Collection<String> frecuenciaPago);


    Optional<TasasCreditos> findFirstByTasaInteresAnualAndEstadoTrue(BigDecimal tasaReferencia);
}