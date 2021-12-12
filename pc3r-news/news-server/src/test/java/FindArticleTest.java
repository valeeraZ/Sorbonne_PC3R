import article.entity.Article;
import article.repository.ArticleRepository;
import org.junit.Test;
import subscription.entity.Category;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FindArticleTest {
    @Test
    public void findArticles(){
        ArticleRepository articleRepository = new ArticleRepository();
        assertFalse(articleRepository.findArticlesByIdCategory(1).isEmpty());
    }
}
