package authentication.service;

import authentication.DTO.LoginRequestDTO;
import authentication.exception.CredentialsInvalidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import user.entity.User;
import user.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static tools.Constants.TOKEN_HEADER;
import static tools.JWTTokenUtils.createToken;
import static tools.exception.ExceptionHandler.handleException;

public class AuthenticationService {
    private final UserRepository userRepository;
    private final BasicPasswordEncryptor passwordEncryptor;
    private final ObjectMapper mapper;

    public AuthenticationService() {
        this.userRepository = new UserRepository();
        this.passwordEncryptor = new BasicPasswordEncryptor();
        this.mapper = new ObjectMapper();
    }

    public void authenticate(LoginRequestDTO loginRequestDTO, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        try{
            User user = checkCredentials(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            resp.setStatus(HttpServletResponse.SC_OK);
            String token = createToken(user.getId_user(), user.getEmail());
            resp.setHeader(TOKEN_HEADER, token);
            out.println(mapper.writeValueAsString(user));
        }catch (CredentialsInvalidException ex){
            handleException(ex, "/authentication", "User authenticate exception", resp);
        }
    }

    private User checkCredentials(String email, String password){
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new CredentialsInvalidException(ImmutableMap.of("email", email, "password", password))
        );
        boolean correct = passwordEncryptor.checkPassword(password, user.getPassword());
        if(!correct){
            throw new CredentialsInvalidException(ImmutableMap.of("email", email, "password", password));
        }else {
            return user;
        }
    }
}
