package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.entity.AcoasmiEntity;
import com.acoasmi.roble.repository.AcoasmiRepository;
import com.acoasmi.roble.service.AcoasmiService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AcoasmiServiceImpl<E extends AcoasmiEntity, REQ, RES, ID> implements AcoasmiService<E, REQ, RES, ID> {

    protected final AcoasmiRepository<E, ID> repository;
    private final Class<E> entityClass;

    protected AcoasmiServiceImpl(AcoasmiRepository<E, ID> repository, Class<E> entityClass) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    protected abstract RES mapToResponseDTO(E entity);
    protected abstract void mapearDtoAEntidad(REQ requestDto, E entity);

    @Override
    @Transactional(readOnly = true)
    public List<RES> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + id));
        return mapToResponseDTO(entity);
    }

    @Override
    @Transactional
    public RES create(REQ requestDto) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            mapearDtoAEntidad(requestDto, entity);
            E savedEntity = repository.save(entity);
            return mapToResponseDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error al instanciar automáticamente la entidad: " + entityClass.getSimpleName(), e);
        }
    }

    @Override
    @Transactional
    public RES update(ID id, REQ requestDto) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, ID inexistente: " + id));
        mapearDtoAEntidad(requestDto, entity);
        E updatedEntity = repository.save(entity);
        return mapToResponseDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro con ese ID"));

        entity.setEstado(false);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void cambiarEstado(ID id, Boolean estado) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con el ID: " + id));

        entity.setEstado(estado);
        repository.save(entity);
    }
}