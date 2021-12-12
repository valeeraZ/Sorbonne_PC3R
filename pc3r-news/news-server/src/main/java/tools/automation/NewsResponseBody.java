package tools.automation;

import article.entity.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewsResponseBody {
    private String status;
    private int totalResults;
    private List<Article> articles;
}
