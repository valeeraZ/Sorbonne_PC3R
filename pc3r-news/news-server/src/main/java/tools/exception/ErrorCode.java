package tools.exception;

import lombok.Getter;

import static javax.servlet.http.HttpServletResponse.*;


@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(1001, SC_BAD_REQUEST ,"This email address is already registered."),
    EMAIL_NOT_FOUND(1002,SC_NOT_FOUND,"email.notfound"),
    ID_NOT_FOUND(1002, SC_NOT_FOUND, "The ressource with this ID is not found"),
    VERIFY_JWT_FAILED(1003, SC_UNAUTHORIZED,"Your token is invalid or expired, please reconnect to have a new token."),
    METHOD_ARGUMENT_INVALID(1004,SC_BAD_REQUEST,"The arguments of method are not valid."),
    CREDENTIALS_INVALID(1003,SC_UNAUTHORIZED,"Your credentials are invalid, please check it again.");

    private final int code;
    private final int status;
    private final String message;

    ErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
