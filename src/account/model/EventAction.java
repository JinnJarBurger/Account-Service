package account.model;

/**
 * @author adnan
 * @since 12/27/2022
 */
public enum EventAction {

    CREATE_USER,
    CHANGE_PASSWORD,
    ACCESS_DENIED,
    LOGIN_FAILED,
    GRANT_ROLE,
    REMOVE_ROLE,
    LOCK_USER,
    UNLOCK_USER,
    DELETE_USER,
    BRUTE_FORCE
}
