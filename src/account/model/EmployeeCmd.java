package account.model;

import lombok.*;

import java.time.LocalDate;

/**
 * @author adnan
 * @since 12/6/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCmd {

    private String name;
    private String lastname;
    private String period;
    private String salary;
}
