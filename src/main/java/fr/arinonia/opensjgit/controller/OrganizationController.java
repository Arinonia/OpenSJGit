package fr.arinonia.opensjgit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrganizationController {
    @GetMapping("/create-organization")
    public String getCreateOrganizationView() {
        return "todo";
    }
}
