package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.Repository;
import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.service.RepositoryService;
import fr.arinonia.opensjgit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController implements ILoggedController {

    private final UserService userService;
    private final RepositoryService repositoryService;

    @Autowired
    public DashboardController(final UserService userService, final RepositoryService repositoryService) {
        this.userService = userService;
        this.repositoryService = repositoryService;
    }


    @GetMapping("/dashboard")
    public String dashboard(final Model model) {
        final List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        return "dashboard";
    }

    @GetMapping("/edit-user/{id}")
    public String showEditUserForm(final @PathVariable("id") Long id, final Model model) {
        final User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        final List<Repository> repositories = this.repositoryService.findAllByOwner(user);
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        model.addAttribute("user", user);
        model.addAttribute("repositories", repositories);
        return "edit-user";
    }

    @PostMapping("/edit-user/{id}")
    public String updateUser(final @PathVariable("id") Long id, final User updatedUser, final BindingResult result, final Model model) {
        if (result.hasErrors()) {
            updatedUser.setId(id);
            return "edit-user";
        }
        final User existingUser = this.userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        updatedUser.setPassword(existingUser.getPassword());
        updatedUser.setCreation_date(existingUser.getCreation_date());
        updatedUser.setEmail(existingUser.getEmail());

        this.userService.save(updatedUser);
        model.addAttribute("users", this.userService.findAllUsers());
        return "redirect:/dashboard";
    }
}
