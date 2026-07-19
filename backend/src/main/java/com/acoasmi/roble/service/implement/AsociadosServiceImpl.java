package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoCuentasResponseDTO;
import com.acoasmi.roble.dto.response.AsociadosBeneficiariosResponseDTO;
import com.acoasmi.roble.dto.response.AsociadosResponseDTO;
import com.acoasmi.roble.entity.AsociadoCuentas;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.AsociadosBeneficiarios;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.AsociadosService;
import com.acoasmi.roble.service.CumplimientoPerfilRiesgoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class AsociadosServiceImpl
        extends AcoasmiServiceImpl<Asociados, AsociadosRequestDTO, AsociadosResponseDTO, Long>
        implements AsociadosService {

    private final AsociadosRepository asociadosRepository;
    private final ActividadesEconomicasRepository actividadesEconomicasRepository;
    private final DepartamentosRepository departamentosRepository;
    private final MunicipiosRepository municipiosRepository;
    private final DistritosRepository distritosRepository;
    private final CumplimientoPerfilRiesgoService cumplimientoService;
    private final AsociadosBeneficiariosRepository asociadosBeneficiariosRepository;
    private final AsociadoCuentasRepository asociadoCuentasRepository;

    public AsociadosServiceImpl(AsociadosRepository asociadosRepository,
                                ActividadesEconomicasRepository actividadesEconomicasRepository,
                                DepartamentosRepository departamentosRepository,
                                MunicipiosRepository municipiosRepository,
                                DistritosRepository distritosRepository,
                                CumplimientoPerfilRiesgoService cumplimientoService,
                                AsociadosBeneficiariosRepository asociadosBeneficiariosRepository,
                                AsociadoCuentasRepository asociadoCuentasRepository) {

        super(asociadosRepository, Asociados.class);

        this.asociadosRepository = asociadosRepository;
        this.actividadesEconomicasRepository = actividadesEconomicasRepository;
        this.departamentosRepository = departamentosRepository;
        this.municipiosRepository = municipiosRepository;
        this.distritosRepository = distritosRepository;
        this.cumplimientoService = cumplimientoService;
        this.asociadosBeneficiariosRepository = asociadosBeneficiariosRepository;
        this.asociadoCuentasRepository = asociadoCuentasRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadosResponseDTO> getByNombreCompletoAsociado(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El criterio de búsqueda por nombre no puede estar vacío");
        }
        return asociadosRepository.findByNombreCompletoAsociadoContainingIgnoreCase(nombreCompleto.trim())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadosResponseDTO getByNumeroAsociado(Integer numeroAsociado) {
        return asociadosRepository.findByNumeroAsociadoAndEstadoTrue(numeroAsociado)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún asociado con el número: " + numeroAsociado));
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadosResponseDTO getByNumeroDocumento(String numeroDocumento) {
        return asociadosRepository.findByNumeroDocumento(numeroDocumento)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún asociado con el documento: " + numeroDocumento));
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadosResponseDTO getByNit(String nit) {
        return asociadosRepository.findByNit(nit)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún asociado con el NIT: " + nit));
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadosResponseDTO getByEmail(String email) {
        return asociadosRepository.findByEmailIgnoreCase(email)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún asociado con el email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AsociadosResponseDTO> getByEstado(Boolean estado, Pageable pageable) {
        return asociadosRepository.findByEstado(estado, pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AsociadosResponseDTO> buscarGlobal(String filtro, Pageable pageable) {
        return asociadosRepository.buscarPorNombresApellidosODocumento(filtro, pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional
    public AsociadosResponseDTO create(AsociadosRequestDTO requestDto) {

        if (asociadosRepository.findByNumeroDocumento(requestDto.getNumeroDocumento()).isPresent()) {
            throw new RuntimeException("El número de documento ya está registrado.");
        }
        if (asociadosRepository.findByNumeroAsociadoAndEstadoTrue(requestDto.getNumeroAsociado()).isPresent()) {
            throw new RuntimeException("El número de asociado ya está asignado.");
        }
        if (asociadosRepository.findByEmailIgnoreCase(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        if (requestDto.getBeneficiarios() == null || requestDto.getBeneficiarios().isEmpty()) {
            throw new RuntimeException("Debe registrar al menos un beneficiario para completar la afiliación.");
        }

        BigDecimal sumaPorcentajes = requestDto.getBeneficiarios().stream()
                .map(b -> b.getPorcentaje() != null ? b.getPorcentaje() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumaPorcentajes.compareTo(new BigDecimal("100.00")) != 0) {
            throw new RuntimeException("La suma de los porcentajes de los beneficiarios debe ser exactamente el 100.00%");
        }

        Asociados asociado = new Asociados();
        mapearDtoAEntidad(requestDto, asociado);

        asociado.setEstado(true);
        if (asociado.getFechaIngreso() == null) {
            asociado.setFechaIngreso(LocalDateTime.now());
        }

        Asociados asociadoGuardado = asociadosRepository.save(asociado);

        requestDto.getBeneficiarios().forEach(bDto -> {
            AsociadosBeneficiarios beneficiario = new AsociadosBeneficiarios();
            beneficiario.setAsociado(asociadoGuardado);
            beneficiario.setCuenta(null);
            beneficiario.setNombreBeneficiario(bDto.getNombreBeneficiario());
            beneficiario.setParentesco(bDto.getParentesco());
            beneficiario.setPorcentaje(bDto.getPorcentaje());
            beneficiario.setNumeroDocumento(bDto.getNumeroDocumento());
            beneficiario.setFechaNacimiento(bDto.getFechaNacimiento());
            asociadosBeneficiariosRepository.save(beneficiario);
        });

        crearCuentaAutomatica(asociadoGuardado, "APORTACIONES");
        crearCuentaAutomatica(asociadoGuardado, "AHORRO VISTA");
        crearCuentaAutomatica(asociadoGuardado, "AHORRO VISTA");

        cumplimientoService.generarPerfilRiesgoInicial(asociadoGuardado, requestDto);

        return mapToResponseDTO(asociadoGuardado);
    }

    @Override
    @Transactional
    public AsociadosResponseDTO update(Long id, AsociadosRequestDTO requestDto) {
        Asociados asociado = asociadosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asociado no encontrado con el ID: " + id));

        asociadosRepository.findByNumeroDocumento(requestDto.getNumeroDocumento())
                .ifPresent(a -> {
                    if (!a.getId().equals(id)) {
                        throw new RuntimeException("El número de documento ya existe.");
                    }
                });

        mapearDtoAEntidad(requestDto, asociado);

        return mapToResponseDTO(asociadosRepository.save(asociado));
    }

    @Override
    protected void mapearDtoAEntidad(AsociadosRequestDTO request, Asociados asociado) {
        asociado.setNumeroAsociado(request.getNumeroAsociado());
        asociado.setNombres(request.getNombres());
        asociado.setApellidos(request.getApellidos());
        asociado.setTipoDocumento(request.getTipoDocumento());
        asociado.setNumeroDocumento(request.getNumeroDocumento());
        asociado.setNit(request.getNit());

        asociado.setNrc(request.getNrc() != null && !request.getNrc().isBlank() ? request.getNrc() : "N/A");

        asociado.setFechaNacimiento(request.getFechaNacimiento());
        asociado.setTelefono(request.getTelefono());
        asociado.setEmail(request.getEmail());
        asociado.setDireccionComplementaria(request.getDireccionComplementaria());

        if (request.getFechaNacimiento() != null) {
            int edadCalculada = Period.between(request.getFechaNacimiento(), LocalDate.now()).getYears();
            asociado.setEdad(edadCalculada);
        }

        asociado.setActividadEconomica(actividadesEconomicasRepository.findByDescripcionContainingIgnoreCase(request.getActividadEconomica())
                .orElseThrow(() -> new RuntimeException("La actividad económica '" + request.getActividadEconomica() + "' no es válida.")));

        asociado.setDepartamento(departamentosRepository.findByNombreDepartamentoContainingIgnoreCaseAndEstadoTrue(request.getNombreDepartamento())
                .orElseThrow(() -> new RuntimeException("El departamento seleccionado no es válido.")));

        asociado.setMunicipio(municipiosRepository.findByNombreMunicipioContainingIgnoreCaseAndEstadoTrue(request.getNombreMunicipio())
                .orElseThrow(() -> new RuntimeException("El municipio seleccionado no es válido.")));

        asociado.setDistrito(distritosRepository.findByNombreDistritoContainingIgnoreCase(request.getNombreDistrito())
                .orElseThrow(() -> new RuntimeException("El distrito seleccionado no es válido.")));
    }

    @Override
    protected AsociadosResponseDTO mapToResponseDTO(Asociados asociado) {
        Integer edad = asociado.getEdad();
        if (edad == null && asociado.getFechaNacimiento() != null) {
            edad = Period.between(asociado.getFechaNacimiento(), LocalDate.now()).getYears();
        }

        List<AsociadoCuentasResponseDTO> cuentasDto = asociado.getCuentas() != null ?
                asociado.getCuentas().stream().map(c -> new AsociadoCuentasResponseDTO(
                        c.getId(),
                        c.getNumeroCuenta(),
                        asociado.getNumeroAsociado() != null ? asociado.getNumeroAsociado().toString() : "",
                        asociado.getNombres() + " " + asociado.getApellidos(),
                        c.getTipoCuenta(),
                        c.getTasaInteresAnual(),
                        c.getSaldoActual(),
                        c.getEstadoCuenta(),
                        null,
                        java.util.Collections.emptyList(),
                        c.getEstado()
                )).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList();

        List<AsociadosBeneficiariosResponseDTO> beneficiariosDto = asociado.getBeneficiarios() != null ?
                asociado.getBeneficiarios().stream().map(b -> new AsociadosBeneficiariosResponseDTO(
                        b.getId(),
                        b.getNombreBeneficiario(),
                        b.getParentesco(),
                        b.getPorcentaje(),
                        b.getNumeroDocumento(),
                        b.getFechaNacimiento()
                )).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList();

        return new AsociadosResponseDTO(
                asociado.getId(),
                asociado.getNumeroAsociado(),
                asociado.getNombres(),
                asociado.getApellidos(),
                asociado.getTipoDocumento(),
                asociado.getNumeroDocumento(),
                asociado.getNit(),
                asociado.getNrc(),
                asociado.getFechaNacimiento(),
                edad,
                asociado.getActividadEconomica() != null ? asociado.getActividadEconomica().getDescripcion() : "No especificada",
                asociado.getTelefono(),
                asociado.getEmail(),
                asociado.getDepartamento() != null ? asociado.getDepartamento().getNombreDepartamento() : "No especificado",
                asociado.getMunicipio() != null ? asociado.getMunicipio().getNombreMunicipio() : "No especificado",
                asociado.getDistrito() != null ? asociado.getDistrito().getNombreDistrito() : "No especificado",
                asociado.getDireccionComplementaria(),
                asociado.getFechaIngreso(),
                asociado.getFechaRetiro(),
                asociado.getEstado(),
                cuentasDto,
                beneficiariosDto
        );
    }

    private void crearCuentaAutomatica(Asociados asociado, String tipoCuenta) {
        AsociadoCuentas cuenta = new AsociadoCuentas();
        cuenta.setAsociado(asociado);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSaldoActual(BigDecimal.ZERO);

        cuenta.setTasaInteresAnual("APORTACIONES".equals(tipoCuenta) ? new BigDecimal("0.00") : new BigDecimal("1.50"));
        cuenta.setEstadoCuenta("ACTIVA");
        cuenta.setEstado(true);

        String numeroAsociado = asociado.getNumeroAsociado() != null ? asociado.getNumeroAsociado().toString() : "0000";
        String numeroCuentaGenerado;

        if ("APORTACIONES".equals(tipoCuenta)) {
            numeroCuentaGenerado = numeroAsociado;
        } else {
            String prefijoBase = "1011-" + numeroAsociado + "-";
            int correlativo = 1;
            while (asociadoCuentasRepository.findByNumeroCuenta(prefijoBase + correlativo).isPresent()) {
                correlativo++;
            }
            numeroCuentaGenerado = prefijoBase + correlativo;
        }

        cuenta.setNumeroCuenta(numeroCuentaGenerado);
        asociadoCuentasRepository.save(cuenta);
    }
}