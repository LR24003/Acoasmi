package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.CatalogoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.CatalogoCuentasResponseDTO;
import com.acoasmi.roble.entity.CatalogoCuentas;
import com.acoasmi.roble.repository.CatalogoCuentasRepository;
import com.acoasmi.roble.service.CatalogoCuentasService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoCuentasServiceImpl extends AcoasmiServiceImpl<CatalogoCuentas, CatalogoCuentasRequestDTO, CatalogoCuentasResponseDTO, Long> implements CatalogoCuentasService {

    private final CatalogoCuentasRepository catalogoCuentasRepository;

    public CatalogoCuentasServiceImpl(CatalogoCuentasRepository catalogoCuentasRepository) {
        super(catalogoCuentasRepository);
        this.catalogoCuentasRepository = catalogoCuentasRepository;
    }

    @Override
    protected CatalogoCuentasResponseDTO convertToResponseDto(CatalogoCuentas entity) {
        if (entity == null) return null;

        List<CatalogoCuentasResponseDTO> subCuentasDto = new ArrayList<>();
        if (entity.getSubCuentas() != null) {
            subCuentasDto = entity.getSubCuentas().stream()
                    .filter(subCuenta -> Boolean.TRUE.equals(subCuenta.getEstado()))
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
        }

        return CatalogoCuentasResponseDTO.builder()
                .id(entity.getId())
                .codigoCuenta(entity.getCodigoCuenta())
                .nombreCuenta(entity.getNombreCuenta())
                .tipoCuenta(entity.getTipoCuenta())
                .nivel(entity.getNivel())
                .naturalezaCuenta(entity.getNaturalezaCuenta())
                .estado(entity.getEstado())
                .idCuentaPadre(entity.getCuentaPadre() != null ? entity.getCuentaPadre().getId() : null)
                .codigoCuentaPadre(entity.getCuentaPadre() != null ? entity.getCuentaPadre().getCodigoCuenta() : null)
                .subCuentas(subCuentasDto)
                .build();
    }

    @Override
    protected CatalogoCuentas convertToEntity(CatalogoCuentasRequestDTO dto) {
        if (dto == null) return null;
        CatalogoCuentas cuenta = new CatalogoCuentas();
        cuenta.setCodigoCuenta(dto.getCodigoCuenta());
        cuenta.setNombreCuenta(dto.getNombreCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setNivel(dto.getNivel());
        cuenta.setNaturalezaCuenta(dto.getNaturalezaCuenta());
        cuenta.setEstado(true);

        if (dto.getIdCuentaPadre() != null) {
            CatalogoCuentas padre = catalogoCuentasRepository.findById(dto.getIdCuentaPadre())
                    .orElseThrow(() -> new RuntimeException("La cuenta padre especificada no existe con ID: " + dto.getIdCuentaPadre()));
            cuenta.setCuentaPadre(padre);
        }

        return cuenta;
    }

    @Override
    protected void updateEntityFromDto(CatalogoCuentasRequestDTO dto, CatalogoCuentas entity) {
        if (dto == null || entity == null) return;

        entity.setCodigoCuenta(dto.getCodigoCuenta());
        entity.setNombreCuenta(dto.getNombreCuenta());
        entity.setTipoCuenta(dto.getTipoCuenta());
        entity.setNivel(dto.getNivel());
        entity.setNaturalezaCuenta(dto.getNaturalezaCuenta());

        if (dto.getIdCuentaPadre() != null) {
            if (entity.getCuentaPadre() == null || !entity.getCuentaPadre().getId().equals(dto.getIdCuentaPadre())) {
                CatalogoCuentas nuevoPadre = catalogoCuentasRepository.findById(dto.getIdCuentaPadre())
                        .orElseThrow(() -> new RuntimeException("La cuenta padre especificada no existe con ID: " + dto.getIdCuentaPadre()));
                entity.setCuentaPadre(nuevoPadre);
            }
        } else {
            entity.setCuentaPadre(null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoCuentasResponseDTO> getArbolContable() {
        return catalogoCuentasRepository.findByCuentaPadreIsNullAndEstadoTrue().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoCuentasResponseDTO> getSubCuentasPorPadre(Long idPadre) {
        return catalogoCuentasRepository.findByCuentaPadreIdAndEstadoTrue(idPadre).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogoCuentasResponseDTO getByCodigoCuenta(String codigoCuenta) {
        CatalogoCuentas entity = catalogoCuentasRepository.findByCodigoCuentaAndEstadoTrue(codigoCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta contable activa no encontrada con el código: " + codigoCuenta));
        return convertToResponseDto(entity);
    }
}