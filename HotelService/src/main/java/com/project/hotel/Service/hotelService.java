package com.project.hotel.Service;

import com.project.hotel.Entity.Hotel;

import java.util.List;

public interface hotelService {
    // create hotel
    Hotel saveHotel(Hotel hotel);

    // get a hotel
    Hotel findHotelById(String id);

    // get all hotels
    List<Hotel> findAllHotels();

    // delete hotel by id
    void deleteHotelById(String id);
}
