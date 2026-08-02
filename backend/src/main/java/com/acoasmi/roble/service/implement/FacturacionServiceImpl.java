package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.FacturaDetallesRequestDTO;
import com.acoasmi.roble.dto.request.FacturacionRequestDTO;
import com.acoasmi.roble.dto.response.FacturaDetallesResponseDTO;
import com.acoasmi.roble.dto.response.FacturacionResponseDTO;
import com.acoasmi.roble.entity.*;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.FacturacionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FacturacionServiceImpl extends AcoasmiServiceImpl<Facturas,
        FacturacionRequestDTO, FacturacionResponseDTO, Long>
        implements FacturacionService {

    private final FacturacionRepository facturacionRepository;
    private final ControlCajasRepository controlCajasRepository;
    private final AsociadosRepository asociadosRepository;
    private final CatalogoCuentasRepository catalogoCuentasRepository;

    public FacturacionServiceImpl(FacturacionRepository facturacionRepository,
                                  ControlCajasRepository controlCajasRepository,
                                  AsociadosRepository asociadosRepository,
                                  CatalogoCuentasRepository catalogoCuentasRepository) {
        super(facturacionRepository, Facturas.class);
        this.facturacionRepository = facturacionRepository;
        this.controlCajasRepository = controlCajasRepository;
        this.asociadosRepository = asociadosRepository;
        this.catalogoCuentasRepository = catalogoCuentasRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public FacturacionResponseDTO obtenerPorUuid(UUID uuid) {
        Facturas factura = facturacionRepository.findByCodigoGeneracionUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con el UUID especificado: " + uuid));
        return mapToResponseDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturacionResponseDTO obtenerPorIdConDetalles(Long id) {
        Facturas factura = facturacionRepository.findByIdConDetalles(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));
        return mapToResponseDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturacionResponseDTO> getByUsuarioEmisor(String usuario, Pageable pageable) {
        return facturacionRepository.findByUsuarioEmisor(usuario, pageable)
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

        if (factura.getEmpresa() != null) {
            response.setNombreEmpresa(factura.getEmpresa().getNombreFinanciera());
            response.setNitEmpresa(factura.getEmpresa().getNit());
            response.setNrcEmpresa(factura.getEmpresa().getNrc());
        }

        if (factura.getUsuario() != null) {
            response.setUsuarioCajero(factura.getUsuario().getUsuario());
        }
        if (factura.getCaja() != null) {
            response.setNumeroCaja(factura.getCaja().getNumeroCaja());
        }

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

        if (factura.getPartida() != null) {
            response.setIdPartidaContable(factura.getPartida().getId());
        }

        if (factura.getDetalles() != null && !factura.getDetalles().isEmpty()) {
            List<FacturaDetallesResponseDTO> detallesDto = factura.getDetalles().stream().map(d -> {
                FacturaDetallesResponseDTO detDto = new FacturaDetallesResponseDTO();
                BeanUtils.copyProperties(d, detDto);
                if (d.getCuentaContable() != null) {
                    detDto.setCodigoCuentaContable(d.getCuentaContable().getCodigoCuenta());
                    detDto.setNombreCuentaContable(d.getCuentaContable().getNombreCuenta());
                }
                return detDto;
            }).collect(Collectors.toList());
            response.setDetalles(detallesDto);
        }

        return response;
    }

    @Override
    protected void mapearDtoAEntidad(FacturacionRequestDTO dto, Facturas factura) {
        if (dto == null) return;

        ControlCajas caja = controlCajasRepository.findByUsuarioCajero_Usuario(dto.getUsuario())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una sesión de caja activa para el usuario: " + dto.getUsuario()));

        Asociados asociado = null;
        if (dto.getNumeroAsociado() != null) {
            asociado = asociadosRepository.findByNumeroAsociado(dto.getNumeroAsociado())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "No se encontró ningún asociado con el número: " + dto.getNumeroAsociado()
                    ));
        }

        BeanUtils.copyProperties(dto, factura, "detalles");
        factura.setCaja(caja);
        factura.setAsociado(asociado);
        factura.setUsuario(caja.getUsuarioCajero());

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            if (factura.getDetalles() == null) {
                factura.setDetalles(new ArrayList<>());
            } else {
                factura.getDetalles().clear();
            }

            for (FacturaDetallesRequestDTO detDto : dto.getDetalles()) {
                FacturaDetalles detalle = new FacturaDetalles();
                BeanUtils.copyProperties(detDto, detalle);

                if (detDto.getIdCuentaContable() != null) {
                    CatalogoCuentas cuenta = catalogoCuentasRepository.findById(detDto.getIdCuentaContable())
                            .orElseThrow(() -> new EntityNotFoundException("Cuenta contable no encontrada con ID: " + detDto.getIdCuentaContable()));
                    detalle.setCuentaContable(cuenta);
                }

                factura.addDetalle(detalle);
            }
        }

        if (factura.getId() == null) {
            if (dto.getCodigoGeneracionUuid() == null) {
                factura.setCodigoGeneracionUuid(generarCodigoGeneracionUuid());
            } else {
                if (facturacionRepository.existsByCodigoGeneracionUuid(dto.getCodigoGeneracionUuid())) {
                    throw new IllegalArgumentException("El código de generación UUID ya se encuentra registrado.");
                }
            }

            if (dto.getNumeroControl() == null || dto.getNumeroControl().isBlank()) {
                String numeroCaja = (caja.getNumeroCaja() != null) ? caja.getNumeroCaja() : "01";
                factura.setNumeroControl(generarNumeroControl(dto.getTipoDte(), numeroCaja));
            } else {
                if (facturacionRepository.existsByNumeroControl(dto.getNumeroControl())) {
                    throw new IllegalArgumentException("El número de control ya se encuentra registrado.");
                }
            }

            if (factura.getSelloRecepcionMh() == null || factura.getSelloRecepcionMh().isBlank()) {
                factura.setSelloRecepcionMh(generarSelloRecepcionMh());
                factura.setEstadoDte("PROCESADO");
            }

            if (factura.getMontoTotalLetras() == null || factura.getMontoTotalLetras().isBlank()) {
                factura.setMontoTotalLetras(String.format("%s 00/100 USD", factura.getMontoTotal().toString()));
            }
        }
    }


    private UUID generarCodigoGeneracionUuid() {
        return UUID.randomUUID();
    }

    private String generarNumeroControl(String tipoDte, String codigoCaja) {
        String tipo = (tipoDte != null && !tipoDte.isBlank()) ? tipoDte : "01";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return String.format("DTE-%s-C%s-%s", tipo, codigoCaja, timestamp);
    }

    private String generarSelloRecepcionMh() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String hexRaw = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return (year + hexRaw).substring(0, 40);
    }
}