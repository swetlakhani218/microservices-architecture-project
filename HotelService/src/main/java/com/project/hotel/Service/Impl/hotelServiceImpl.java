package com.project.hotel.Service.Impl;

import com.project.hotel.Entity.Hotel;
import com.project.hotel.Exception.ResourceNotFoundException;
import com.project.hotel.Repository.hotelServiceRepository;
import com.project.hotel.Service.hotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class hotelServiceImpl implements hotelService {
    @Autowired
    private hotelServiceRepository hotelServiceRepository;

    @Override
    public Hotel saveHotel(Hotel hotel) {
        String HotelId = UUID.randomUUID().toString();
        hotel.setId(HotelId);
        return hotelServiceRepository.save(hotel);
    }

    @Override
    public Hotel findHotelById(String id) {
        return hotelServiceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("hotel with given id not found"));
    }

    @Override
    public List<Hotel> findAllHotels() {
        return hotelServiceRepository.findAll();
    }

    @Override
    public void deleteHotelById(String id) {
        hotelServiceRepository.deleteById(id);
    }
}
