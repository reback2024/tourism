package com.tourism.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("itinerary_day")
public class ItineraryDay {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itineraryId;
    private Integer dayNumber;
    private String attractions;
    private String hotels;
    private String restaurants;
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getItineraryId() { return itineraryId; }
    public void setItineraryId(Long itineraryId) { this.itineraryId = itineraryId; }
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
