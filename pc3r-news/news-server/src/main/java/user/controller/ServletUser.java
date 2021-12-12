package user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import user.DTO.UserRegisterDTO;
import user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUser extends HttpServlet{
    private final UserService userService = new UserService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getPathInfo().replaceFirst("/", "");
        userService.find(id, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(name, password, email);
        userService.save(userRegisterDTO, resp);
    }
}
