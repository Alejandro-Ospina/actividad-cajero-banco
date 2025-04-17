package alejandro.aplicacion.Aplicacion.Servicios;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LogeoCuentaBancoServicio extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String numeroCuenta) throws UsernameNotFoundException;
}
