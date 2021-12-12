package authentication.controller;

import authentication.DTO.LoginRequestDTO;
import authentication.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletAuthentication extends HttpServlet {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        authenticationService.authenticate(loginRequestDTO, resp);
    }
}
