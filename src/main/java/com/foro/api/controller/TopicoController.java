package com.foro.api.controller;

import com.foro.api.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    // ─────────────────────────────────────────────
    // POST /topicos — Crear nuevo tópico
    // ─────────────────────────────────────────────
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriComponentsBuilder) {

        // Validar duplicados
        if (topicoRepository.existsByTitulo(datos.titulo())) {
            return ResponseEntity.badRequest().build();
        }
        if (topicoRepository.existsByMensaje(datos.mensaje())) {
            return ResponseEntity.badRequest().build();
        }

        var topico = new Topico(datos);
        topicoRepository.save(topico);

        URI url = uriComponentsBuilder
                .path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(url).body(new DatosRespuestaTopico(topico));
    }

    // ─────────────────────────────────────────────
    // GET /topicos — Listar todos los tópicos (paginado)
    // ─────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion) {

        var page = topicoRepository.findAll(paginacion)
                .map(DatosListadoTopico::new);

        return ResponseEntity.ok(page);
    }

    // ─────────────────────────────────────────────
    // GET /topicos/{id} — Detalle de un tópico
    // ─────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornarDatosTopico(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .map(topico -> ResponseEntity.ok(new DatosRespuestaTopico(topico)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────────
    // PUT /topicos/{id} — Actualizar título y mensaje
    // ─────────────────────────────────────────────
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos) {

        return topicoRepository.findById(id)
                .map(topico -> {
                    topico.actualizarDatos(datos);
                    return ResponseEntity.ok(new DatosRespuestaTopico(topico));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────────
    // DELETE /topicos/{id} — Eliminar un tópico
    // ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
