package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.AsociadoCuentasResponseDTO;
import com.acoasmi.roble.dto.response.AsociadosBeneficiariosResponseDTO;
import com.acoasmi.roble.entity.AsociadoCuentas;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.entity.AsociadosBeneficiarios;
import com.acoasmi.roble.repository.AsociadoCuentasRepository;
import com.acoasmi.roble.repository.AsociadosRepository;
import com.acoasmi.roble.repository.AsociadosBeneficiariosRepository;
import com.acoasmi.roble.service.AsociadoCuentasService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsociadoCuentasServiceImpl
        extends AcoasmiServiceImpl<AsociadoCuentas, AsociadoCuentasRequestDTO, AsociadoCuentasResponseDTO, Long>
        implements AsociadoCuentasService {

    private final AsociadoCuentasRepository cuentaRepository;
    private final AsociadosRepository asociadosRepository;
    private final AsociadosBeneficiariosRepository beneficiariosRepository;

    public AsociadoCuentasServiceImpl(AsociadoCuentasRepository cuentaRepository,
                                      AsociadosRepository asociadosRepository,
                                      AsociadosBeneficiariosRepository beneficiariosRepository) {
        super(cuentaRepository, AsociadoCuentas.class);
        this.cuentaRepository = cuentaRepository;
        this.asociadosRepository = asociadosRepository;
        this.beneficiariosRepository = beneficiariosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadoCuentasResponseDTO getByNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede estar vacío");
        }
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new RuntimeException("No se encontró la cuenta número: " + numeroCuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadoCuentasResponseDTO> getByNumeroAsociado(Integer numeroAsociado) {
        if (numeroAsociado == null) {
            throw new IllegalArgumentException("El número de asociado no puede ser nulo");
        }
        return cuentaRepository.findByAsociadoNumeroAsociado(numeroAsociado)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AsociadoCuentasResponseDTO create(AsociadoCuentasRequestDTO requestDto) {

        Asociados asociado = asociadosRepository.findByNumeroAsociadoAndEstadoTrue(requestDto.getNumeroAsociado())
                .orElseThrow(() -> new RuntimeException("Asociado activo no encontrado con el número de asociado especificado: " + requestDto.getNumeroAsociado()));

        if (requestDto.getBeneficiarios() != null && !requestDto.getBeneficiarios().isEmpty()) {
            BigDecimal sumaPorcentajes = requestDto.getBeneficiarios().stream()
                    .map(b -> b.getPorcentaje() != null ? b.getPorcentaje() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sumaPorcentajes.compareTo(new BigDecimal("100.00")) != 0) {
                throw new RuntimeException("La suma de los porcentajes de los beneficiarios de la cuenta debe ser exactamente el 100.00%");
            }
        }

        AsociadoCuentas cuenta = new AsociadoCuentas();
        cuenta.setAsociado(asociado);
        cuenta.setTipoCuenta(requestDto.getTipoCuenta());
        cuenta.setSaldoActual(requestDto.getSaldoInicial());
        cuenta.setTasaInteresAnual(requestDto.getTasaInteresAnual());
        cuenta.setEstadoCuenta("ACTIVA");
        cuenta.setEstado(true);

        String numeroCuentaGenerado = generarNumeroCuentaInstitucional(asociado, requestDto.getTipoCuenta());
        cuenta.setNumeroCuenta(numeroCuentaGenerado);

        AsociadoCuentas cuentaGuardada = cuentaRepository.save(cuenta);

        if (requestDto.getBeneficiarios() != null) {
            requestDto.getBeneficiarios().forEach(bDto -> {
                AsociadosBeneficiarios beneficiario = new AsociadosBeneficiarios();
                beneficiario.setAsociado(asociado);
                beneficiario.setCuenta(cuentaGuardada);
                beneficiario.setNombreBeneficiario(bDto.getNombreBeneficiario());
                beneficiario.setParentesco(bDto.getParentesco());
                beneficiario.setPorcentaje(bDto.getPorcentaje());
                beneficiario.setNumeroDocumento(bDto.getNumeroDocumento());
                beneficiario.setFechaNacimiento(bDto.getFechaNacimiento());
                beneficiario.setEstado(true);
                beneficiariosRepository.save(beneficiario);
            });
        }

        return mapToResponseDTO(cuentaGuardada);
    }

    @Override
    protected void mapearDtoAEntidad(AsociadoCuentasRequestDTO request, AsociadoCuentas entidad) {
        entidad.setTipoCuenta(request.getTipoCuenta());
        entidad.setTasaInteresAnual(request.getTasaInteresAnual());
    }

    @Override
    protected AsociadoCuentasResponseDTO mapToResponseDTO(AsociadoCuentas entidad) {
        String nombreCompletoAsociado = (entidad.getAsociado() != null)
                ? entidad.getAsociado().getNombres() + " " + entidad.getAsociado().getApellidos()
                : "No especificado";

        String numeroAsociado = (entidad.getAsociado() != null && entidad.getAsociado().getNumeroAsociado() != null)
                ? entidad.getAsociado().getNumeroAsociado().toString()
                : "N/A";

        List<AsociadosBeneficiariosResponseDTO> beneficiarios = null;
        if (entidad.getBeneficiarios() != null) {
            beneficiarios = entidad.getBeneficiarios().stream()
                    .map(b -> new AsociadosBeneficiariosResponseDTO(
                            b.getId(),
                            b.getNombreBeneficiario(),
                            b.getParentesco(),
                            b.getPorcentaje(),
                            b.getNumeroDocumento(),
                            b.getFechaNacimiento()
                    )).collect(Collectors.toList());
        }

        return new AsociadoCuentasResponseDTO(
                entidad.getId(),
                entidad.getNumeroCuenta(),
                numeroAsociado,
                nombreCompletoAsociado,
                entidad.getTipoCuenta(),
                entidad.getTasaInteresAnual(),
                entidad.getSaldoActual(),
                entidad.getEstadoCuenta(),
                entidad.getFechaApertura(),
                beneficiarios,
                entidad.getEstado()
        );
    }

    private String generarNumeroCuentaInstitucional(Asociados asociado, String tipoCuenta) {
        String numeroAsociado = asociado.getNumeroAsociado() != null
                ? asociado.getNumeroAsociado().toString()
                : "0000";

        if ("APORTACIONES".equals(tipoCuenta)) {
            return numeroAsociado;
        } else {
            String prefijoBase = "1011-" + numeroAsociado + "-";
            int correlativo = 1;

            while (cuentaRepository.findByNumeroCuenta(prefijoBase + correlativo).isPresent()) {
                correlativo++;
            }

            return prefijoBase + correlativo;
        }
    }
}