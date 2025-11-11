package com.project.hotel.Repository;

import com.project.hotel.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface hotelServiceRepository extends JpaRepository<Hotel, String> {

}
