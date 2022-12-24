package account.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author adnan
 * @since 12/9/2022
 */
public class CustomDateValidator implements ConstraintValidator<ValidDate, Date> {

    private static final String DATE_PATTERN = "MM-yyyy";

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        try {
            new SimpleDateFormat(DATE_PATTERN).parse(value.toString());

            return true;
        } catch (ParseException exception) {
            return false;
        }
    }
}
