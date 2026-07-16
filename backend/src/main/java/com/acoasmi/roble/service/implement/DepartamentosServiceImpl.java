package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.DepartamentosRequestDTO;
import com.acoasmi.roble.dto.response.DepartamentosResponseDTO;
import com.acoasmi.roble.entity.Departamentos;
import com.acoasmi.roble.repository.DepartamentosRepository;
import com.acoasmi.roble.service.DepartamentosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartamentosServiceImpl extends AcoasmiServiceImpl<Departamentos,
        DepartamentosRequestDTO, DepartamentosResponseDTO, Long>
        implements DepartamentosService {

    private final DepartamentosRepository departamentosRepository;

    public DepartamentosServiceImpl(DepartamentosRepository departamentosRepository) {
        super(departamentosRepository, Departamentos.class);
        this.departamentosRepository = departamentosRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public DepartamentosResponseDTO getByCodigoDepartamento(String codigoDepartamento) {
        return departamentosRepository.findByCodigoDepartamento(codigoDepartamento)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró el departamento con el código: " + codigoDepartamento));
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentosResponseDTO getByNombre(String nombre) {
        return departamentosRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(nombre)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró un departamento activo con el nombre: " + nombre));
    }


    @Override
    protected void mapearDtoAEntidad(DepartamentosRequestDTO dto, Departamentos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoDepartamento(dto.getCodigoDepartamento());
        entity.setNombre(dto.getNombre());

        if (entity.getId() == null) {
            entity.setEstado(true);
        }
    }

    @Override
    protected DepartamentosResponseDTO mapToResponseDTO(Departamentos entity) {
        if (entity == null) return null;

        return DepartamentosResponseDTO.builder()
                .id(entity.getId())
                .codigoDepartamento(entity.getCodigoDepartamento())
                .nombre(entity.getNombre())
                .estado(entity.getEstado())
                .build();
    }
}
