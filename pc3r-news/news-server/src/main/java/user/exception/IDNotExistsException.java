package user.exception;

import tools.exception.BaseException;
import tools.exception.ErrorCode;

import java.util.Map;

public class IDNotExistsException extends BaseException {
    public IDNotExistsException(Map<String, Object> data) {
        super(ErrorCode.ID_NOT_FOUND, data);
    }
}
