package account.service;

import account.model.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static account.model.Constants.breachedPasswords;

/**
 * @author adnan
 * @since 11/29/2022
 */
@Service
public class AccessManager {

    @Autowired
    private UserRepository userRepository;

    public void checkEmailAndPassword(User user) {
        checkDuplicateEmail(user.getEmail());
        checkIsPasswordBreached(user.getPassword());
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }
    }

    public void checkIsPasswordBreached(String password) {
        if (breachedPasswords.contains(password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
    }
}
