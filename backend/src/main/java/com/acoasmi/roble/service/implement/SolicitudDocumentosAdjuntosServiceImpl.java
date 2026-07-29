package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CreditoDocumentosAdjuntosRequestDTO;
import com.acoasmi.roble.dto.response.CreditoDocumentosAdjuntosResponseDTO;
import com.acoasmi.roble.entity.CreditoDocumentosAdjuntos;
import com.acoasmi.roble.entity.SolicitudesCredito;
import com.acoasmi.roble.repository.CreditoDocumentosAdjuntosRepository;
import com.acoasmi.roble.repository.SolicitudesCreditoRepository;
import com.acoasmi.roble.service.SolicitudDocumentosAdjuntosService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

@Service
public class SolicitudDocumentosAdjuntosServiceImpl
        extends AcoasmiServiceImpl<CreditoDocumentosAdjuntos, CreditoDocumentosAdjuntosRequestDTO,
        CreditoDocumentosAdjuntosResponseDTO, Long>
        implements SolicitudDocumentosAdjuntosService {

    private final CreditoDocumentosAdjuntosRepository documentoRepository;
    private final SolicitudesCreditoRepository solicitudesCreditoRepository;

    public SolicitudDocumentosAdjuntosServiceImpl(
            CreditoDocumentosAdjuntosRepository documentoRepository,
            SolicitudesCreditoRepository solicitudesCreditoRepository) {
        super(documentoRepository, CreditoDocumentosAdjuntos.class);
        this.documentoRepository = documentoRepository;
        this.solicitudesCreditoRepository = solicitudesCreditoRepository;
    }

    @Override
    @Transactional
    public CreditoDocumentosAdjuntosResponseDTO adjuntarDocumento(Long idSolicitud,
                                                                  CreditoDocumentosAdjuntosRequestDTO requestDto,
                                                                  MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        SolicitudesCredito solicitud = solicitudesCreditoRepository.findByIdWithDocumentos(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud de crédito no encontrada con ID: " + idSolicitud));

        String nombreOriginal = archivo.getOriginalFilename();
        String nombreSeguro = "Solicitud_" + idSolicitud + "_" + System.currentTimeMillis() + "_" + nombreOriginal;

        CreditoDocumentosAdjuntos nuevoDocumento = getCreditoDocumentosAdjuntos(requestDto, nombreSeguro, solicitud);

        String tipoSanitizado = HtmlUtils.htmlEscape(requestDto.getTipoDocumento().trim());
        nuevoDocumento.setTipoDocumento(tipoSanitizado);

        CreditoDocumentosAdjuntos documentoGuardado = documentoRepository.save(nuevoDocumento);

        return mapToResponseDTO(documentoGuardado);
    }

    private static @NonNull CreditoDocumentosAdjuntos getCreditoDocumentosAdjuntos(
            CreditoDocumentosAdjuntosRequestDTO requestDto, String nombreSeguro, SolicitudesCredito solicitud) {

        String urlArchivoSimulada = "http://acoasmi/SolicitudesCreditoDocumentos/" + nombreSeguro;

        CreditoDocumentosAdjuntos nuevoDocumento = new CreditoDocumentosAdjuntos();
        nuevoDocumento.setSolicitudCredito(solicitud);
        nuevoDocumento.setTipoDocumento(requestDto.getTipoDocumento().toUpperCase().trim());
        nuevoDocumento.setRutaArchivoStorage(urlArchivoSimulada);
        nuevoDocumento.setEstado(true);
        return nuevoDocumento;
    }

    @Override
    protected void mapearDtoAEntidad(CreditoDocumentosAdjuntosRequestDTO request, CreditoDocumentosAdjuntos entidad) {
        entidad.setTipoDocumento(request.getTipoDocumento().toUpperCase().trim());
    }

    @Override
    protected CreditoDocumentosAdjuntosResponseDTO mapToResponseDTO(CreditoDocumentosAdjuntos documento) {
        if (documento == null) return null;

        String numeroSolicitud = (documento.getSolicitudCredito() != null)
                ? documento.getSolicitudCredito().getNumeroSolicitud()
                : null;

        return new CreditoDocumentosAdjuntosResponseDTO(
                documento.getId(),
                numeroSolicitud,
                documento.getTipoDocumento(),
                documento.getRutaArchivoStorage(),
                documento.getFechaSubida(),
                documento.getEstado()
        );
    }
}