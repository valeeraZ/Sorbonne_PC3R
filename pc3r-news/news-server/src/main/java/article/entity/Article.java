package article.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import subscription.entity.Category;

@Data
@NoArgsConstructor
public class Article {
    private Category category;
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    @Data
    @NoArgsConstructor
    public static class Source{
        private String id;
        private String name;
    }
}
