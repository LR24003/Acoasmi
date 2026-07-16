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
    public DistritosResponseDTO getByCodigoDistrito(String codigoDistrito) {
        Distritos distrito = distritosRepository.findByCodigoDistrito(codigoDistrito)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con código: " + codigoDistrito));
        return mapToResponseDTO(distrito);
    }

    @Override
    @Transactional(readOnly = true)
    public DistritosResponseDTO getByNombre(String nombre) {
        Distritos distrito = distritosRepository.findByNombreContainingIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado con el nombre: " + nombre));
        return mapToResponseDTO(distrito);
    }

    @Override
    protected DistritosResponseDTO mapToResponseDTO(Distritos entity) {
        if (entity == null) return null;

        DistritosResponseDTO.DistritosResponseDTOBuilder builder = DistritosResponseDTO.builder()
                .id(entity.getId())
                .codigoDistrito(entity.getCodigoDistrito())
                .nombre(entity.getNombre())
                .estado(entity.getEstado());

        if (entity.getDepartamento() != null) {
            Departamentos dept = entity.getDepartamento();
            builder.nombreDepartamento(dept.getNombre());
        }

        if (entity.getMunicipio() != null) {
            Municipios muni = entity.getMunicipio();
            builder.nombreMunicipio(muni.getNombre());
        }

        return builder.build();
    }

    @Override
    protected void mapearDtoAEntidad(DistritosRequestDTO dto, Distritos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoDistrito(dto.getCodigoDistrito());
        entity.setNombre(dto.getNombre());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }

        Departamentos departamento = departamentosRepository
                .findByNombreContainingIgnoreCaseAndEstadoTrue(dto.getNombreDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con nombre: " + dto.getNombreDepartamento()));
        entity.setDepartamento(departamento);

        Municipios municipio = municipiosRepository
                .findByNombreContainingIgnoreCaseAndEstadoTrue(dto.getNombreMunicipio())
                .orElseThrow(() -> new RuntimeException("Municipio no encontrado con nombre: " + dto.getNombreMunicipio()));
        entity.setMunicipio(municipio);
    }

}