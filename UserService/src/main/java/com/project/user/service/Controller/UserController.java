package com.project.user.service.Controller;

import com.project.user.service.Entity.User;
import com.project.user.service.Service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User user1 =  userService.createUser(user);
        return ResponseEntity. status(HttpStatus.CREATED).body(user1);
    }

    int retryCount = 1;

    //get single user
    @GetMapping("/{userId}")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "rateLimitFallback")
//     @CircuitBreaker(name="ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
   @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        User user1 = userService.getUserById(userId);
        logger.info("Retry Count: {}",retryCount);
        retryCount++;
        return ResponseEntity.ok(user1);
    }

    // fallback method must have same parameters + one extra for exception
    public ResponseEntity<User> rateLimitFallback(String userId, Exception ex) {
        logger.warn("Rate limit exceeded for userId: {}", userId, ex);

        User fallbackUser = new User();
        fallbackUser.setUserId("000");
        fallbackUser.setName("Dummy User");
        fallbackUser.setEmail("dummy@example.com");
        fallbackUser.setAbout("Rate limit exceeded — please try again later.");

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(fallbackUser);
    }

    // creating fallback method for circuitbreaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
        logger.info("Fallback is executed as Service is down  : ",ex.getMessage());
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .userId("132232")
                .about("This user is dummy user because some service is down.").build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    // get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }


}
