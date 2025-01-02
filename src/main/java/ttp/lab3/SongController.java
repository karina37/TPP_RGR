package ttp.lab3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping
    public String showSongPage(Model model) {
        model.addAttribute("songs", songService.getAllSongs());
        return "songs";
    }

    @GetMapping("/add")
    public String addSong() {
        return "songs";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveSong(@ModelAttribute Song song, Model model) {
        String result = songService.saveSong(song);
        model.addAttribute("message", result);
        model.addAttribute("songs", songService.getAllSongs());
        return "songs";
    }

    @PostMapping("/search")
    public String searchSong(@RequestParam("searchId") int id, Model model) {
        Song song = songService.findSongById(id);
        if (song != null) {
            model.addAttribute("song", song);
            model.addAttribute("message", "Song found!");
        } else {
            model.addAttribute("error", "Song with ID " + id + " not found.");
        }
        model.addAttribute("songs", songService.getAllSongs());
        return "songs";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteSong(@RequestParam("deleteId") int id, Model model) {
        String result = songService.deleteSong(id);
        if (result.contains("successfully")) {
            model.addAttribute("message", result);
        } else {
            model.addAttribute("error", result);
        }
        model.addAttribute("songs", songService.getAllSongs());
        return "songs";
    }
}
