package br.com.alura.forum.service

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoInputMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Topico
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TopicoService(
    private var topicos: List<Topico> = ArrayList(),
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoInputMapper: TopicoInputMapper,
    private val notFoundMessage: String = "Tópico não encontrado"
) {

    fun listar(): List<TopicoView> {
        return topicos.stream().map { t ->
            topicoViewMapper.map(t)
        }.collect(Collectors.toList());
    }

    fun buscarPorId(id: Long): TopicoView {
        val topico = topicos.stream().filter { topico ->
            topico.id == id
        }.findFirst().orElseThrow{NotFoundException(notFoundMessage)};
        return topicoViewMapper.map(topico);
    }

    fun cadastrar(input: TopicoInput): TopicoView {
        val topico = topicoInputMapper.map(input);
        topico.id = topicos.size.toLong() + 1;
        topicos = topicos.plus(topico);
        return topicoViewMapper.map(topico);
    }

    fun atualizar(id: Long, input: TopicoInput): TopicoView {
        var topico = topicos.stream().filter({ t ->
            t.id == id
        }).findFirst().orElseThrow{NotFoundException(notFoundMessage)};
        val topicoAtualizado = Topico(
            id = id,
            titulo = input.titulo,
            mensagem = input.mensagem,
            autor = topico.autor,
            curso = topico.curso,
            respostas = topico.respostas,
            status = topico.status,
            dataCriacao = topico.dataCriacao
        )
        topicos = topicos.minus(topico).plus(topicoAtualizado);
        return topicoViewMapper.map(topicoAtualizado);
    }

    fun excluir(id: Long) {
        val topico = topicos.stream().filter { topico ->
            topico.id == id
        }.findFirst().orElseThrow{NotFoundException(notFoundMessage)};
        topicos = topicos.minus(topico);
    }

}