package org.launchcode.moviedock.data;

import org.launchcode.moviedock.models.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {



    @Query(value ="SELECT * FROM review WHERE movie_id = :movieId AND user_id = :userId",nativeQuery = true)
    Review findByUserIdAndMovieId(Integer movieId, Integer userId);

    @Query(value ="SELECT * FROM review order by date_time desc LIMIT 4",nativeQuery = true)
    List<Review> findTopFour();

    @Query(value ="SELECT * FROM review where user_id = :userId order by date_time desc ",nativeQuery = true)
    List<Review> findReviewSortDescDate(Integer userId);



//    @Query(value ="SELECT * FROM review WHERE user_id = :userId",nativeQuery = true)
//    Iterable<Review> findByUserId(Integer userId);

//    @Query(value ="SELECT movie_id FROM review WHERE user_id = :userId",nativeQuery = true)
//    Iterable<Integer> findMovieByUserId(Integer userId);

}
