package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.AsociadosBeneficiariosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosBeneficiariosResponseDTO;
import com.acoasmi.roble.entity.AsociadosBeneficiarios;
import com.acoasmi.roble.service.AsociadosBeneficiariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/beneficiarios")
@Tag(name = "Beneficiarios", description = "Controlador para la gestión de beneficiarios de cuentas y afiliaciones")
public class AsociadosBeneficiariosController extends AcoasmiController<AsociadosBeneficiarios,
        AsociadosBeneficiariosRequestDTO, AsociadosBeneficiariosResponseDTO, Long> {

    private final AsociadosBeneficiariosService beneficiariosService;

    public AsociadosBeneficiariosController(AsociadosBeneficiariosService beneficiariosService) {
        super(beneficiariosService, "Beneficiarios");
        this.beneficiariosService = beneficiariosService;
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(summary = "Obtener beneficiarios por número de cuenta",
            description = "Retorna la lista de beneficiarios asociados a una cuenta pasiva específica")
    public ResponseEntity<List<AsociadosBeneficiariosResponseDTO>> getByNumeroCuenta(
            @PathVariable String numeroCuenta) {
        return ResponseEntity.ok(beneficiariosService.getByNumeroCuenta(numeroCuenta));
    }

    @GetMapping("/asociado/{numeroAsociado}")
    @Operation(summary = "Obtener beneficiarios por número de asociado",
            description = "Retorna todos los beneficiarios vinculados a un número de asociado (globales y de sus cuentas)")
    public ResponseEntity<List<AsociadosBeneficiariosResponseDTO>> getByNumeroAsociado(
            @PathVariable Integer numeroAsociado) {
        return ResponseEntity.ok(beneficiariosService.getByNumeroAsociado(numeroAsociado));
    }
}
