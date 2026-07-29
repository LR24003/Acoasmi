package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CreditoGarantiasRequestDTO;
import com.acoasmi.roble.dto.response.CreditoGarantiasResponseDTO;
import com.acoasmi.roble.entity.CreditoGarantias;
import com.acoasmi.roble.repository.CreditoGarantiasRepository;
import com.acoasmi.roble.service.CreditoGarantiaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditoGarantiaServiceImpl extends AcoasmiServiceImpl<CreditoGarantias,
        CreditoGarantiasRequestDTO, CreditoGarantiasResponseDTO, Long>
        implements CreditoGarantiaService {

    private final CreditoGarantiasRepository creditoGarantiasRepository;

    public CreditoGarantiaServiceImpl(CreditoGarantiasRepository creditoGarantiasRepository) {
        super(creditoGarantiasRepository, CreditoGarantias.class);
        this.creditoGarantiasRepository = creditoGarantiasRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreditoGarantiasResponseDTO> obtenerPorTipoGarantia(String tipoGarantia) {
        return creditoGarantiasRepository.findByTipoGarantiaContainingIgnoreCase(tipoGarantia)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    protected void mapearDtoAEntidad(CreditoGarantiasRequestDTO request, CreditoGarantias garantia) {
        garantia.setTipoGarantia(request.getTipoGarantia());
        garantia.setValorEstimado(request.getValorEstimado());
        garantia.setDireccionGarantia(request.getDireccionGarantia());
        garantia.setDescripcion(request.getDescripcion());

        garantia.setNombreFiador(request.getNombreFiador());
        garantia.setIdentificacionFiador(request.getIdentificacionFiador());
        garantia.setTelefonoFiador(request.getTelefonoFiador());
        garantia.setIngresosFiador(request.getIngresosFiador());
    }


    @Override
    protected CreditoGarantiasResponseDTO mapToResponseDTO(CreditoGarantias garantia) {
        if (garantia == null) {
            return null;
        }

        return CreditoGarantiasResponseDTO.builder()
                .idGarantia(garantia.getId())
                .tipoGarantia(garantia.getTipoGarantia())
                .valorEstimado(garantia.getValorEstimado())
                .direccionGarantia(garantia.getDireccionGarantia())
                .descripcion(garantia.getDescripcion())
                .nombreFiador(garantia.getNombreFiador())
                .identificacionFiador(garantia.getIdentificacionFiador())
                .telefonoFiador(garantia.getTelefonoFiador())
                .ingresosFiador(garantia.getIngresosFiador())
                .estado(garantia.getEstado())
                .build();
    }
}
