package account.model;

import account.validator.ValidDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author adnan
 * @since 12/3/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment", uniqueConstraints = @UniqueConstraint(name = "UniqueEmployeeAndPeriod",
        columnNames = {"employee", "period"}))
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @NotBlank
    @Email(regexp = ".*@acme.com")
    private String employee;

    @NotNull
    @DateTimeFormat(pattern = "MM-yyyy", iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Date period;

    @NotNull
    @Min(value = 0L)
    private Long salary;
}
