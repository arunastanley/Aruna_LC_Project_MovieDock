package org.launchcode.moviedock.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.launchcode.moviedock.data.AppUserRepository;
import org.launchcode.moviedock.data.MovieRepository;
import org.launchcode.moviedock.data.ReviewRepository;
import org.launchcode.moviedock.models.AppUser;
import org.launchcode.moviedock.models.Movie;
import org.launchcode.moviedock.models.dto.UserMovieDTO;
import org.launchcode.moviedock.security.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PrincipalService principalService;


    @GetMapping("/movies/add-favorite-movie")
    public String displayAddFavoriteMovieForm(@RequestParam Integer movieId, Model model) {

        model.addAttribute("title", "Add a Favorite Movie");

        AppUser user = principalService.getPrincipal();
        Optional<Movie> optMovie = movieRepository.findById(movieId);

        if (optMovie.isPresent()) {
            Movie movie = (Movie) optMovie.get();
            UserMovieDTO userMovieDTO = new UserMovieDTO();
            userMovieDTO.setMovie(movie);
            userMovieDTO.setUser(user);
            model.addAttribute("movie", movie);
            model.addAttribute("userMovieDTO", userMovieDTO);
            boolean isFavorite = user.getFavoriteMovies().contains(movie);
            model.addAttribute("isFavorite", isFavorite);
            model.addAttribute("favorite", "Movie already added in Favorite Movies list");
            return "/movies/add-favorite-movie";
        } else {
            model.addAttribute("title", "My Profile");
            return "redirect:/profile";
        }
    }


    @PostMapping("/movies/add-favorite-movie")
    public String processAddFavoriteMovieForm(@ModelAttribute @Valid UserMovieDTO userMovieDTO, Errors errors, Model model) {
        Movie movie = userMovieDTO.getMovie();
        AppUser user = principalService.getPrincipal();
        if (!errors.hasErrors()) {
            if (movie != null) {
                user.addFavoriteMovies(movie);
                appUserRepository.save(user);
                model.addAttribute("user", user);
                boolean isFavorite = user.getFavoriteMovies().contains(movie);
                model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
                model.addAttribute("isFavorite", !isFavorite);
                model.addAttribute("title", "My Profile");
                return "user/profile";
            }
        } else {
            model.addAttribute("user", user);
            model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
        }
        model.addAttribute("title", "My Profile");
        return "user/profile";
    }

    @GetMapping("/movies/add-to-watch-movie")
    public String displayToWatchMovieForm(@RequestParam Integer movieId, Model model) {

        AppUser user = principalService.getPrincipal();
        Optional<Movie> optMovie = movieRepository.findById(movieId);

        if (optMovie.isPresent()) {
            Movie movie = (Movie) optMovie.get();
            UserMovieDTO userMovieDTO = new UserMovieDTO();
            userMovieDTO.setMovie(movie);
            userMovieDTO.setUser(user);
            model.addAttribute("movie", movie);
            model.addAttribute("userMovieDTO", userMovieDTO);
            boolean isToWatch = user.getToWatchMovies().contains(movie);
            model.addAttribute("isToWatch", isToWatch);
            model.addAttribute("toWatch", "Movie already added in To-Watch Movies list");
            model.addAttribute("title", "Add To Watch Movie");
            return "/movies/add-to-watch-movie";
        } else {
            model.addAttribute("title", "My Profile");
            return "redirect:/profile";
        }
    }

    @PostMapping("/movies/add-to-watch-movie")
    public String processToWatchMovieForm(@ModelAttribute @Valid UserMovieDTO userMovieDTO, Errors errors, Model model) {
        Movie movie = userMovieDTO.getMovie();
        AppUser user = principalService.getPrincipal();
        if (!errors.hasErrors()) {
            if (movie != null) {
                user.addToWatchMovies(movie);
                appUserRepository.save(user);
                model.addAttribute("user", user);
                model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
                boolean isToWatch = user.getToWatchMovies().contains(movie);
                model.addAttribute("isToWatch", !isToWatch);
                model.addAttribute("title", "My Profile");
                return "user/profile";
            }
        } else {
            model.addAttribute("user", user);
            model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
        }
        return "user/profile";
    }




    @DeleteMapping("/movies/favorite-movie/{movieId}")
    public String deleteMovieFromFavoriteList(@PathVariable int movieId, Model model) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        AppUser user = principalService.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
        user.removeFavoriteMovie(movie.get());
        appUserRepository.save(user);
        model.addAttribute("title", "My Profile");
        return "user/profile";
    }

    @DeleteMapping("/movies/to-watch-movie/{movieId}")
    public String deleteMovieFromToWatchList(@PathVariable int movieId, Model model) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        AppUser user = principalService.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("reviews", reviewRepository.findReviewSortDescDate(user.getId()));
        user.removeToWatchMovie(movie.get());
        appUserRepository.save(user);
        model.addAttribute("title", "My Profile");
        return "user/profile";
    }
}
