package account.service;

import account.model.*;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Set;

import static account.model.Operation.GRANT;
import static account.model.Operation.REMOVE;
import static account.model.Role.*;

/**
 * @author adnan
 * @since 11/29/2022
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User changeRole(RoleChangeDto roleChangeDto) {
        String email = roleChangeDto.getUser();
        String roleName = ROLE_PREFIX + roleChangeDto.getRole();
        Operation operation = roleChangeDto.getOperation();

        User user = findByEmail(email);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        if (operation == GRANT) {
            if ((user.isAdmin() && !role.getName().equals(ROLE_ADMINISTRATOR))
                    || (!user.isAdmin() && role.getName().equals(ROLE_ADMINISTRATOR))) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            }

            user.getRoles().add(role);
            user = saveOrUpdate(user);
        } else if (operation == REMOVE) {
            if (!user.getRoles().contains(role)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            }

            if (user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            }

            if (user.getRoles().size() == 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }

            user.getRoles().remove(role);
            user = saveOrUpdate(user);
        }

        return user;
    }

    public void changePassword(NewPasswordDto newPasswordDto, Principal principal) {
        User user = findByEmail(principal.getName());

        if (encoder.matches(newPasswordDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        user.setPassword(encoder.encode(newPasswordDto.getPassword()));
        saveOrUpdate(user);
    }

    public void increaseFailedAttempts(User user) {
        userRepository.updateFailedAttempts(user.getFailedAttempt() + 1, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    @Transactional
    public User saveOrUpdate(User user) {
        if (user.isNew()) {
            user.setEmail(user.getEmail().toLowerCase());
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(userRepository.count() == 0
                    ? Set.of(roleRepository.findByName(ROLE_ADMINISTRATOR).orElse(new Role(ROLE_ADMINISTRATOR)))
                    : Set.of(roleRepository.findByName(ROLE_USER).orElse(new Role(ROLE_USER))));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void delete(String email) {
        User user = findByEmail(email);

        if (user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(user);
    }
}
