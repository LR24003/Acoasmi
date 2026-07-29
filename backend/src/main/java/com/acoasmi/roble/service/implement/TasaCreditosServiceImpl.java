package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.TasaCreditosRequestDTO;
import com.acoasmi.roble.dto.response.TasaCreditosResponseDTO;
import com.acoasmi.roble.entity.TasasCreditos;
import com.acoasmi.roble.repository.TasaCreditosRepository;
import com.acoasmi.roble.service.TasaCreditosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class TasaCreditosServiceImpl
        extends AcoasmiServiceImpl<TasasCreditos, TasaCreditosRequestDTO, TasaCreditosResponseDTO, Long>
        implements TasaCreditosService {

    private final TasaCreditosRepository tasaCreditosRepository;

    public TasaCreditosServiceImpl(TasaCreditosRepository tasaCreditosRepository) {
        super(tasaCreditosRepository, TasasCreditos.class);
        this.tasaCreditosRepository = tasaCreditosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TasaCreditosResponseDTO> buscarPorNombreProducto(String nombreProducto) {
        return tasaCreditosRepository.findByNombreProductoIgnoreCaseAndEstadoTrue(nombreProducto)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TasaCreditosResponseDTO> buscarPorNombreYFrecuencia(String nombreProducto, Collection<String> frecuenciasPago) {
        return tasaCreditosRepository.findByNombreProductoYFrecuencia(
                        nombreProducto, frecuenciasPago)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    protected void mapearDtoAEntidad(TasaCreditosRequestDTO request, TasasCreditos tasaCredito) {
        if (request == null) return;

        tasaCredito.setNombreProducto(request.getNombreProducto());
        tasaCredito.setTasaInteresAnual(request.getTasaInteresAnual());

        if (tasaCredito.getFrecuenciasPago() == null) {
            tasaCredito.setFrecuenciasPago(new HashSet<>());
        } else {
            tasaCredito.getFrecuenciasPago().clear();
        }

        if (request.getFrecuenciasPago() != null) {
            tasaCredito.getFrecuenciasPago().addAll(request.getFrecuenciasPago());
        }
    }

    @Override
    protected TasaCreditosResponseDTO mapToResponseDTO(TasasCreditos tasaCredito) {
        if (tasaCredito == null) {
            return null;
        }

        return TasaCreditosResponseDTO.builder()
                .id(tasaCredito.getId())
                .nombreProducto(tasaCredito.getNombreProducto())
                .tasaInteresAnual(tasaCredito.getTasaInteresAnual())
                .frecuenciasPago(tasaCredito.getFrecuenciasPago() != null
                        ? new HashSet<>(tasaCredito.getFrecuenciasPago())
                        : new HashSet<>())
                .estado(tasaCredito.getEstado())
                .build();
    }
}