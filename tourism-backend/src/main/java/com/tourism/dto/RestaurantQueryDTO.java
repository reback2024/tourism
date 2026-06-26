package com.tourism.dto;

public class RestaurantQueryDTO {
    private String keyword;
    private String city;
    private String cuisineType;
    private String sortBy;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
