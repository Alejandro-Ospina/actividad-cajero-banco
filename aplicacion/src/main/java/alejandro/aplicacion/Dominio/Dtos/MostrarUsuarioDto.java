package alejandro.aplicacion.Dominio.Dtos;

import alejandro.aplicacion.Dominio.Modelos.Usuario;

public record MostrarUsuarioDto(
        Integer id,
        String nombre,
        String identificacion,
        Integer edad,
        String nacionalidad,
        String genero
) {
    public MostrarUsuarioDto(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getIdentificacion(),
                usuario.getEdad(),
                usuario.getNacionalidad(),
                usuario.getGenero()
        );
    }
}
