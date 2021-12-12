package subscription.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import subscription.DTO.SubscriptionDTO;
import subscription.entity.Category;
import subscription.repository.CategoryRepository;
import subscription.repository.SubscriptionRepository;
import tools.exception.MethodArgumentInvalidException;
import user.entity.User;
import user.exception.IDNotExistsException;
import user.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static tools.exception.ExceptionHandler.handleException;
import static tools.exception.Validating.validate;

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public SubscriptionService() {
        this.subscriptionRepository = new SubscriptionRepository();
        this.categoryRepository = new CategoryRepository();
        this.userRepository = new UserRepository();
        this.mapper = new ObjectMapper();
    }

    public void saveSubscriptions(String id_user, List<SubscriptionDTO> subscriptionDTOs, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        User user = userRepository.getUserById(id_user).orElseThrow(
                () -> new IDNotExistsException(ImmutableMap.of("id_user", id_user))
        );
        List<Category> subscriptions = new ArrayList<>();
        for(SubscriptionDTO subscriptionDTO : subscriptionDTOs){
            try{
                validate(subscriptionDTO);
                int id_category = subscriptionDTO.getId_category();
                Category category = categoryRepository.findCategoryById(id_category).orElseThrow(
                        () -> new IDNotExistsException(ImmutableMap.of("id_category", id_category))
                );
                subscriptions.add(category);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            } catch (MethodArgumentInvalidException e) {
                handleException(e, "/subscriptions", "Subscription creating exception", resp);
            } catch (IDNotExistsException ex){
                handleException(ex, "/subscriptions", "User/Category ID not found exception", resp);
            }
        }
        user.setSubscriptions(subscriptions);
        userRepository.update(user);
        out.println(mapper.writeValueAsString(user));
    }
}
