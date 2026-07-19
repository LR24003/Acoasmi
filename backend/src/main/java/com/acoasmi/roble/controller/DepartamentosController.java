package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.DepartamentosRequestDTO;
import com.acoasmi.roble.dto.response.DepartamentosResponseDTO;
import com.acoasmi.roble.entity.Departamentos;
import com.acoasmi.roble.service.DepartamentosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departamentos")
@Tag(name = "Departamentos", description = "Endpoints para la gestión y consulta de los departamentos")
public class DepartamentosController extends AcoasmiController<Departamentos,
        DepartamentosRequestDTO, DepartamentosResponseDTO, Long> {

    private final DepartamentosService departamentosService;

    public DepartamentosController(DepartamentosService departamentosService) {
        super(departamentosService, "Departamentos");
        this.departamentosService = departamentosService;
    }

    @GetMapping("/buscar/codigo/{codigoDepartamento}")
    @Operation(
            summary = "Buscar por código de departamento",
            description = "Busca los datos de un departamento utilizando su código geopolítico único."
    )
    public ResponseEntity<DepartamentosResponseDTO> getByCodigoDepartamento(@PathVariable Integer codigoDepartamento) {
        return ResponseEntity.ok(departamentosService.getByCodigoDepartamento(codigoDepartamento));
    }

    @GetMapping("/buscar/nombre")
    @Operation(
            summary = "Buscar por nombre",
            description = "Busca departamentos activos cuyo nombre contenga la cadena proporcionada."
    )
    public ResponseEntity<DepartamentosResponseDTO> getByNombre(@RequestParam String nombreDepartamento) {
        return ResponseEntity.ok(departamentosService.getByNombreDepartamento(nombreDepartamento));
    }
}