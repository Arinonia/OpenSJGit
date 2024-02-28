package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.dto.UserRegistrationDto;
import fr.arinonia.opensjgit.service.UserService;
import fr.arinonia.opensjgit.service.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationDto userDto, RedirectAttributes redirectAttributes) {
        final Response response = userService.registerUser(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), userDto.getPasswordConfirm());

        if (!response.isSuccess()) {
            redirectAttributes.addFlashAttribute("errorMessage", response.getErrorMessage());
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
