package com.tourism.dto;

import jakarta.validation.constraints.*;

public class ReviewCreateDTO {
    @NotBlank private String targetType;
    @NotNull private Long targetId;
    @Min(1) @Max(5) private Integer rating;
    @NotBlank @Size(max = 1000) private String content;
    private String images;

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
}
