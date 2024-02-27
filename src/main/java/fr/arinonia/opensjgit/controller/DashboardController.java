package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DashboardController implements ILoggedController {

    private final UserService userService;

    @Autowired
    public DashboardController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(final Model model) {
        final User currentUser = userService.findByUsername(getCurrentUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        final String profilePicPath = (currentUser.getProfile_picture() != null || !currentUser.getProfile_picture().equalsIgnoreCase("")) ? currentUser.getProfile_picture() : "/images/default-pic.png";
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("profilePicPath", profilePicPath);
        return "dashboard";
    }

    @GetMapping("/edit-user/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/edit-user/{id}")
    public String updateUser(@PathVariable("id") Long id, User updatedUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            updatedUser.setId(id);
            return "edit-user";
        }
        final User existingUser = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        updatedUser.setPassword(existingUser.getPassword());
        updatedUser.setCreation_date(existingUser.getCreation_date());
        updatedUser.setEmail(existingUser.getEmail());

        userService.save(updatedUser);
        model.addAttribute("users", userService.findAllUsers());
        return "redirect:/dashboard";
    }
}
