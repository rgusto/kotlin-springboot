package br.com.alura.forum.mapper

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.model.Topico
import br.com.alura.forum.service.CursoService
import br.com.alura.forum.service.UsuarioService
import org.springframework.stereotype.Component

@Component
class TopicoInputMapper(
    private var cursoService: CursoService,
    private var usuarioService: UsuarioService
) : Mapper<TopicoInput, Topico> {

    override fun map(t: TopicoInput): Topico {
        return Topico(
            titulo = t.titulo,
            mensagem = t.mensagem,
            curso = cursoService.buscarPorId(t.idCurso),
            autor = usuarioService.buscarPorId(t.idCurso)
        )
    }

}
