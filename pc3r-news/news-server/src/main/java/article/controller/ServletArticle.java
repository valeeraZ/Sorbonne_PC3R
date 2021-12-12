package article.controller;

import article.service.ArticleService;
import tools.exception.MethodArgumentInvalidException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletArticle extends HttpServlet {
    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id_user = req.getParameter("id_user");
        if(id_user != null){
            articleService.getSubscribedArticles(id_user, resp);
        }else {
            String id_category = req.getParameter("id_category");
            articleService.getArticles(id_category, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
