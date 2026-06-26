package com.tourism.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ItineraryVO {
    private Long id;
    private String title;
    private String destination;
    private Integer days;
    private BigDecimal budget;
    private String preferences;
    private String content;
    private List<DayVO> daysDetail;
    private LocalDateTime createTime;

    public static class DayVO {
        private Integer dayNumber;
        private String attractions;
        private String hotels;
        private String restaurants;
        private String notes;
        public Integer getDayNumber() { return dayNumber; }
        public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
        public String getAttractions() { return attractions; }
        public void setAttractions(String attractions) { this.attractions = attractions; }
        public String getHotels() { return hotels; }
        public void setHotels(String hotels) { this.hotels = hotels; }
        public String getRestaurants() { return restaurants; }
        public void setRestaurants(String restaurants) { this.restaurants = restaurants; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<DayVO> getDaysDetail() { return daysDetail; }
    public void setDaysDetail(List<DayVO> daysDetail) { this.daysDetail = daysDetail; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
