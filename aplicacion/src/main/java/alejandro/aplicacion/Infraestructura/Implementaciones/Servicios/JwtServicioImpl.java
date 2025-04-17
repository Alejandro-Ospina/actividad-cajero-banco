package alejandro.aplicacion.Infraestructura.Implementaciones.Servicios;

import alejandro.aplicacion.Aplicacion.Servicios.JwtService;
import alejandro.aplicacion.Dominio.Modelos.CuentaBanco;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtServicioImpl implements JwtService {

    @Value("${firma.token}")
    private String claveSecreta;

    @Value("${expiracion.normal}")
    private int tiempoNormal;

    @Value("${expiracion.refresh.token}")
    private int tiempoRefresh;

    @Override
    public String generarToken(CuentaBanco cuentaBanco) {
        return Jwts.builder()
                .issuer("Api bancaria de prueba")
                .subject(cuentaBanco.getUsername())
                .claim("nombre", cuentaBanco.getUsuario().getNombre())
                .claim("cuenta de banco", cuentaBanco.getNumeroCuenta())
                .claim("tipo de cuenta", cuentaBanco.getTipoCuenta().name())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expiracionToken(tiempoNormal))
                .signWith(obtenerClaveFirmada(), Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public String generarRefreshToken(CuentaBanco cuentaBanco) {
        return Jwts.builder()
                .header()
                .type("REFRESH")
                .and()
                .issuer("Api bancaria de prueba")
                .subject(cuentaBanco.getUsername())
                .claim("nombre", cuentaBanco.getUsuario().getNombre())
                .claim("cuenta de banco", cuentaBanco.getNumeroCuenta())
                .claim("tipo de cuenta", cuentaBanco.getTipoCuenta().name())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expiracionToken(tiempoRefresh))
                .signWith(obtenerClaveFirmada(), Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public boolean esValidoElToken(String token) {
        Claims reclamaciones;
        try{
            reclamaciones = Jwts.parser()
                    .verifyWith(obtenerClaveFirmada())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return reclamaciones.getExpiration().after(new Date());
        } catch(JwtException | IllegalArgumentException ex){
            return false;
        }
    }

    @Override
    public String obtenerSujeto(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(obtenerClaveFirmada())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch(JwtException | IllegalArgumentException ex){
            throw new RuntimeException("No se ha podido verificar la identidad del usuario");
        }
    }

    @Override
    public String obtenerTipo(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(obtenerClaveFirmada())
                    .build()
                    .parseSignedClaims(token)
                    .getHeader()
                    .getType();
        } catch(JwtException | IllegalArgumentException ex){
            throw new RuntimeException("Token no reconocido");
        }
    }

    @Override
    public String obtenerCuentaBancaria(String refreshToken) {
        try{
            return (String) Jwts.parser()
                    .verifyWith(obtenerClaveFirmada())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload()
                    .get("cuenta de banco");
        } catch(JwtException | IllegalArgumentException ex){
            throw new RuntimeException("Token no reconocido");
        }
    }

    private Date expiracionToken(int tiempoSegundos){
        return Date.from(Instant.now().plusSeconds(tiempoSegundos));
    }

    private SecretKey obtenerClaveFirmada(){
        return Keys.hmacShaKeyFor(claveSecreta.getBytes(StandardCharsets.UTF_8));
    }
}
