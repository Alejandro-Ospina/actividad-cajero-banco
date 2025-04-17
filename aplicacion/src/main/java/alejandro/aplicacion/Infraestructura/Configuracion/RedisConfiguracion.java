package alejandro.aplicacion.Infraestructura.Configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguracion {

    @Bean
    public RedisConnectionFactory conexionTipoLechuga(){
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration("localhost", 6379)
        );
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> operacionesRedis(RedisConnectionFactory conexionRedis){
        RedisTemplate<String, String> plantillaConexion = new RedisTemplate<>();
        plantillaConexion.setConnectionFactory(conexionRedis);
        plantillaConexion.setKeySerializer(new StringRedisSerializer());
        plantillaConexion.setValueSerializer(new StringRedisSerializer());
        return plantillaConexion;
    }
}
