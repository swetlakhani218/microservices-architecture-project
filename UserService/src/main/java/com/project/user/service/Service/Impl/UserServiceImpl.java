package com.project.user.service.Service.Impl;

import com.project.user.service.Entity.Hotel;
import com.project.user.service.Entity.Rating;
import com.project.user.service.Entity.User;
import com.project.user.service.Expection.ResourceNotFoundException;
import com.project.user.service.ExternalServices.HotelService;
import com.project.user.service.Repository.UserRepository;
import com.project.user.service.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User createUser(User user) {
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        // get user by userId using userRepository
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given ID is not found "+userId));

        // fetch rating of above user from rating service
        // http://localhost:8083/ratings/users/{userId}  calling this api we will get ratings of this user
        // whenever one service calls another service then it happens by http client
        
        // one of the client is classic blocking which is RestTemplate
        // another one is non blocking Reactive WebClient
        // another one is Apache HttpClient

        //another one is Feign Client  -> HTTP web client by netflix
        //another one is rest client


        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        logger.info("{}",ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();


        // writing interceptor logic directly in code

//        // Step 2️⃣: Extract JWT token from SecurityContext
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String token = null;
//        if (authentication != null && authentication.getCredentials() != null) {
//            token = authentication.getCredentials().toString();
//        }
//
//        logger.info("🪪 Forwarding token to Rating Service: {}", token != null);
//
//        // Step 3️⃣: Set up headers with Authorization
//        HttpHeaders headers = new HttpHeaders();
//        if (token != null) {
//            headers.set("Authorization", "Bearer " + token);
//        }
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        // Step 4️⃣: Make REST call with headers
//        ResponseEntity<Rating[]> ratingResponse = restTemplate.exchange(
//                "http://RATING-SERVICE/ratings/users/" + user.getUserId(),
//                HttpMethod.GET,
//                entity,
//                Rating[].class
//        );
//
//        Rating[] ratingsOfUser = ratingResponse.getBody();
//        logger.info("🎯 Ratings fetched from Rating Service: {}", (Object) ratingsOfUser);
//
//        List<Rating> ratings = Arrays.asList(ratingsOfUser);

        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api to hotel service to get the hotel

//            ResponseEntity<Hotel> forEntity =  restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
//            Hotel hotel = forEntity.getBody();
//            logger.info("Response Status Code:{}",forEntity.getStatusCode());

            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            //set the hotel to rating
            rating.setHotel(hotel);

            //return the rating
            return rating;
        }).collect(Collectors.toList());

         user.setRatings(ratingList);

        return user;
    }

    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void updateUser(User user) {

    }
}
