package subscription.controller;

import subscription.DTO.SubscriptionDTO;
import subscription.service.SubscriptionService;
import user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServletSubscription extends HttpServlet {

    private final SubscriptionService subscriptionService = new SubscriptionService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id_user = req.getPathInfo().replaceFirst("/", "");
        userService.find(id_user, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SubscriptionDTO> subscriptionDTOs = new ArrayList<>();
        String id_user = req.getParameter("id_user");
        for(int i = 1; i <= 20; i++){
            String id = req.getParameter("c" + i);
            if (id != null){
                int id_category = Integer.parseInt(id);
                SubscriptionDTO dto = new SubscriptionDTO(id_category);
                subscriptionDTOs.add(dto);
            }
        }
        subscriptionService.saveSubscriptions(id_user, subscriptionDTOs, resp);
    }
}
