package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadosBeneficiariosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosBeneficiariosResponseDTO;
import com.acoasmi.roble.entity.AsociadosBeneficiarios;
import com.acoasmi.roble.repository.AsociadosBeneficiariosRepository;
import com.acoasmi.roble.service.AsociadosBeneficiariosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsociadosBeneficiariosServiceImpl
        extends AcoasmiServiceImpl<AsociadosBeneficiarios, AsociadosBeneficiariosRequestDTO,
        AsociadosBeneficiariosResponseDTO, Long>
        implements AsociadosBeneficiariosService {

    private final AsociadosBeneficiariosRepository beneficiariosRepository;

    public AsociadosBeneficiariosServiceImpl(AsociadosBeneficiariosRepository beneficiariosRepository) {
        super(beneficiariosRepository, AsociadosBeneficiarios.class);
        this.beneficiariosRepository = beneficiariosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadosBeneficiariosResponseDTO> getByNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null) {
            throw new IllegalArgumentException("El número de cuenta no puede ser nulo");
        }
        return beneficiariosRepository.findByCuentaNumeroCuenta(numeroCuenta)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsociadosBeneficiariosResponseDTO> getByNumeroAsociado(Integer numeroAsociado) {
        if (numeroAsociado == null) {
            throw new IllegalArgumentException("El número de asociado no puede ser nulo");
        }
        return beneficiariosRepository.findByAsociadoNumeroAsociado(numeroAsociado)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    protected void mapearDtoAEntidad(AsociadosBeneficiariosRequestDTO request, AsociadosBeneficiarios entidad) {
        entidad.setNombreBeneficiario(request.getNombreBeneficiario());
        entidad.setParentesco(request.getParentesco());
        entidad.setTelefono(request.getTelefono());
        entidad.setPorcentaje(request.getPorcentaje());
        entidad.setNumeroDocumento(request.getNumeroDocumento());
        entidad.setFechaNacimiento(request.getFechaNacimiento());

    }

    @Override
    protected AsociadosBeneficiariosResponseDTO mapToResponseDTO(AsociadosBeneficiarios entidad) {
        return new AsociadosBeneficiariosResponseDTO(
                entidad.getId(),
                entidad.getCuenta().getNumeroCuenta(),
                entidad.getNombreBeneficiario(),
                entidad.getTelefono(),
                entidad.getParentesco(),
                entidad.getPorcentaje(),
                entidad.getNumeroDocumento(),
                entidad.getFechaNacimiento()
        );
    }
}
