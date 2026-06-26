package com.tourism.dto;

public class AttractionQueryDTO {

    private String keyword;
    private String city;
    private String category;
    private String sortBy;
    private String order;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }
}
