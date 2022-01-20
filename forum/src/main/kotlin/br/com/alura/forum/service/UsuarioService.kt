package br.com.alura.forum.service

import br.com.alura.forum.model.Curso
import br.com.alura.forum.model.Usuario
import org.springframework.stereotype.Service
import java.util.*

@Service
class UsuarioService(var usuarios: List<Usuario>) {

    init {
        val autor = Usuario(
            id = 1,
            nome = "Ana Silva",
            email = "bla@bla.com"
        );
        usuarios = Arrays.asList(autor);
    }

    fun buscarPorId(id: Long): Usuario {
        return usuarios.stream().filter({
                usuario -> usuario.id == id
        }).findFirst().get();
    }

}
