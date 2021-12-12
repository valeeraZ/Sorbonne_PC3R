package tools.exception;

import java.util.Map;

public class MethodArgumentInvalidException extends BaseException{
    public MethodArgumentInvalidException(Map<String, Object> data){
        super(ErrorCode.METHOD_ARGUMENT_INVALID, data);
    }
}
