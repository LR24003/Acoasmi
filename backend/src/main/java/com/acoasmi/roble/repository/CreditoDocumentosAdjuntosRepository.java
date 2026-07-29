package com.acoasmi.roble.repository;

import com.acoasmi.roble.entity.CreditoDocumentosAdjuntos;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CreditoDocumentosAdjuntosRepository extends AcoasmiRepository<
        CreditoDocumentosAdjuntos, Long> {


    List<CreditoDocumentosAdjuntos> findBySolicitudCredito_Id(Long idSolicitud);
}
