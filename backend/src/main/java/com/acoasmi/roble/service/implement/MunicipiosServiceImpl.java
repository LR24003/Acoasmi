package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.MunicipiosRequestDTO;
import com.acoasmi.roble.dto.response.MunicipiosResponseDTO;
import com.acoasmi.roble.entity.Municipios;
import com.acoasmi.roble.entity.Departamentos;
import com.acoasmi.roble.repository.MunicipiosRepository;
import com.acoasmi.roble.repository.DepartamentosRepository;
import com.acoasmi.roble.service.MunicipiosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MunicipiosServiceImpl extends AcoasmiServiceImpl<Municipios,
        MunicipiosRequestDTO, MunicipiosResponseDTO, Long>
        implements MunicipiosService {

    private final MunicipiosRepository municipiosRepository;
    private final DepartamentosRepository departamentosRepository;

    public MunicipiosServiceImpl(MunicipiosRepository municipiosRepository,
                                 DepartamentosRepository departamentosRepository) {
        super(municipiosRepository, Municipios.class);
        this.municipiosRepository = municipiosRepository;
        this.departamentosRepository = departamentosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public MunicipiosResponseDTO getByCodigoMunicipio(Integer codigoMunicipio) {
        return municipiosRepository.findByCodigoMunicipio(codigoMunicipio)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró el municipio con el código: " + codigoMunicipio));
    }

    @Override
    @Transactional(readOnly = true)
    public MunicipiosResponseDTO getByNombreMunicipio(String nombreMunicipio) {
        return municipiosRepository.findByNombreMunicipioContainingIgnoreCaseAndEstadoTrue(nombreMunicipio)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró un municipio activo con el nombre: " + nombreMunicipio));
    }

    @Override
    protected void mapearDtoAEntidad(MunicipiosRequestDTO dto, Municipios entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoMunicipio(dto.getCodigoMunicipio());
        entity.setNombreMunicipio(dto.getNombreMunicipio());

        if (dto.getNombreDepartamento() != null && !dto.getNombreDepartamento().isBlank()) {
            Departamentos departamento = departamentosRepository
                    .findByNombreDepartamentoContainingIgnoreCaseAndEstadoTrue(dto.getNombreDepartamento().trim())
                    .orElseThrow(() -> new RuntimeException("El departamento '" + dto.getNombreDepartamento() + "' no existe o está inactivo"));

            entity.setDepartamento(departamento);
        }

        if (entity.getId() == null) {
            entity.setEstado(true);
        }
    }

    @Override
    protected MunicipiosResponseDTO mapToResponseDTO(Municipios entity) {
        if (entity == null) return null;

        MunicipiosResponseDTO.MunicipiosResponseDTOBuilder builder = MunicipiosResponseDTO.builder()
                .id(entity.getId())
                .codigoMunicipio(entity.getCodigoMunicipio())
                .nombreMunicipio(entity.getNombreMunicipio())
                .estado(entity.getEstado());

        if (entity.getDepartamento() != null) {
            Departamentos dept = entity.getDepartamento();
            builder.nombreDepartamento(dept.getNombreDepartamento());

        }

        return builder.build();
    }
}