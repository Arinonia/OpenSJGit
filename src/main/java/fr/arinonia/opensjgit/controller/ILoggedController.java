package fr.arinonia.opensjgit.controller;

import org.springframework.security.core.context.SecurityContextHolder;

public interface ILoggedController {

    default String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
