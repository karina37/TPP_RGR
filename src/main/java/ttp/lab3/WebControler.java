package ttp.lab3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebControler{

    @GetMapping("/")
    public String main() {
        return "home";
    }

    @GetMapping("/groups/add")
    public String addGroup() {
        return "groups";
    }

    @GetMapping("/genres/add")
    public String addGenre() {
        return "genres";
    }

    @GetMapping("/songs/add")
    public String addSong() {
        return "songs";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

