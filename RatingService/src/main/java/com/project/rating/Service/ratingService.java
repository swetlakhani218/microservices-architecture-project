package com.project.rating.Service;

import com.project.rating.Entity.Rating;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ratingService {

    //create
    Rating createRating(Rating rating);

    // get all ratings
    List<Rating> getAllRatings();

    //user wise ratings i.e get all by userId
    List<Rating> getAllRatingsByUserId(String userId);

    //hotel wise ratings i.e get all by hotelId
    List<Rating> getAllRatingsByHotelId(String hotelId);

}
