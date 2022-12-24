package account.controller.api;

import account.model.NewPassword;
import account.model.User;
import account.service.AccessManager;
import account.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * @author adnan
 * @since 11/16/2022
 */
@RestController
@RequestMapping("/api")
public class UserApiController {

    UserService userService;
    AccessManager accessManager;

    public UserApiController(UserService userService, AccessManager accessManager) {
        this.userService = userService;
        this.accessManager = accessManager;
    }

    @PostMapping("/auth/signup")
    public User signUp(@Valid @RequestBody User user) {
        accessManager.checkEmailAndPassword(user);

        return userService.saveOrUpdate(user);
    }

    @PostMapping("/auth/changepass")
    public Map<String, String> changePassword(@Valid @RequestBody NewPassword newPassword, Principal principal) {
        accessManager.checkIsPasswordBreached(newPassword.getPassword());
        userService.changePassword(newPassword, principal);

        return Map.of(
                "email", principal.getName(),
                "status", "The password has been updated successfully"
        );
    }
}
