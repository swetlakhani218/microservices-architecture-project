package com.project.hotel.Controller;

import com.project.hotel.Entity.Hotel;
import com.project.hotel.Service.Impl.hotelServiceImpl;
import com.project.hotel.Service.hotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class hotelServiceController {
    @Autowired
    private hotelService hotelService;

    //create
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.saveHotel(hotel));
    }


    // get single
    @GetMapping("/{Id}")
    public ResponseEntity<Hotel> createHotel(@PathVariable String Id) {
        return ResponseEntity.status(HttpStatus.OK).body(hotelService.findHotelById(Id));
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels() {
        return ResponseEntity.ok(hotelService.findAllHotels());
    }


    // get all
}
