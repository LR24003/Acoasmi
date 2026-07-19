package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CumplimientoPerfilRiesgoRequestDTO;
import com.acoasmi.roble.dto.response.CumplimientoPerfilRiesgoResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.CumplimientoPerfilRiesgo;
import com.acoasmi.roble.repository.CumplimientoPerfilRiesgoRepository;
import com.acoasmi.roble.service.CumplimientoPerfilRiesgoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CumplimientoPerfilRiesgoServiceImpl
        extends AcoasmiServiceImpl<CumplimientoPerfilRiesgo, CumplimientoPerfilRiesgoRequestDTO,
        CumplimientoPerfilRiesgoResponseDTO, Long>
        implements CumplimientoPerfilRiesgoService {

    private final CumplimientoPerfilRiesgoRepository perfilRiesgoRepository;


    public CumplimientoPerfilRiesgoServiceImpl(CumplimientoPerfilRiesgoRepository perfilRiesgoRepository
                                               ) {
        super(perfilRiesgoRepository, CumplimientoPerfilRiesgo.class);
        this.perfilRiesgoRepository = perfilRiesgoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CumplimientoPerfilRiesgoResponseDTO> getByNivelRiesgo(String nivelRiesgo) {
        return perfilRiesgoRepository.findByNivelRiesgoContainingIgnoreCase(nivelRiesgo)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void generarPerfilRiesgoInicial(Asociados asociado, com.acoasmi.roble.dto.request.AsociadosRequestDTO dto) {
        CumplimientoPerfilRiesgo perfil = new CumplimientoPerfilRiesgo();
        perfil.setAsociado(asociado);

        perfil.setNivelRiesgo(dto.getEsPep() != null && dto.getEsPep() ? "ALTO" : "BAJO");

        perfil.setEsPep(dto.getEsPep() != null ? dto.getEsPep() : false);
        perfil.setCargoOrigenPep(dto.getCargoOrigenPep());
        perfil.setMontoMaximoMensualEsperado(dto.getMontoMaximoMensualEsperado());
        perfil.setOrigenFondosDeclarado(dto.getOrigenFondosDeclarado());
        perfil.setEstado(true);

        perfilRiesgoRepository.save(perfil);
    }

    @Override
    protected void mapearDtoAEntidad(CumplimientoPerfilRiesgoRequestDTO request, CumplimientoPerfilRiesgo entity) {
        if (request.getNivelRiesgo() != null) {
            entity.setNivelRiesgo(request.getNivelRiesgo());
        }

        if (request.getEsPep() != null) {
            entity.setEsPep(request.getEsPep());
        }

        if (request.getCargoOrigenPep() != null) {
            entity.setCargoOrigenPep(request.getCargoOrigenPep());
        }

        if (request.getMontoMaximoMensualEsperado() != null) {
            entity.setMontoMaximoMensualEsperado(request.getMontoMaximoMensualEsperado());
        }

        if (request.getOrigenFondosDeclarado() != null) {
            entity.setOrigenFondosDeclarado(request.getOrigenFondosDeclarado());
        }
    }

    @Override
    protected CumplimientoPerfilRiesgoResponseDTO mapToResponseDTO(CumplimientoPerfilRiesgo entity) {
        if (entity == null) return null;

        String nombreAsociado = (entity.getAsociado() != null)
                ? entity.getAsociado().getNombres() + " " + entity.getAsociado().getApellidos()
                : "No disponible";

        Integer numeroAsociado = (entity.getAsociado() != null)
                ? entity.getAsociado().getNumeroAsociado()
                : 0;

        String cargoPep = (entity.getCargoOrigenPep() != null) ? entity.getCargoOrigenPep() : "";
        Boolean esPepSeguro = (entity.getEsPep() != null) ? entity.getEsPep() : false;

        return new CumplimientoPerfilRiesgoResponseDTO(
                entity.getId(),
                entity.getAsociado() != null ? entity.getAsociado().getId() : null,
                numeroAsociado,
                nombreAsociado,
                entity.getNivelRiesgo(),
                esPepSeguro,
                cargoPep,
                entity.getMontoMaximoMensualEsperado(),
                entity.getOrigenFondosDeclarado(),
                entity.getFechaUltimaActualizacion(),
                entity.getEstado()
        );
    }
}