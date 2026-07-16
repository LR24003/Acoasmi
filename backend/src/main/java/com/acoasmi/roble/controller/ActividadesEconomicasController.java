package com.acoasmi.roble.controller;

import com.acoasmi.roble.dto.request.ActividadesEconomicasRequestDTO;
import com.acoasmi.roble.dto.response.ActividadesEconomicasResponseDTO;
import com.acoasmi.roble.entity.ActividadesEconomicas;
import com.acoasmi.roble.service.ActividadesEconomicasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actividades-economicas")
@Tag(name = "Actividades Económicas", description = "Endpoints para la gestión y consulta de los giros o actividades económicas")
public class ActividadesEconomicasController extends AcoasmiController<ActividadesEconomicas,
        ActividadesEconomicasRequestDTO, ActividadesEconomicasResponseDTO, Long> {

    private final ActividadesEconomicasService actividadesEconomicasService;

    public ActividadesEconomicasController(ActividadesEconomicasService actividadesEconomicasService) {
        super(actividadesEconomicasService, "Actividades Económicas");
        this.actividadesEconomicasService = actividadesEconomicasService;
    }

    @GetMapping("/buscar/codigo-mh/{codigoMh}")
    @Operation(
            summary = "Buscar por código MH",
            description = "Busca una actividad económica específica utilizando el código estandarizado de Hacienda."
    )
    public ResponseEntity<ActividadesEconomicasResponseDTO> getByCodigoMh(@PathVariable String codigoMh) {
        return ResponseEntity.ok(actividadesEconomicasService.getByCodigoMh(codigoMh));
    }

    @GetMapping("/buscar/descripcion")
    @Operation(
            summary = "Buscar por descripción",
            description = "Permite buscar la información de una actividad económica usando su descripción exacta."
    )
    public ResponseEntity<ActividadesEconomicasResponseDTO> getByDescripcion(@RequestParam String descripcion) {
        return ResponseEntity.ok(actividadesEconomicasService.getByDescripcion(descripcion));
    }
}
