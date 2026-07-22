package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CajaAperturaRequestDTO;
import com.acoasmi.roble.dto.request.CajaCierreRequestDTO;
import com.acoasmi.roble.dto.response.ControlCajasResponseDTO;
import com.acoasmi.roble.entity.ControlCajas;
import com.acoasmi.roble.entity.Usuarios;
import com.acoasmi.roble.repository.ControlCajasRepository;
import com.acoasmi.roble.repository.UsuariosRepository;
import com.acoasmi.roble.service.ControlCajasService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
public class ControlCajasServiceImpl extends AcoasmiServiceImpl<ControlCajas,
        CajaAperturaRequestDTO, ControlCajasResponseDTO, Long>
        implements ControlCajasService {

    private final ControlCajasRepository controlCajasRepository;
    private final UsuariosRepository usuariosRepository;

    public ControlCajasServiceImpl(ControlCajasRepository controlCajasRepository,
                                   UsuariosRepository usuariosRepository) {
        super(controlCajasRepository, ControlCajas.class);
        this.controlCajasRepository = controlCajasRepository;
        this.usuariosRepository = usuariosRepository;
    }


    @Override
    protected ControlCajasResponseDTO mapToResponseDTO(ControlCajas entidad) {
        if (entidad == null) return null;

        ControlCajasResponseDTO dto = new ControlCajasResponseDTO();
        dto.setIdSesionCaja(entidad.getId());
        dto.setNumeroCaja(entidad.getNumeroCaja());
        dto.setMontoApertura(entidad.getMontoApertura());
        dto.setMontoCierreTeorico(entidad.getMontoCierreTeorico());
        dto.setMontoCierreReal(entidad.getMontoCierreReal());
        dto.setFechaApertura(entidad.getFechaApertura());
        dto.setFechaCierre(entidad.getFechaCierre());

        if (entidad.getUsuarioCajero() != null) {
            String nombres = entidad.getUsuarioCajero().getNombres() != null ? entidad.getUsuarioCajero().getNombres() : "";
            String apellidos = entidad.getUsuarioCajero().getApellidos() != null ? entidad.getUsuarioCajero().getApellidos() : "";

            dto.setNombreCajero((nombres + " " + apellidos).trim());
        }

        BigDecimal real = entidad.getMontoCierreReal() != null ? entidad.getMontoCierreReal() : BigDecimal.ZERO;
        BigDecimal teorico = entidad.getMontoCierreTeorico() != null ? entidad.getMontoCierreTeorico() : BigDecimal.ZERO;
        dto.setDiferencia(real.subtract(teorico));

        return dto;
    }

    @Override
    protected void mapearDtoAEntidad(CajaAperturaRequestDTO requestDto, ControlCajas entity) {

        if (requestDto != null && entity != null) {
            entity.setMontoApertura(requestDto.getMontoApertura());
        }
    }

    @Override
    @Transactional
    public ControlCajasResponseDTO create(CajaAperturaRequestDTO createDto) {

        Usuarios usuario = usuariosRepository.findByUsuarioIgnoreCaseAndEstadoTrue(createDto.getUsuarioCajero())
                .orElseThrow(() -> new RuntimeException("El cajero '" + createDto.getUsuarioCajero() + "' no existe o está inactivo"));

        controlCajasRepository.findByUsuarioCajeroIdAndFechaCierreIsNullAndEstadoTrue(usuario.getId())
                .ifPresent(caja -> {
                    throw new IllegalStateException("El cajero ya cuenta con una sesión de caja activa y abierta");
                });

        ControlCajas nuevaCaja = getCajas(createDto, usuario);

        ControlCajas cajaGuardada = controlCajasRepository.save(nuevaCaja);
        return mapToResponseDTO(cajaGuardada);
    }

    private static @NonNull ControlCajas getCajas(CajaAperturaRequestDTO createDto, Usuarios usuario) {
        ControlCajas nuevaCaja = new ControlCajas();
        nuevaCaja.setUsuarioCajero(usuario);

        // 1. Asignar el número de caja enviado en el DTO (Ej: "01", "02")
        if (createDto.getNumeroCaja() == null || createDto.getNumeroCaja().isBlank()) {
            throw new IllegalArgumentException("El número de caja es obligatorio para realizar la apertura.");
        }
        nuevaCaja.setNumeroCaja(createDto.getNumeroCaja().trim());

        // 2. Estado y montos iniciales
        nuevaCaja.setMontoApertura(createDto.getMontoApertura());
        nuevaCaja.setMontoCierreTeorico(createDto.getMontoApertura());
        nuevaCaja.setMontoCierreReal(BigDecimal.ZERO);
        nuevaCaja.setEstado(true); // Asegurar que el estado inicie en 'true' (abierta)
        return nuevaCaja;
    }

    @Override
    @Transactional
    public ControlCajasResponseDTO update(Long id, CajaAperturaRequestDTO updateDto) {
        throw new UnsupportedOperationException("Para actualizar el estado de una caja use los endpoints específicos de arqueo o cierre");
    }


    @Override
    @Transactional
    public ControlCajasResponseDTO realizarArqueoIntermedio(Long idSesionCaja, CajaCierreRequestDTO arqueoDto) {
        ControlCajas caja = controlCajasRepository.findByIdAndEstadoTrue(idSesionCaja)
                .orElseThrow(() -> new RuntimeException("La sesión de caja especificada no existe"));

        if (caja.getFechaCierre() != null) {
            throw new IllegalStateException("No se puede realizar un arqueo en una sesión de caja que ya fue cerrada");
        }

        caja.setMontoCierreReal(arqueoDto.getMontoCierreReal());

        ControlCajas cajaActualizada = controlCajasRepository.save(caja);
        return mapToResponseDTO(cajaActualizada);
    }

    @Override
    @Transactional
    public ControlCajasResponseDTO cerrarCaja(Long idSesionCaja, CajaCierreRequestDTO cierreDto) {
        ControlCajas caja = controlCajasRepository.findByIdAndEstadoTrue(idSesionCaja)
                .orElseThrow(() -> new RuntimeException("La sesión de caja especificada no existe"));

        if (caja.getFechaCierre() != null) {
            throw new IllegalStateException("Esta sesión de caja ya fue cerrada previamente");
        }

        caja.setMontoCierreReal(cierreDto.getMontoCierreReal());
        caja.setFechaCierre(ZonedDateTime.now());

        ControlCajas cajaCerrada = controlCajasRepository.save(caja);
        return mapToResponseDTO(cajaCerrada);
    }
}