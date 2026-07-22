package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.FacturacionRequestDTO;
import com.acoasmi.roble.dto.response.FacturacionResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.ControlCajas;
import com.acoasmi.roble.entity.Facturas;
import com.acoasmi.roble.repository.FacturacionRepository;
import com.acoasmi.roble.repository.ControlCajasRepository;
import com.acoasmi.roble.repository.AsociadosRepository;
import com.acoasmi.roble.service.FacturacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FacturacionServiceImpl extends AcoasmiServiceImpl<Facturas,
        FacturacionRequestDTO, FacturacionResponseDTO, Long>
        implements FacturacionService {

    private final FacturacionRepository facturacionRepository;
    private final ControlCajasRepository controlCajasRepository;
    private final AsociadosRepository asociadosRepository;

    public FacturacionServiceImpl(FacturacionRepository facturacionRepository,
                                  ControlCajasRepository controlCajasRepository,
                                  AsociadosRepository asociadosRepository) {
        super(facturacionRepository, Facturas.class);
        this.facturacionRepository = facturacionRepository;
        this.controlCajasRepository = controlCajasRepository;
        this.asociadosRepository = asociadosRepository;
    }

    @Override
    @Transactional
    public FacturacionResponseDTO crearFactura(FacturacionRequestDTO request) {
        return this.create(request);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturacionResponseDTO obtenerPorUuid(UUID uuid) {
        Facturas factura = facturacionRepository.findByCodigoGeneracionUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con el UUID especificado."));
        return mapToResponseDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturacionResponseDTO> getByCajaUsuario(String usuario, Pageable pageable) {
        return facturacionRepository.findByUsuarioCajero(usuario, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturacionResponseDTO> getByAsociadoNumeroAsociado(Integer numeroAsociado, Pageable pageable) {
        return facturacionRepository.findByNumeroAsociado(numeroAsociado, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    protected FacturacionResponseDTO mapToResponseDTO(Facturas factura) {
        if (factura == null) return null;

        FacturacionResponseDTO response = new FacturacionResponseDTO();

        BeanUtils.copyProperties(factura, response);
        response.setIdFactura(factura.getId());

        response.setUsuarioCajero(
                factura.getCaja() != null && factura.getCaja().getUsuarioCajero() != null
                        ? String.valueOf(factura.getCaja().getUsuarioCajero())
                        : null
        );

        if (factura.getAsociado() != null) {
            response.setNumeroAsociado(factura.getAsociado().getNumeroAsociado());

            String nombres = factura.getAsociado().getNombres() != null ? factura.getAsociado().getNombres() : "";
            String apellidos = factura.getAsociado().getApellidos() != null ? factura.getAsociado().getApellidos() : "";

            String nombreCompleto = (nombres + " " + apellidos).trim();
            response.setNombreCompletoAsociado(nombreCompleto.isEmpty() ? "SIN NOMBRE" : nombreCompleto);
        } else {
            response.setNumeroAsociado(null);
            response.setNombreCompletoAsociado("CONSUMIDOR FINAL");
        }

        return response;
    }

    @Override
    protected void mapearDtoAEntidad(FacturacionRequestDTO dto, Facturas factura) {
        if (dto == null) return;

        if (factura.getId() == null) {
            if (facturacionRepository.findByCodigoGeneracionUuid(dto.getCodigoGeneracionUuid()).isPresent()) {
                throw new IllegalArgumentException("El código de generación UUID ya se encuentra registrado.");
            }
            if (facturacionRepository.findByNumeroControl(dto.getNumeroControl()).isPresent()) {
                throw new IllegalArgumentException("El número de control ya se encuentra registrado.");
            }
        }

        ControlCajas caja = controlCajasRepository.findByUsuarioCajero_Usuario(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("No se encontró una sesión de caja para el usuario: " + dto.getUsuario()));

        Asociados asociado = null;
        if (dto.getNombreCompletoAsociado() != null && !dto.getNombreCompletoAsociado().trim().isEmpty()) {
            asociado = asociadosRepository
                    .findFirstByNombreCompletoAsociadoContainingIgnoreCase(dto.getNombreCompletoAsociado().trim())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró ningún asociado con el nombre: " + dto.getNombreCompletoAsociado()));
        }

        BeanUtils.copyProperties(dto, factura);
        factura.setCaja(caja);
        factura.setAsociado(asociado);
    }
}