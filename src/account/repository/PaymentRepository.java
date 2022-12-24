package account.repository;

import account.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author adnan
 * @since 12/3/2022
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByEmployeeAndPeriod(String email, Date period);

    List<Payment> findAllByEmployeeOrderByPeriodDesc(String email);
}
