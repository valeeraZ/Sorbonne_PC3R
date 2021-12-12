package tools.filter;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import tools.Constants;
import tools.JWTTokenUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static tools.Constants.TOKEN_HEADER;
import static tools.exception.ExceptionHandler.handleException;

@Slf4j
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(filterConfig.getFilterName() + " init ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String token = req.getHeader(TOKEN_HEADER);
        try{
            if(token == null || !token.startsWith(Constants.TOKEN_PREFIX)){
                throw new TokenInvalidException(ImmutableMap.of("Authorization", "token-beginning-with-Bearer"));
            }else {
                String newToken = validateAuthentication(token);
                resp.setHeader(TOKEN_HEADER, newToken);
            }
            chain.doFilter(req, resp);
        }catch (TokenInvalidException e){
            handleException(e, req.getRequestURI(), "User authenticate exception", resp);
        }
    }

    @Override
    public void destroy() {

    }

    private String validateAuthentication(String authorization){
        //remove the prefix "Bearer " of token
        String token = authorization.replace(Constants.TOKEN_PREFIX,"");
        String user_email = null;
        String user_id = null;
        try{
            user_email = JWTTokenUtils.getEmailByToken(token);
            user_id = JWTTokenUtils.getIdUser(token);
            if (user_email == null || user_id == null)
                throw new TokenInvalidException(ImmutableMap.of("Authorization", authorization));
            return JWTTokenUtils.createToken(user_id, user_email);
        }catch ( SignatureException | ExpiredJwtException | MalformedJwtException | IllegalArgumentException e){
            throw new TokenInvalidException(ImmutableMap.of("Authorization", authorization));
        }
    }
}
