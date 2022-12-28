package account.controller.api;

import account.model.NewPasswordDto;
import account.model.User;
import account.service.AccessManager;
import account.service.EventLogger;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

import static account.model.EventAction.CHANGE_PASSWORD;
import static account.model.EventAction.CREATE_USER;
import static org.springframework.boot.logging.LogLevel.INFO;

/**
 * @author adnan
 * @since 11/16/2022
 */
@RestController
@RequestMapping("/api/auth")
public class UserApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccessManager accessManager;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/signup")
    public User signUp(@Valid @RequestBody User user) {
        accessManager.checkEmailAndPassword(user);
        user = userService.saveOrUpdate(user);

        EventLogger.log(CREATE_USER, LOGGER, INFO, request, user.getEmail());

        return user;
    }

    @PostMapping("/changepass")
    public Map<String, String> changePassword(@Valid @RequestBody NewPasswordDto newPasswordDto, Principal principal) {
        accessManager.checkIsPasswordBreached(newPasswordDto.getPassword());
        userService.changePassword(newPasswordDto, principal);

        EventLogger.log(CHANGE_PASSWORD, LOGGER, INFO, request, principal.getName());

        return Map.of(
                "email", principal.getName(),
                "status", "The password has been updated successfully"
        );
    }
}
