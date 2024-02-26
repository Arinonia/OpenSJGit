package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    private final UserRepository userRepository;

    @Autowired
    public SettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/settings")
    public String getSettingsView(final Model model) {
        final User currentUser = userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        final String profilePicPath = (currentUser.getProfile_picture() != null || !currentUser.getProfile_picture().equalsIgnoreCase("")) ? currentUser.getProfile_picture() : "/images/default-pic.png";

        model.addAttribute("creationDate", currentUser.getCreation_date());
        model.addAttribute("profilePicPath", profilePicPath);
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("email", currentUser.getEmail());
        return "settings";
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
