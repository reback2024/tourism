package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.common.Result;
import com.tourism.entity.*;
import com.tourism.mapper.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final UserMapper userMapper;
    private final AttractionMapper aMapper;
    private final HotelMapper hMapper;
    private final RestaurantMapper rMapper;
    private final ReviewMapper revMapper;
    private final TravelNoteMapper nMapper;

    public AdminDashboardController(UserMapper u, AttractionMapper a, HotelMapper h,
                                     RestaurantMapper r, ReviewMapper rv, TravelNoteMapper n) {
        this.userMapper = u; this.aMapper = a; this.hMapper = h;
        this.rMapper = r; this.revMapper = rv; this.nMapper = n;
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userMapper.selectCount(null));
        data.put("attractionCount", aMapper.selectCount(new LambdaQueryWrapper<Attraction>().eq(Attraction::getStatus, 1)));
        data.put("hotelCount", hMapper.selectCount(new LambdaQueryWrapper<Hotel>().eq(Hotel::getStatus, 1)));
        data.put("restaurantCount", rMapper.selectCount(new LambdaQueryWrapper<Restaurant>().eq(Restaurant::getStatus, 1)));
        data.put("reviewCount", revMapper.selectCount(null));
        data.put("travelNoteCount", nMapper.selectCount(new LambdaQueryWrapper<TravelNote>().eq(TravelNote::getStatus, 1)));
        return Result.ok(data);
    }
}
