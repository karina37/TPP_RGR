package ttp.lab3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public String showGenrePage(Model model) {
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveGenre(@ModelAttribute Genres genre, Model model) {
        String result = genreService.saveGenre(genre);
        model.addAttribute("message", result);
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres";
    }

    @PostMapping("/search")
    public String searchGenre(@RequestParam("searchId") int id, Model model) {
        Genres genre = genreService.findGenreById(id);
        if (genre != null) {
            model.addAttribute("genre", genre);
            model.addAttribute("message", "Genre found!");
        } else {
            model.addAttribute("error", "Genre with ID " + id + " not found.");
        }
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres";
    }

    // Handle genre search by name
    @PostMapping("/search/name")
    public String searchGenreByName(@RequestParam("searchName") String name, Model model) {
        List<Genres> foundGenres = genreService.findGenresByName(name);
        if (!foundGenres.isEmpty()) {
            model.addAttribute("genres", foundGenres);
            model.addAttribute("message", "Found " + foundGenres.size() + " genres matching '" + name + "'");
        } else {
            model.addAttribute("error", "No genres found matching '" + name + "'");
            model.addAttribute("genres", genreService.getAllGenres());
        }
        return "genres";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteGenre(@RequestParam("deleteId") int id, Model model) {
        String result = genreService.deleteGenre(id);
        if (result.contains("successfully")) {
            model.addAttribute("message", result);
        } else {
            model.addAttribute("error", result);
        }
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres";
    }

    // Handle exceptions
    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        model.addAttribute("error", "An error occurred: " + e.getMessage());
        model.addAttribute("genres", genreService.getAllGenres());
        return "genres";
    }
}
