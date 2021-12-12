package authentication.exception;

import tools.exception.BaseException;
import tools.exception.ErrorCode;

import java.util.Map;

public class CredentialsInvalidException extends BaseException {
    public CredentialsInvalidException(Map<String, Object> data) {
        super(ErrorCode.CREDENTIALS_INVALID, data);
    }
}
