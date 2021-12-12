package article.repository;

import article.entity.Article;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import subscription.entity.Category;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

import static tools.database.MongoUtil.getMongoDataBase;

public class ArticleRepository {

    private final static MongoDatabase db = getMongoDataBase();

    public List<Article> findArticlesByCategory(Category category){
        List<Article> articles = new ArrayList<>();
        MongoCollection<Article> collection = db.getCollection("articles", Article.class);
        FindIterable<Article> iterable = collection.find(eq("category", category));
        MongoCursor<Article> cursor = iterable.iterator();
        cursor.forEachRemaining(articles::add);
        return articles;
    }

    public List<Article> findArticlesByIdCategory(int id_category){
        List<Article> articles = new ArrayList<>();
        MongoCollection<Article> collection = db.getCollection("articles", Article.class);
        FindIterable<Article> iterable = collection.find(eq("category.id_category", id_category));
        MongoCursor<Article> cursor = iterable.iterator();
        cursor.forEachRemaining(articles::add);
        return articles;
    }
}
