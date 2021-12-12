package tools.filter;

import tools.exception.BaseException;
import tools.exception.ErrorCode;

import java.util.Map;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException(Map<String, Object> data) {
        super(ErrorCode.VERIFY_JWT_FAILED, data);
    }
}
