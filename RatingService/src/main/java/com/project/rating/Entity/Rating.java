package com.project.rating.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("user_ratings")
public class Rating {
    @Id                            // in mongodb id's are by default auto generated
    private String ratingId;

    private String userId;
    private String hotelId;
    private int rating;
    private String  feedback;
}
