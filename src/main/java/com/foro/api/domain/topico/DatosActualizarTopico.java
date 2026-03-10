package com.foro.api.domain.topico;

// DTO para actualizar un tópico (campos opcionales)
public record DatosActualizarTopico(
        String titulo,
        String mensaje
) {
}
