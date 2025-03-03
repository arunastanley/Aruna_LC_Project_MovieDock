package org.launchcode.moviedock.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.launchcode.moviedock.data.ApiMovieRepository;
import org.launchcode.moviedock.data.MovieRepository;
import org.launchcode.moviedock.data.ReviewRepository;
import org.launchcode.moviedock.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.launchcode.moviedock.models.dto.MovieHelper;

import java.util.Arrays;
import java.util.Optional;




@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("results")
    public String index(Model model){
        return "search";
    }


    @PostMapping("results")
    public String processSearchResults(@ModelAttribute @Valid Movie movie, Model model, @RequestParam String searchTerm) throws JsonProcessingException {

        model.addAttribute("title", "Movie Search Results");

        //get list of api ids
        MovieHelper mh = new MovieHelper();
        String[] listOfApiIds = mh.makeMovieList(searchTerm);
        Movie[] movies;

        //the way text parsing is currently working, 10 movies are being sent to listOfApiIds
        movies = new Movie[listOfApiIds.length];

        model.addAttribute("apis", listOfApiIds);


        for (int i = 0; i < listOfApiIds.length; i++){
            Movie movie1 = new Movie();
            movie1.setMovieInfoById(listOfApiIds[i]);

            movies[i] = movie1;


            String year = movie1.getYear();
            String title = movie1.getName();
            String apiId = movie1.getApiID();



        }

        //checks if movies is populated
        if (movies.length>0) {
            model.addAttribute("movies", movies);
        }
        else{
            model.addAttribute("error", "No movies found, please enter another movie title");
        }

        System.out.println(Arrays.toString(movies));


        return "search";
    }


    @GetMapping("movie-view/{apiId}")
    public String displayViewMovie(Model model, @PathVariable String apiId, @ModelAttribute @Valid Movie movie) throws JsonProcessingException{



        movie.setMovieInfoById(apiId);

        String year = movie.getYear();
        String title = movie.getName();
        String plot = movie.getPlot();
        String director = movie.getDirector();
        String poster = movie.getPoster();

        model.addAttribute("plot", plot);
        model.addAttribute("year", year);
        model.addAttribute("title", title);
        model.addAttribute("director", director);
        model.addAttribute("poster", poster);




        System.out.println(plot);
    if (plot!=null) {
        Optional<Movie> optMovie = movieRepository.findByApiID(movie.getApiID());
        if (optMovie.isPresent()) {
            System.out.println(movie.getApiID());
            Movie a = (Movie) optMovie.get();
            System.out.println("it exists");
            a.userView();
            movieRepository.save(a);

//            For adding review Link and diplaying reviews for the movie
            model.addAttribute("movie",a);
            model.addAttribute("reviews", reviewRepository.findReviewByMovieSortDescDate(a.getId()));

        }
        else{
            System.out.println("it doesn't exist");
            movie.userView();
            movieRepository.save(movie);

            //            For adding review Link and diplaying reviews for the movie
            model.addAttribute("movie",movie);

        }
    }

        return "movie-view";
    }





}