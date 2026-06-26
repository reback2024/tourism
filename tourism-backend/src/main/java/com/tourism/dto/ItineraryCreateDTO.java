package com.tourism.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class ItineraryCreateDTO {
    @NotBlank private String title;
    @NotBlank private String destination;
    @NotNull private Integer days;
    private BigDecimal budget;
    private String preferences;
    private String content;
    private List<DayDTO> daysDetail;

    public static class DayDTO {
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
    public List<DayDTO> getDaysDetail() { return daysDetail; }
    public void setDaysDetail(List<DayDTO> daysDetail) { this.daysDetail = daysDetail; }
}
