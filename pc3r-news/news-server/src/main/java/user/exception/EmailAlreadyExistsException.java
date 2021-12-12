package user.exception;

import tools.exception.BaseException;
import tools.exception.ErrorCode;

import java.util.Map;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(Map<String, Object> data) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, data);
    }
}
