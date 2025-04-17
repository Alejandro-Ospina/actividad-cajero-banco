package alejandro.aplicacion.Infraestructura.Implementaciones.Servicios;

import alejandro.aplicacion.Aplicacion.Servicios.LogeoCuentaBancoServicio;
import alejandro.aplicacion.Dominio.Repositorios.CuentaBancoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogeoCuentaBancoServicioImpl implements LogeoCuentaBancoServicio {

    private final CuentaBancoRepositorio repositorio;

    @Override
    public UserDetails loadUserByUsername(String numeroCuenta) throws UsernameNotFoundException {
        return repositorio.findByNumeroCuenta(numeroCuenta).orElseThrow(
                () -> new UsernameNotFoundException("No se encontró el usuario con la cuenta: " + numeroCuenta)
        );
    }
}
