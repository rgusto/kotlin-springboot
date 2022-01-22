package br.com.alura.forum.controller

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.dto.TopicoPorCategoriaDto
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.service.TopicoService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping("/topicos")
class TopicoController(
    private val service: TopicoService
) {

    @GetMapping
    @Cacheable("topicos")
    fun listar(
        @RequestParam(required = false) nomeCurso: String?,
        @PageableDefault(
            size = 5, sort = ["dataCriacao"], direction = Sort.Direction.DESC
        ) paginacao: Pageable
    ): Page<TopicoView> {
        return service.listar(nomeCurso, paginacao);
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<TopicoView> {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = ["topicos"], allEntries = true)
    fun cadastrar(
        @Valid @RequestBody input: TopicoInput,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<TopicoView> {
        val topicoView = service.cadastrar(input);
        val uri = uriComponentsBuilder.path("/topicos/${topicoView.id}").build().toUri();
        return ResponseEntity.created(uri).body(topicoView);
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = ["topicos"], allEntries = true)
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody input: TopicoInput): ResponseEntity<TopicoView> {
        return ResponseEntity.ok(service.atualizar(id, input));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = ["topicos"], allEntries = true)
    fun excluir(@PathVariable id: Long) {
        return service.excluir(id);
    }

    @GetMapping("/relatorio")
    fun relatorio(): List<TopicoPorCategoriaDto> {
        return service.relatorio();
    }


}