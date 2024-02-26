package fr.arinonia.opensjgit.service;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.UserRepository;
import fr.arinonia.opensjgit.service.responses.PasswordUpdateResponse;
import fr.arinonia.opensjgit.service.responses.RegistrationResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public RegistrationResponse registerUser(final String username, final String email, final String password, final String passwordConfirm) {
        final RegistrationResponse response = new RegistrationResponse();

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

    public PasswordUpdateResponse updatePassword(final String username, final String currentPassword, final String newPassword) {
        final PasswordUpdateResponse response = new PasswordUpdateResponse();
        final Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            response.setSuccess(false);
            response.setErrorMessage("User not found.");
            return response;
        }

        final User user = userOptional.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            response.setSuccess(false);
            response.setErrorMessage("Current password is incorrect.");
            return response;
        }

        if (newPassword.equals(currentPassword)) {//maybe should work with passwordEncoder.matches(newPassword, currentPassword)
            response.setSuccess(false);
            response.setErrorMessage("New password cannot be the same as the current password.");
            return response;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        response.setSuccess(true);
        return response;
    }


    public Optional<User> findByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }


}
