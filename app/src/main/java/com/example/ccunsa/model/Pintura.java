package com.example.ccunsa.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pinturas")
public class Pintura {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String paintingName;
    private String authorName;
    private String description;
    private String galleryName;
    private String iconPath;
    private String audioPath;

    // Constructor con ID (para asignación manual)
    public Pintura(int id, String paintingName, String authorName, String description, String galleryName, String iconPath, String audioPath) {
        this.id = id;
        this.paintingName = paintingName;
        this.authorName = authorName;
        this.description = description;
        this.galleryName = galleryName;
        this.iconPath = iconPath;
        this.audioPath = audioPath;
    }

    // Constructor sin ID (para asignación automática)
    @Ignore
    public Pintura(String paintingName, String authorName, String description, String galleryName, String iconPath, String audioPath) {
        this.paintingName = paintingName;
        this.authorName = authorName;
        this.description = description;
        this.galleryName = galleryName;
        this.iconPath = iconPath;
        this.audioPath = audioPath;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaintingName() {
        return paintingName;
    }

    public void setPaintingName(String paintingName) {
        this.paintingName = paintingName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioFileName() {
        return audioPath;
    }
}
