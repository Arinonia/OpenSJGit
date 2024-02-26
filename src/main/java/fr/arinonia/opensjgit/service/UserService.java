package fr.arinonia.opensjgit.service;

import fr.arinonia.opensjgit.entity.User;
import fr.arinonia.opensjgit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (this.userRepository.findByUsername(username) != null) {
            response.setSuccess(false);
            response.setErrorMessage("User already exists with the same name");
            return response;
        }

        if (this.userRepository.findByEmail(email) != null) {
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

}
