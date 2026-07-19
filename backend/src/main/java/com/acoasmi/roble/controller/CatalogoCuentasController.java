package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.CatalogoCuentasRequestDTO;
import com.acoasmi.roble.dto.response.CatalogoCuentasResponseDTO;
import com.acoasmi.roble.entity.CatalogoCuentas;
import com.acoasmi.roble.service.CatalogoCuentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo-cuentas")
@Tag(name = "Catálogo de Cuentas", description = "Controlador para la gestión y estructuración del catálogo de cuentas contable")
public class CatalogoCuentasController extends AcoasmiController<CatalogoCuentas,
        CatalogoCuentasRequestDTO, CatalogoCuentasResponseDTO, Long> {

    private final CatalogoCuentasService catalogoCuentasService;

    public CatalogoCuentasController(CatalogoCuentasService catalogoCuentasService) {
        super(catalogoCuentasService, "Catálogo de Cuentas");
        this.catalogoCuentasService = catalogoCuentasService;
    }

    @GetMapping("/arbol")
    @Operation(
            summary = "Obtener el árbol contable completo",
            description = "Retorna únicamente las cuentas raíz (Nivel 1) con todas sus subcuentas hijas anidadas de forma recursiva y que se encuentren activas."
    )
    public ResponseEntity<List<CatalogoCuentasResponseDTO>> getArbolContable() {
        List<CatalogoCuentasResponseDTO> arbol = catalogoCuentasService.getArbolContable();
        return ResponseEntity.ok(arbol);
    }

    @GetMapping("/subcuentas")
    @Operation(
            summary = "Obtener subcuentas por cuenta padre",
            description = "Retorna una lista plana de las subcuentas activas que dependen directamente de un ID de cuenta padre especificado."
    )
    public ResponseEntity<List<CatalogoCuentasResponseDTO>> getSubCuentasPorPadre(@RequestParam Long idPadre) {
        List<CatalogoCuentasResponseDTO> subCuentas = catalogoCuentasService.getSubCuentasPorPadre(idPadre);
        return ResponseEntity.ok(subCuentas);
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar cuenta por su código estructurado",
            description = "Retorna la información detallada de una cuenta contable activa buscando por su código de cuenta único (ej. '1011-01')."
    )
    public ResponseEntity<CatalogoCuentasResponseDTO> getByCodigoCuenta(@RequestParam String codigoCuenta) {
        CatalogoCuentasResponseDTO cuenta = catalogoCuentasService.getByCodigoCuenta(codigoCuenta);
        return ResponseEntity.ok(cuenta);
    }

}