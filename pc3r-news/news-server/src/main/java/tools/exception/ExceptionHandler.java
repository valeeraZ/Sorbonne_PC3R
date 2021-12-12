package tools.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ExceptionHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void handleException(BaseException ex, String path, String log_info, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        ErrorResponse errorResponse = new ErrorResponse(ex, path);
        log.info(log_info + ": "+ errorResponse.toString());
        resp.setStatus(ex.getErrorCode().getStatus());
        out.println(mapper.writeValueAsString(errorResponse));
    }
}
