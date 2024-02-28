package fr.arinonia.opensjgit.service;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.UserRepository;
import fr.arinonia.opensjgit.service.responses.Response;
import fr.arinonia.opensjgit.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class UserService implements UserDetailsService  {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Response registerUser(final String username, final String email, final String password, final String passwordConfirm) {
        final Response response = new Response();

        if (!password.equals(passwordConfirm)) {
            response.setSuccess(false);
            response.setErrorMessage("Passwords do not match.");
            return response;
        }

        if (password.length() < 7) {
            response.setSuccess(false);
            response.setErrorMessage("Your password size must be higher than 7");
            return response;
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            response.setSuccess(false);
            response.setErrorMessage("User already exists with the same name");
            return response;
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            response.setSuccess(false);
            response.setErrorMessage("User already exists with the same email");
            return response;
        }

        final User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(this.passwordEncoder.encode(password));
        newUser.setCreation_date(java.time.LocalDate.now().toString());
        newUser.setUsing_2fa(false);
        this.userRepository.save(newUser);
        response.setSuccess(true);
        return response;
    }

    public Response updatePassword(final String username, final String currentPassword, final String newPassword, final String confirmNewPassword) {
        final Response response = new Response();
        final Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            response.setSuccess(false);
            response.setErrorMessage("User not found.");
            return response;
        }

        final User user = userOptional.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            System.out.println(currentPassword);
            response.setSuccess(false);
            response.setErrorMessage("Current password is incorrect.");
            return response;
        }

        if (newPassword.equals(currentPassword)) {//maybe should work with passwordEncoder.matches(newPassword, currentPassword)
            response.setSuccess(false);
            response.setErrorMessage("New password cannot be the same as the current password.");
            return response;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            response.setSuccess(false);
            response.setErrorMessage("The new password and confirmation password do not match.");
            return response;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        response.setSuccess(true);
        return response;
    }

    public Response updateProfilePicture(String username, MultipartFile profilePicture) {
        Response response = new Response();
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String filePath = saveProfilePicture(username, profilePicture);
            user.setProfile_picture(filePath);
            userRepository.save(user);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    public String saveProfilePicture(final String username, final MultipartFile file) throws IOException {
        final File uploadDir = new File(Constants.PROFILE_PICTURE_PATH);
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                System.err.println("cannot create " + Constants.PROFILE_PICTURE_PATH);//TODO add better log trace
            }
        }

        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        final String currentPicturePath = user.getProfile_picture();
        if (currentPicturePath != null && !currentPicturePath.isEmpty() && !currentPicturePath.equals("default-pic.png")) {
            final Path path = Paths.get(Constants.PROFILE_PICTURE_PATH + currentPicturePath);
            try {
                Files.deleteIfExists(path);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        final String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if(filename.contains("..")) {
            throw new IOException("Filename contains invalid path sequence " + filename);
        }
        final String newFileName = UUID.randomUUID() + "-" + filename;
        //That's kinda dangerous to replace I should find another way
        Files.copy(file.getInputStream(), new File(uploadDir, newFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    public Optional<User> findByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> findById(final Long id) {
        return this.userRepository.findById(id);
    }

    public void save(final User user) {
        this.userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
