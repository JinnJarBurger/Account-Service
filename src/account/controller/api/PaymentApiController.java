package account.controller.api;

import account.model.Payment;
import account.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author adnan
 * @since 12/3/2022
 */
@RestController
@RequestMapping("/api")
public class PaymentApiController {

    PaymentService paymentService;

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/acct/payments")
    public Map<String, String> uploadPayrolls(@Valid @RequestBody @NotEmpty
                                              List<@Valid Payment> payments) {

        paymentService.checkEmployeeExists(payments);
        paymentService.saveOrUpdateAll(payments);

        return Map.of(
                "status", "Added successfully!"
        );
    }

    @PutMapping("/acct/payments")
    public Map<String, String> updatePayroll(@RequestBody Payment payment) {
        paymentService.updateSalary(payment);

        return Map.of(
                "status", "Updated successfully!"
        );
    }
}
