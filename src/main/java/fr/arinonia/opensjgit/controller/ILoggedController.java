package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.UserRepository;
import fr.arinonia.opensjgit.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

public interface ILoggedController {

    default String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Deprecated
    default User getCurrentUser(final UserRepository userRepository) {
        return userRepository.findByUsername(getCurrentUsername()).orElseThrow(() -> new IllegalArgumentException("User not found with username '" + getCurrentUsername() + "'"));
    }

    default User getCurrentUser(final UserService userService) {
        return userService.findByUsername(getCurrentUsername()).orElseThrow(() -> new IllegalArgumentException("User not found with username '" + getCurrentUsername() + "'"));
    }

    default String getProfilePicturePath(final UserService userService) {
        final User user = getCurrentUser(userService);
        return  (user.getProfile_picture() != null || !user.getProfile_picture().equalsIgnoreCase("")) ? user.getProfile_picture() : "/images/default-pic.png";
    }
}
