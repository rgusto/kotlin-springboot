package br.com.alura.forum.repository

import br.com.alura.forum.dto.TopicoPorCategoriaDto
import br.com.alura.forum.model.Topico
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TopicoRepository : JpaRepository<Topico, Long> {

    fun findByCursoNome(nomeCurso: String, paginacao: Pageable): Page<Topico>;

    @Query("Select new br.com.alura.forum.dto.TopicoPorCategoriaDto(curso.categoria, count(t)) " +
            "from Topico t join t.curso curso group by curso.categoria")
    fun relatorio(): List<TopicoPorCategoriaDto>

}