package account.controller.api;

import account.model.User;
import account.service.PaymentService;
import account.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

/**
 * @author adnan
 * @since 11/30/2022
 */
@RestController
@RequestMapping("/api")
public class EmployeeApiController {

    UserService userService;
    PaymentService paymentService;

    public EmployeeApiController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @GetMapping("/empl/payment")
    public Object getPayment(@RequestParam(required = false)
                             @DateTimeFormat(pattern = "MM-yyyy", iso = DateTimeFormat.ISO.DATE)
                             Date period,
                             Principal principal) {

        User user = userService.findByEmail(principal.getName());

        if (period != null) {
            return paymentService.getPaymentInfo(user, period);
        }

        return paymentService.getAllPaymentInfo(user);
    }
}
