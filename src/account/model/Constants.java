package account.model;

import java.util.List;

/**
 * @author adnan
 * @since 11/30/2022
 */
public interface Constants {

    String ADMINISTRATOR = "ADMINISTRATOR";
    String AUDITOR = "AUDITOR";
    String ACCOUNTANT = "ACCOUNTANT";
    String USER = "USER";

    List<String> breachedPasswords = List.of(
            "PasswordForJanuary",
            "PasswordForFebruary",
            "PasswordForMarch",
            "PasswordForApril",
            "PasswordForMay",
            "PasswordForJune",
            "PasswordForJuly",
            "PasswordForAugust",
            "PasswordForSeptember",
            "PasswordForOctober",
            "PasswordForNovember",
            "PasswordForDecember"
    );
}
