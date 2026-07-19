package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadoDocumentosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoDocumentosResponseDTO;
import com.acoasmi.roble.entity.AsociadoDocumentos;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.repository.AsociadoDocumentosRepository;
import com.acoasmi.roble.repository.AsociadosRepository;
import com.acoasmi.roble.service.AsociadoDocumentosService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsociadoDocumentosServiceImpl extends AcoasmiServiceImpl<AsociadoDocumentos,
        AsociadoDocumentosRequestDTO, AsociadoDocumentosResponseDTO, Long>
        implements AsociadoDocumentosService {

    private final AsociadoDocumentosRepository documentoRepository;
    private final AsociadosRepository asociadosRepository;

    public AsociadoDocumentosServiceImpl(AsociadoDocumentosRepository documentoRepository,
                                         AsociadosRepository asociadosRepository) {
        super(documentoRepository, AsociadoDocumentos.class);
        this.documentoRepository = documentoRepository;
        this.asociadosRepository = asociadosRepository;
    }

    @Override
    @Transactional
    public AsociadoDocumentosResponseDTO subirDocumento(Integer numeroAsociado, AsociadoDocumentosRequestDTO requestDto, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        Asociados asociado = asociadosRepository.findByNumeroAsociadoAndEstadoTrue(numeroAsociado)
                .orElseThrow(() -> new RuntimeException("No se encontró un asociado activo con el número: " + numeroAsociado));

        String nombreOriginal = archivo.getOriginalFilename();
        String nombreSeguro = "Doc_" + numeroAsociado + "_" + System.currentTimeMillis() + "_" + nombreOriginal;
        AsociadoDocumentos nuevoDocumento = getAsociadoDocumentos(requestDto, nombreSeguro, asociado);

        String tipoSanitizado = HtmlUtils.htmlEscape(requestDto.getTipoDocumento().trim());
        nuevoDocumento.setTipoDocumento(tipoSanitizado);

        AsociadoDocumentos documentoGuardado = documentoRepository.save(nuevoDocumento);

        return mapToResponseDTO(documentoGuardado);
    }

    private static @NonNull AsociadoDocumentos getAsociadoDocumentos(AsociadoDocumentosRequestDTO requestDto, String nombreSeguro, Asociados asociado) {
        String urlArchivoSimulada = "http://acoasmi/AsociadoDocumentos/" + nombreSeguro;

        AsociadoDocumentos nuevoDocumento = new AsociadoDocumentos();
        nuevoDocumento.setAsociado(asociado);
        nuevoDocumento.setTipoDocumento(requestDto.getTipoDocumento().toUpperCase().trim());
        nuevoDocumento.setNombreArchivo(nombreSeguro);
        nuevoDocumento.setUrlArchivo(urlArchivoSimulada);
        nuevoDocumento.setEstado(true);
        return nuevoDocumento;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadoDocumentosResponseDTO> getByNumeroAsociado(Integer numeroAsociado) {
        List<AsociadoDocumentos> documentos = documentoRepository.findByAsociadoNumeroAsociadoAndEstadoTrue(numeroAsociado);

        return documentos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    protected void mapearDtoAEntidad(AsociadoDocumentosRequestDTO request, AsociadoDocumentos entidad) {
        entidad.setTipoDocumento(request.getTipoDocumento().toUpperCase().trim());
    }

    @Override
    protected AsociadoDocumentosResponseDTO mapToResponseDTO(AsociadoDocumentos documento) {
        if (documento == null) return null;

        String nombreCompleto = "";
        Integer numeroAsociado = null;

        if (documento.getAsociado() != null) {
            nombreCompleto = documento.getAsociado().getNombres() + " " + documento.getAsociado().getApellidos();
            numeroAsociado = documento.getAsociado().getNumeroAsociado();
        }

        return new AsociadoDocumentosResponseDTO(
                documento.getId(),
                numeroAsociado,
                nombreCompleto,
                documento.getTipoDocumento(),
                documento.getNombreArchivo(),
                documento.getUrlArchivo(),
                documento.getFechaSubida(),
                documento.getEstado()
        );
    }
}