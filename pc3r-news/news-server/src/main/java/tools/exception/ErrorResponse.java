package tools.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String timestamp;
    private int code;
    private int status;
    private String message;
    private String path;
    private final HashMap<String, Object> errorDetail = new HashMap<>();

    public ErrorResponse(int code, int status, String message, String path, Map<String, Object> errorDetail) {
        this.timestamp = Instant.now().toString();
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
        if(!ObjectUtils.isEmpty(errorDetail)){
            this.errorDetail.putAll(errorDetail);
        }
    }

    public ErrorResponse(BaseException ex, String path){
        this(ex.getErrorCode().getCode(),
                ex.getErrorCode().getStatus(),
                ex.getErrorCode().getMessage(),
                path,
                ex.getData());
    }
}
