package account.service;

import account.model.EmployeeDto;
import account.model.Payment;
import account.model.User;
import account.repository.PaymentRepository;
import account.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adnan
 * @since 12/3/2022
 */
@Service
public class PaymentService {

    PaymentRepository paymentRepository;
    UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public void checkEmployeeExists(List<Payment> payments) {
        payments.forEach(payment -> {
            payment.setEmployee(payment.getEmployee().toLowerCase());

            if (!userRepository.existsByEmail(payment.getEmployee())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee doesn't exist in the database");
            }
        });
    }

    public EmployeeDto getPaymentInfo(User user, Date period) {
        Payment payment = findByEmployeeAndPeriod(user.getEmail().toLowerCase(), period);

        return EmployeeDto.builder()
                .name(user.getName())
                .lastname(user.getLastname())
                .period(new SimpleDateFormat("MMMM-yyyy").format(payment.getPeriod()))
                .salary(payment.getSalary() / 100 + " dollar(s) " + payment.getSalary() % 100 + " cent(s)")
                .build();
    }

    public List<EmployeeDto> getAllPaymentInfo(User user) {
        return findAllByEmail(user.getEmail().toLowerCase()).stream()
                .map(payment -> EmployeeDto.builder()
                        .name(user.getName())
                        .lastname(user.getLastname())
                        .period(new SimpleDateFormat("MMMM-yyyy").format(payment.getPeriod()))
                        .salary(payment.getSalary() / 100 + " dollar(s) " + payment.getSalary() % 100 + " cent(s)")
                        .build())
                .collect(Collectors.toList());
    }

    public Payment findByEmployeeAndPeriod(String email, Date period) {
        return paymentRepository.findByEmployeeAndPeriod(email, period)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Such Payment Exist"));
    }

    public List<Payment> findAllByEmail(String email) {
        return paymentRepository.findAllByEmployeeOrderByPeriodDesc(email);
    }

    @Transactional
    public void updateSalary(Payment payment) {
        Payment paymentToUpdate = findByEmployeeAndPeriod(payment.getEmployee().toLowerCase(), payment.getPeriod());
        paymentToUpdate.setSalary(payment.getSalary());
        paymentRepository.save(paymentToUpdate);
    }

    @Transactional
    public List<Payment> saveOrUpdateAll(List<Payment> payments) {
        return paymentRepository.saveAll(payments);
    }
}
