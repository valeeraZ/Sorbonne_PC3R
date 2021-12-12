package tools.exception;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseException extends RuntimeException{
    @Getter
    private final ErrorCode errorCode;
    @Getter
    private final transient HashMap<String, Object> data = new HashMap<>();

    public BaseException(ErrorCode errorCode, Map<String, Object> data) {
        this.errorCode = errorCode;
        if(!ObjectUtils.isEmpty(data)){
            this.data.putAll(data);
        }
    }
}
