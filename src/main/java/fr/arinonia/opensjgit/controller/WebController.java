package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController implements ILoggedController {

    private final UserService userService;

    @Autowired
    public WebController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getWebViw(final Model model) {
        final User currentUser = this.getCurrentUser(this.userService);
        model.addAttribute("creationDate", currentUser.getCreation_date());
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        model.addAttribute("userRank", currentUser.getRank().name());
        return "home";
    }
}
