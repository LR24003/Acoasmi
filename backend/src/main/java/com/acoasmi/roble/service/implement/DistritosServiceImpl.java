package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.DistritosRequestDTO;
import com.acoasmi.roble.dto.response.DistritosResponseDTO;
import com.acoasmi.roble.entity.Departamentos;
import com.acoasmi.roble.entity.Distritos;
import com.acoasmi.roble.entity.Municipios;
import com.acoasmi.roble.repository.DepartamentosRepository;
import com.acoasmi.roble.repository.DistritosRepository;
import com.acoasmi.roble.repository.MunicipiosRepository;
import com.acoasmi.roble.service.DistritosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistritosServiceImpl extends AcoasmiServiceImpl<Distritos,
        DistritosRequestDTO, DistritosResponseDTO, Long> implements DistritosService {

    private final DistritosRepository distritosRepository;
    private final DepartamentosRepository departamentosRepository;
    private final MunicipiosRepository municipiosRepository;

    public DistritosServiceImpl(
            DistritosRepository distritosRepository,
            DepartamentosRepository departamentosRepository,
            MunicipiosRepository municipiosRepository) {
        super(distritosRepository, Distritos.class);
        this.distritosRepository = distritosRepository;
        this.departamentosRepository = departamentosRepository;
        this.municipiosRepository = municipiosRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public DistritosResponseDTO getByCodigoDistrito(Integer codigoDistrito) {
        Distritos distrito = distritosRepository.findByCodigoDistrito(codigoDistrito)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con código: " + codigoDistrito));
        return mapToResponseDTO(distrito);
    }

    @Override
    @Transactional(readOnly = true)
    public DistritosResponseDTO getByNombreDistrito(String nombreDistrito) {
        Distritos distrito = distritosRepository.findByNombreDistritoContainingIgnoreCase(nombreDistrito)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con el nombre: " + nombreDistrito));
        return mapToResponseDTO(distrito);
    }

    @Override
    protected DistritosResponseDTO mapToResponseDTO(Distritos entity) {
        if (entity == null) return null;

        DistritosResponseDTO.DistritosResponseDTOBuilder builder = DistritosResponseDTO.builder()
                .id(entity.getId())
                .codigoDistrito(entity.getCodigoDistrito())
                .nombreDistrito(entity.getNombreDistrito())
                .estado(entity.getEstado());

        if (entity.getDepartamento() != null) {
            Departamentos dept = entity.getDepartamento();
            builder.nombreDepartamento(dept.getNombreDepartamento());
        }

        if (entity.getMunicipio() != null) {
            Municipios municipio = entity.getMunicipio();
            builder.nombreMunicipio(municipio.getNombreMunicipio());
        }

        return builder.build();
    }

    @Override
    protected void mapearDtoAEntidad(DistritosRequestDTO dto, Distritos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoDistrito(dto.getCodigoDistrito());
        entity.setNombreDistrito(dto.getNombreDistrito());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }

        Departamentos departamento = departamentosRepository
                .findByNombreDepartamentoContainingIgnoreCaseAndEstadoTrue(dto.getNombreDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con nombre: " + dto.getNombreDepartamento()));
        entity.setDepartamento(departamento);

        Municipios municipio = municipiosRepository
                .findByNombreMunicipioContainingIgnoreCaseAndEstadoTrue(dto.getNombreMunicipio())
                .orElseThrow(() -> new RuntimeException("Municipio no encontrado con nombre: " + dto.getNombreMunicipio()));
        entity.setMunicipio(municipio);
    }

}