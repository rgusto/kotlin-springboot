package br.com.alura.forum.controller

import br.com.alura.forum.dto.TopicoInput
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.model.Topico
import br.com.alura.forum.service.TopicoService
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
    fun listar(): List<TopicoView> {
        return service.listar();
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<TopicoView> {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Transactional
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
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody input: TopicoInput): ResponseEntity<TopicoView> {
        return ResponseEntity.ok(service.atualizar(id, input));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    fun excluir(@PathVariable id: Long) {
        return service.excluir(id);
    }

}