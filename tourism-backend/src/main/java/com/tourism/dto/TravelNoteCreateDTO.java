package com.tourism.dto;

import jakarta.validation.constraints.NotBlank;

public class TravelNoteCreateDTO {
    @NotBlank private String title;
    @NotBlank private String content;
    private String coverImage;
    private String destination;
    private String tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}
