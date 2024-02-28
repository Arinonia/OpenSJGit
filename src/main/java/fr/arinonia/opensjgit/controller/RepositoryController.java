package fr.arinonia.opensjgit.controller;

import fr.arinonia.opensjgit.entity.Repository;
import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.service.RepositoryService;
import fr.arinonia.opensjgit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/repositories")
public class RepositoryController implements ILoggedController {

    private final UserService userService;
    private final RepositoryService repositoryService;


    @Autowired
    public RepositoryController(final UserService userService, final RepositoryService repositoryService) {
        this.userService = userService;
        this.repositoryService = repositoryService;
    }

    @GetMapping("/repositories")
    public String listUserRepositories(final Model model, final @RequestParam(defaultValue = "0") int page) {
        final Pageable pageable = PageRequest.of(page, 20);
        final User currentUser = this.getCurrentUser(this.userService);
        final Page<Repository> repositoryPage = repositoryService.findAllByUser(currentUser, pageable);

        model.addAttribute("repositories", repositoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", repositoryPage.getTotalPages());
        model.addAttribute("creationDate", currentUser.getCreation_date());
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        model.addAttribute("userRank", currentUser.getRank().name());
        return "repositories";
    }

    @GetMapping("/create-repository")
    public String getCreateRepositoryView(final Model model) {
        final User currentUser = this.getCurrentUser(this.userService);
        model.addAttribute("creationDate", currentUser.getCreation_date());
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        model.addAttribute("userRank", currentUser.getRank().name());
        model.addAttribute("repository", new Repository());
        return "create-repository";
    }

    @GetMapping("/edit-repository/{id}")
    public String showEditForm(final @PathVariable("id") Long id, final Model model) {
        final Repository repository = repositoryService.findById(id).orElseThrow();
        model.addAttribute("profilePicPath", this.getProfilePicturePath(this.userService));
        model.addAttribute("repository", repository);
        return "edit-repository";
    }

    //TODO
    @PostMapping("/create-repository")
    public String createRepository(final @ModelAttribute("repository") Repository repository, final Principal principal) {
        final User user = this.getCurrentUser(this.userService);
        repository.setOwner(user);
        repository.setCreationDate(LocalDateTime.now());
        this.repositoryService.createRepository(repository);
        return "redirect:/repositories/repositories";
    }

    @PostMapping("/edit-repository/{id}")
    public String updateRepository(final @PathVariable("id") Long id, final @ModelAttribute("repository") Repository repository) {
        final Repository existingRepository = repositoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid repository Id:" + id));
        final User owner = existingRepository.getOwner();
        existingRepository.setOwner(owner);
        existingRepository.setName(repository.getName());
        existingRepository.setDescription(repository.getDescription());
        existingRepository.setPrivate(repository.isPrivate());
        repositoryService.save(existingRepository);
        return "redirect:/repositories/repositories";
    }

    @PostMapping("/delete-repository/{id}")
    public String deleteRepository(@PathVariable("id") Long id) {
        this.repositoryService.delete(id);
        return "redirect:/repositories/repositories";
    }
}
