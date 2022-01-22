package br.com.alura.forum.service

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.dto.TopicoPorCategoriaDto
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoInputMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.repository.TopicoRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TopicoService(
    private var repository: TopicoRepository,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoInputMapper: TopicoInputMapper,
    private val notFoundMessage: String = "Tópico não encontrado"
) {

    fun listar(nomeCurso: String?, paginacao: Pageable): Page<TopicoView> {
        val topicos = if (nomeCurso.isNullOrBlank()) {
            repository.findAll(paginacao);
        } else {
            repository.findByCursoNome(nomeCurso, paginacao);
        }
        return topicos.map { t ->
            topicoViewMapper.map(t)
        };
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

    fun relatorio(): List<TopicoPorCategoriaDto> {
        return repository.relatorio();
    }

}