package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.entity.AcoasmiEntity;
import com.acoasmi.roble.repository.AcoasmiRepository;
import com.acoasmi.roble.service.AcoasmiService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AcoasmiServiceImpl<E extends AcoasmiEntity, REQ, RES, ID> implements AcoasmiService<E, REQ, RES, ID> {

    protected final AcoasmiRepository<E, ID> repository;

    protected AcoasmiServiceImpl(AcoasmiRepository<E, ID> repository) {
        this.repository = repository;
    }

    protected abstract RES convertToResponseDto(E entity);
    protected abstract E convertToEntity(REQ requestDto);
    protected abstract void updateEntityFromDto(REQ requestDto, E entity);

    @Override
    @Transactional(readOnly = true)
    public List<RES> getAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RES getById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + id));
        return convertToResponseDto(entity);
    }

    @Override
    @Transactional
    public RES create(REQ requestDto) {
        E entity = convertToEntity(requestDto);
        E savedEntity = repository.save(entity);
        return convertToResponseDto(savedEntity);
    }

    @Override
    @Transactional
    public RES update(ID id, REQ requestDto) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar, ID inexistente: " + id));
        updateEntityFromDto(requestDto, entity);
        E updatedEntity = repository.save(entity);
        return convertToResponseDto(updatedEntity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el registro con ese ID"));

        entity.setEstado(false);
        repository.delete(entity);
    }

    @Transactional
    public void cambiarEstado(ID id, Boolean estado) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con el ID: " + id));

        entity.setEstado(estado);
        repository.save(entity);
    }
}
