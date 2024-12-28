package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String showGroupPage(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveGroup(@ModelAttribute Group group, Model model) {
        // Встановлюємо значення за замовчуванням для memberCount, якщо воно null
        if (group.memberCount() == null) {
            group = new Group(group.id(), group.name(), 1); // Значення за замовчуванням: 1
        }

        String result = groupService.saveGroup(group);
        model.addAttribute("message", result);
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }

    @PostMapping("/search")
    public String searchGroup(@RequestParam("searchId") int id, Model model) {
        Group group = groupService.findGroupById(id);
        if (group != null) {
            model.addAttribute("group", group);
            model.addAttribute("message", "Групу знайдено!");
        } else {
            model.addAttribute("error", "Групу з ID " + id + " не знайдено.");
        }
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteGroup(@RequestParam("deleteId") int id, Model model) {
        String result = groupService.deleteGroup(id);
        if (result.contains("успішно")) {
            model.addAttribute("message", result);
        } else {
            model.addAttribute("error", result);
        }
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        model.addAttribute("error", "Виникла помилка: " + e.getMessage());
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }
}
