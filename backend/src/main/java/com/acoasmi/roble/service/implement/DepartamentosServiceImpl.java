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
    public DepartamentosResponseDTO getByCodigoDepartamento(Integer codigoDepartamento) {
        return departamentosRepository.findByCodigoDepartamento(codigoDepartamento)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró el departamento con el código: " + codigoDepartamento));
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentosResponseDTO getByNombreDepartamento(String nombreDepartamento) {
        return departamentosRepository.findByNombreDepartamentoContainingIgnoreCaseAndEstadoTrue(nombreDepartamento)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró un departamento activo con el nombre: " + nombreDepartamento));
    }


    @Override
    protected void mapearDtoAEntidad(DepartamentosRequestDTO dto, Departamentos entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoDepartamento(dto.getCodigoDepartamento());
        entity.setNombreDepartamento(dto.getNombreDepartamento());

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
                .nombreDepartamento(entity.getNombreDepartamento())
                .estado(entity.getEstado())
                .build();
    }
}
