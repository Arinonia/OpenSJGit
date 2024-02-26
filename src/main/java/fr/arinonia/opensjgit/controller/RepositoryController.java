package fr.arinonia.opensjgit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RepositoryController {
    @GetMapping("/create-repository")
    public String getCreateRepositoryView() {
        return "todo";
    }
}
