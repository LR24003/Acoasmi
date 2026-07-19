package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.AsociadoDocumentos;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsociadoDocumentosRepository extends AcoasmiRepository<AsociadoDocumentos, Long> {

    List<AsociadoDocumentos> findByAsociadoNumeroAsociadoAndEstadoTrue(Integer numeroAsociado);
}
