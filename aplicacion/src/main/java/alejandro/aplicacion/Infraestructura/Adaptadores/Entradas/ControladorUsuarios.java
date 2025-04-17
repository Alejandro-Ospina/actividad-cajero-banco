package alejandro.aplicacion.Infraestructura.Adaptadores.Entradas;

import alejandro.aplicacion.Aplicacion.Servicios.CuentaBancoServicio;
import alejandro.aplicacion.Dominio.Dtos.*;
import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import alejandro.aplicacion.Dominio.Modelos.Transferencia;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import alejandro.aplicacion.Dominio.Repositorios.TransferenciaRepositorio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/usuarios")
@RequiredArgsConstructor
public class ControladorUsuarios {

    private final CuentaBancoServicio cuentaBancoServicio;
    private final CuentaBancoRepositorio repositorio;
    private final TransferenciaRepositorio transferenciaRepositorio;

    @PostMapping ("/crear-usuario")
    public ResponseEntity<FormatoRespuesta> crearUsuario(@RequestBody @Valid UsuarioDto usuarioDto,
                                                         UriComponentsBuilder uriBuilder){
        var cuentaBancoDto = cuentaBancoServicio.guardarCuentaBanco(usuarioDto);

        URI localizacion = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(cuentaBancoDto.id())
                .toUri();

        return ResponseEntity.created(localizacion).build();
    }

    @PutMapping ("/transferir")
    public ResponseEntity<MostrarTransferenciaDto> transferirDinero(@RequestBody @Valid TransferirDineroDto dto,
                                                                    Authentication authentication){
        var detallesTransferencia = cuentaBancoServicio.transferirDinero(dto, authentication);

        return ResponseEntity.accepted().body(detallesTransferencia);
    }

    @PostMapping ("/crear-nueva-cuenta")
    public ResponseEntity<FormatoRespuesta> crearNuevaCuenta(@RequestBody @Valid CuentaBancoDto dto,
                                                             Authentication authentication,
                                                             UriComponentsBuilder componentsBuilder){
        var cuentaBancoNueva = cuentaBancoServicio.crearNuevaCuenta(dto, authentication);

        URI localizacion = componentsBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(cuentaBancoNueva.id())
                .toUri();

        return ResponseEntity.created(localizacion).build();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<MostrarCuentaBancoDto> mostrarCuentaBancoCreada(@PathVariable @Positive Integer id){
        var cuentaBanco = repositorio.findById(id).orElseThrow();
        return ResponseEntity.ok().body(
                new MostrarCuentaBancoDto(cuentaBanco)
        );
    }

    @GetMapping ("/historial")
    public ResponseEntity<?> obtenerHistorialTransferencias(@PageableDefault(size = 5,
                                                                        sort = "fecha",
                                                                        direction = Sort.Direction.DESC) Pageable pageable,
                                                            Authentication usuarioAutenticado){
        var cuenta = (CuentaBanco) usuarioAutenticado.getPrincipal();
        return ResponseEntity.ok(
                 transferenciaRepositorio
                         .findAllByCuentaEmisor(cuenta.getNumeroCuenta(), pageable)
                         .map(MostrarTransferenciaDto::new)
        );
    }
}
