package com.acoasmi.roble.service.implement;

import com.acoasmi.roble.dto.request.AsociadosRequestDTO;
import com.acoasmi.roble.dto.response.AsociadosResponseDTO;
import com.acoasmi.roble.entity.Asociados;
import com.acoasmi.roble.repository.*;
import com.acoasmi.roble.service.AsociadosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class AsociadosServiceImpl
        extends AcoasmiServiceImpl<Asociados, AsociadosRequestDTO, AsociadosResponseDTO, Long>
        implements AsociadosService {

    private final AsociadosRepository asociadosRepository;
    private final ActividadesEconomicasRepository actividadesEconomicasRepository;
    private final DepartamentosRepository departamentosRepository;
    private final MunicipiosRepository municipiosRepository;
    private final DistritosRepository distritosRepository;

    public AsociadosServiceImpl(AsociadosRepository asociadosRepository,
                                ActividadesEconomicasRepository actividadesEconomicasRepository,
                                DepartamentosRepository departamentosRepository,
                                MunicipiosRepository municipiosRepository,
                                DistritosRepository distritosRepository) {

        super(asociadosRepository, Asociados.class);
        this.asociadosRepository = asociadosRepository;
        this.actividadesEconomicasRepository = actividadesEconomicasRepository;
        this.departamentosRepository = departamentosRepository;
        this.municipiosRepository = municipiosRepository;
        this.distritosRepository = distritosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AsociadosResponseDTO getByNumeroAsociado(Integer numeroAsociado) {
        return asociadosRepository.findByNumeroAsociado(numeroAsociado)
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
        if (asociadosRepository.findByNumeroAsociado(requestDto.getNumeroAsociado()).isPresent()) {
            throw new RuntimeException("El número de asociado ya está asignado.");
        }
        if (asociadosRepository.findByEmailIgnoreCase(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        Asociados asociado = new Asociados();
        mapearDtoAEntidad(requestDto, asociado);

        asociado.setEstado(true);
        if (asociado.getFechaIngreso() == null) {
            asociado.setFechaIngreso(LocalDateTime.now());
        }

        return mapToResponseDTO(asociadosRepository.save(asociado));
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

        asociado.setDepartamento(departamentosRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(request.getNombreDepartamento())
                .orElseThrow(() -> new RuntimeException("El departamento seleccionado no es válido.")));

        asociado.setMunicipio(municipiosRepository.findByNombreContainingIgnoreCaseAndEstadoTrue(request.getNombreMunicipio())
                .orElseThrow(() -> new RuntimeException("El municipio seleccionado no es válido.")));

        asociado.setDistrito(distritosRepository.findByNombreContainingIgnoreCase(request.getNombreDistrito())
                .orElseThrow(() -> new RuntimeException("El distrito seleccionado no es válido.")));
    }

    @Override
    protected AsociadosResponseDTO mapToResponseDTO(Asociados asociado) {
        Integer edad = asociado.getEdad();
        if (edad == null && asociado.getFechaNacimiento() != null) {
            edad = Period.between(asociado.getFechaNacimiento(), LocalDate.now()).getYears();
        }

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
                asociado.getDepartamento() != null ? asociado.getDepartamento().getNombre() : "No especificado",
                asociado.getMunicipio() != null ? asociado.getMunicipio().getNombre() : "No especificado",
                asociado.getDistrito() != null ? asociado.getDistrito().getNombre() : "No especificado",
                asociado.getDireccionComplementaria(),
                asociado.getFechaIngreso(),
                asociado.getFechaRetiro(),
                asociado.getEstado()
        );
    }
}