package account.controller.api;

import account.model.NewPasswordDto;
import account.model.User;
import account.service.AccessManager;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/auth")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessManager accessManager;

    @PostMapping("/signup")
    public User signUp(@Valid @RequestBody User user) {
        accessManager.checkEmailAndPassword(user);

        return userService.saveOrUpdate(user);
    }

    @PostMapping("/changepass")
    public Map<String, String> changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto, Principal principal) {
        accessManager.checkIsPasswordBreached(newPasswordDto.getPassword());
        userService.changePassword(newPasswordDto, principal);

        return Map.of(
                "email", principal.getName(),
                "status", "The password has been updated successfully"
        );
    }
}
