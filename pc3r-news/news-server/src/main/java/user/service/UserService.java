package user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import tools.exception.MethodArgumentInvalidException;
import user.DTO.UserRegisterDTO;
import user.entity.User;
import user.exception.EmailAlreadyExistsException;
import user.exception.IDNotExistsException;
import user.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static tools.exception.ExceptionHandler.handleException;
import static tools.exception.Validating.validate;

@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BasicPasswordEncryptor passwordEncryptor;
    private final ObjectMapper mapper;

    public UserService() {
        this.userRepository = new UserRepository();
        this.passwordEncryptor = new BasicPasswordEncryptor();
        this.mapper = new ObjectMapper();
    }

    public void save(UserRegisterDTO userRegisterDTO, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try{
            validate(userRegisterDTO);
            emailNotExists(userRegisterDTO.getEmail());
            User user = userRegisterDTO.toUser();
            user.setPassword(passwordEncryptor.encryptPassword(userRegisterDTO.getPassword()));
            userRepository.save(user);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(mapper.writeValueAsString(user));
        }catch (EmailAlreadyExistsException | MethodArgumentInvalidException ex){
            handleException(ex, "/users", "User register exception", resp);
        }
    }

    public void find(String id, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try{
            User user = userRepository.getUserById(id).orElseThrow(
                    () -> new IDNotExistsException(ImmutableMap.of("id_user", id))
            );
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(mapper.writeValueAsString(user));
        }catch (IDNotExistsException ex){
            handleException(ex, "/users", "User ID not found exception", resp);
        }
    }

    private void emailNotExists(String email){
        boolean exist = userRepository.getUserByEmail(email).isPresent();
        if(exist){
            throw new EmailAlreadyExistsException(ImmutableMap.of("email", email));
        }
    }
}
