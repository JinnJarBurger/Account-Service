package account.model;

import lombok.*;

/**
 * @author adnan
 * @since 12/6/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {

    private String name;
    private String lastname;
    private String period;
    private String salary;
}
