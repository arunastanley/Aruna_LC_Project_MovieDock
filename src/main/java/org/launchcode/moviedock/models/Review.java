package org.launchcode.moviedock.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


@Entity
public class Review extends AbstractEntity{

    @ManyToOne
    @NotNull(message= "select movie to review")
    private Movie movie;


    @ManyToOne
    @NotNull
    private AppUser user;

    @NotBlank(message = "Review cannot be empty")
    @Size(max = 500, message = "Review cannot exceed 500 characters")
    private String review_text;

    //    provide a dropdown to select options from 1-5
    @NotNull
    private int star_rating;

    private LocalDateTime dateTime;

    public Review(Movie movie, AppUser user, String review_text, int star_rating) {
        this.movie = movie;
        this.user = user;
        this.review_text = review_text;
        this.star_rating = star_rating;
        this.dateTime = LocalDateTime.now();
    }

    public Review() {
    }

    public Movie getMovie() {
        return movie;
    }

    public AppUser getUser() {
        return user;
    }

    public String getReview_text() {
        return review_text;
    }

    public int getStar_rating() {
        return star_rating;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public void setStar_rating(int star_rating) {
        this.star_rating = star_rating;
    }

    public void setDateTime() {
        this.dateTime = LocalDateTime.now();;
    }
}
