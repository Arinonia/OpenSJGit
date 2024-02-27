package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.service.UserService;
import fr.arinonia.opensjgit.service.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class SettingsController implements ILoggedController {

    private final UserService userService;

    @Autowired
    public SettingsController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String getSettingsView(final Model model) {
        final User currentUser = userService.findByUsername(getCurrentUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        final String profilePicPath = (currentUser.getProfile_picture() != null || !currentUser.getProfile_picture().equalsIgnoreCase("")) ? currentUser.getProfile_picture() : "/images/default-pic.png";
        System.out.println(profilePicPath);
        model.addAttribute("creationDate", currentUser.getCreation_date());
        model.addAttribute("profilePicPath", profilePicPath);
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("email", currentUser.getEmail());
        model.addAttribute("mail_confirmed", currentUser.isConfirmed_mail());
        return "settings";
    }

    @PostMapping("/updateUserSettings")
    public String updateProfile(@RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                @RequestParam(value = "currentPassword", required = false) String currentPassword,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                @RequestParam(value = "confirmNewPassword", required = false) String confirmNewPassword,
                                RedirectAttributes redirectAttributes) {

        boolean profileUpdated = false;

        if (profilePicture != null && !profilePicture.isEmpty()) {
            final Response response = this.userService.updateProfilePicture(getCurrentUsername(), profilePicture);
            if (response.isSuccess()) {
                profileUpdated = true;
            } else {
                redirectAttributes.addFlashAttribute("error", response.getErrorMessage());
                return "redirect:/settings";
            }
        }


        if (newPassword != null && !newPassword.isEmpty() && confirmNewPassword != null && !confirmNewPassword.isEmpty()) {
            final Response response = this.userService.updatePassword(getCurrentUsername(), currentPassword, newPassword, confirmNewPassword);

            if (response.isSuccess()) {
                profileUpdated = true;
            } else {
                redirectAttributes.addFlashAttribute("error", response.getErrorMessage());
                return "redirect:/settings";
            }
        }
        if (!profileUpdated) {
            redirectAttributes.addFlashAttribute("message", "No update was performed. Please provide new information.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Your settings have been updated.");
        }
        return "redirect:/settings";
    }
}
