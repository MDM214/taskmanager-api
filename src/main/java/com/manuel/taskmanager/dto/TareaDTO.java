package com.manuel.taskmanager.dto;

public class TareaDTO {

    private Long id;
    private String titulo;
    private String estado;

    public TareaDTO() {
    }

    public TareaDTO(long id,
            String titulo,
            String estado) {

        this.id = id;
        this.titulo = titulo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEstado() {
        return estado;
    }
}
