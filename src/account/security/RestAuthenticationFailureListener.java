package account.security;

import account.model.User;
import account.service.EventLogger;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

import static account.model.EventAction.*;
import static org.springframework.boot.logging.LogLevel.*;

/**
 * @author adnan
 * @since 12/30/2022
 */
@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationFailureHandler.class);

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        Principal principal = request.getUserPrincipal();
        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                userService.increaseFailedAttempts(user);

                EventLogger.log(LOGIN_FAILED, LOGGER, WARN, request, null);
            } else {
                userService.lock(user);
                exception = new LockedException("Your account has been locked due to 3 failed attempts. It will be unlocked after 24 hours.");

                EventLogger.log(BRUTE_FORCE, LOGGER, FATAL, request, null);
                EventLogger.log(LOCK_USER, LOGGER, FATAL, request, "Lock user " + email);
            }
        } else if (!user.isAccountNonLocked()) {
            if (userService.unlockWhenTimeExpired(user)) {
                exception = new LockedException("Your account has been unlocked. Please try to login again.");

                EventLogger.log(UNLOCK_USER, LOGGER, INFO, request, "Unlock user " + email);
            }
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
