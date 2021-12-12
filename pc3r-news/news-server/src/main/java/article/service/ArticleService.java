package article.service;

import article.entity.Article;
import article.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import subscription.DTO.SubscriptionDTO;
import subscription.entity.Category;
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

public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public ArticleService(){
        this.articleRepository = new ArticleRepository();
        this.userRepository = new UserRepository();
        this.mapper = new ObjectMapper();
    }

    public void getArticles(String raw_id_category, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        try{
            if (raw_id_category == null)
                throw new MethodArgumentInvalidException(ImmutableMap.of("id_category", "an-id-from-1-to-20"));
            int id_category = Integer.parseInt(raw_id_category);
            validate(new SubscriptionDTO(id_category));
            List<Article> articles = articleRepository.findArticlesByIdCategory(id_category);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(mapper.writeValueAsString(articles));
        }catch (MethodArgumentInvalidException ex){
            handleException(ex, "/articles", "Category ID not found exception", resp);
        }
    }

    public void getSubscribedArticles(String id_user, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        try{
            User user = userRepository.getUserById(id_user).orElseThrow(
                    () -> new IDNotExistsException(ImmutableMap.of("id_user", id_user))
            );
            List<Category> subscriptions = user.getSubscriptions();
            List<Article> subscribedArticles = new ArrayList<>();
            for(Category category: subscriptions){
                List<Article> articles = articleRepository.findArticlesByCategory(category);
                subscribedArticles.addAll(articles);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(mapper.writeValueAsString(subscribedArticles));
        }catch (IDNotExistsException ex){
            handleException(ex, "/articles", "User ID not found exception", resp);
        }
    }

}
