package subscription.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import subscription.entity.Category;
import subscription.repository.CategoryRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
        this.mapper = new ObjectMapper();
    }

    public void findAll(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        List<Category> categories = categoryRepository.findAll();
        resp.setStatus(HttpServletResponse.SC_OK);
        out.println(mapper.writeValueAsString(categories));
    }
}
