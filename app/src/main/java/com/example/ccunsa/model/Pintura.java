package com.example.ccunsa.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "pinturas")
public class Pintura {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private String audio;

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getAudio() { return audio; }
    public void setAudio(String audio) { this.audio = audio; }
}
