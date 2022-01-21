package br.com.alura.forum.service

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoInputMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Topico
import br.com.alura.forum.repository.TopicoRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
class TopicoService(
    private var repository: TopicoRepository,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoInputMapper: TopicoInputMapper,
    private val notFoundMessage: String = "Tópico não encontrado"
) {

    fun listar(): List<TopicoView> {
        return repository.findAll().stream().map { t ->
            topicoViewMapper.map(t)
        }.collect(Collectors.toList());
    }

    fun buscarPorId(id: Long): TopicoView {
        val topico = repository.findById(id).orElseThrow { NotFoundException(notFoundMessage) };
        return topicoViewMapper.map(topico);
    }

    fun cadastrar(input: TopicoInput): TopicoView {
        val topico = topicoInputMapper.map(input);
        repository.save(topico);
        return topicoViewMapper.map(topico);
    }

    fun atualizar(id: Long, input: TopicoInput): TopicoView {
        var topico = repository.findById(id).orElseThrow { NotFoundException(notFoundMessage) };
        topico.titulo = input.titulo;
        topico.mensagem = input.mensagem;
        return topicoViewMapper.map(topico);
    }

    fun excluir(id: Long) {
        repository.deleteById(id);
    }

}