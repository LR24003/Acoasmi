package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.Asociados;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsociadosRepository extends AcoasmiRepository<Asociados, Long> {

    Optional<Asociados> findByNumeroAsociadoAndEstadoTrue(Integer numeroAsociado);
    Optional<Asociados> findByNumeroDocumento(String numeroDocumento);
    Optional<Asociados> findByNit(String nit);
    Optional<Asociados> findByEmailIgnoreCase(String email);


    Page<Asociados> findByEstado(Boolean estado, Pageable pageable);


    @Query("SELECT a FROM Asociados a WHERE " +
            "LOWER(a.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(a.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "a.numeroDocumento LIKE CONCAT('%', :filtro, '%')")
    Page<Asociados> buscarPorNombresApellidosODocumento(@Param("filtro") String filtro, Pageable pageable);

    @Query("SELECT a FROM Asociados a WHERE " +
            "LOWER(function('unaccent', CONCAT(a.nombres, ' ', a.apellidos))) " +
            "LIKE LOWER(function('unaccent', CONCAT('%', :nombreCompleto, '%'))) " +
            "AND a.estado = true")
    Optional<Asociados> findFirstByNombreCompletoAsociadoContainingIgnoreCase(@Param("nombreCompleto") String nombreCompleto);

    Optional<Asociados> findByNumeroAsociado(Integer numeroAsociado);
}
