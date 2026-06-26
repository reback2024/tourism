package com.tourism.dto;

import java.math.BigDecimal;

public class HotelQueryDTO {
    private String keyword;
    private String city;
    private Integer starLevel;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private String sortBy;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getStarLevel() { return starLevel; }
    public void setStarLevel(Integer starLevel) { this.starLevel = starLevel; }
    public BigDecimal getPriceMin() { return priceMin; }
    public void setPriceMin(BigDecimal priceMin) { this.priceMin = priceMin; }
    public BigDecimal getPriceMax() { return priceMax; }
    public void setPriceMax(BigDecimal priceMax) { this.priceMax = priceMax; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
