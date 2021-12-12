import article.entity.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;
import subscription.entity.Category;
import subscription.repository.CategoryRepository;
import tools.automation.NewsResponseBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static tools.Constants.BASE_URL;
import static tools.Constants.apiKey;
import static tools.database.MongoUtil.getMongoDataBase;
@Slf4j
public class SaveArticlesTest {
    private final static List<Category> categories = new CategoryRepository().findAll();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final static MongoDatabase db = getMongoDataBase();
    private final static MongoCollection<BasicDBObject> collection = db.getCollection("articles", BasicDBObject.class);

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private static NewsResponseBody callNewsAPI(String todayDate, String category) throws IOException {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("qInTitle", category)
                .addQueryParameter("from", todayDate)
                .addQueryParameter("to", todayDate)
                .addQueryParameter("sortBy", "popularity")
                .addQueryParameter("apiKey", apiKey)
                .addQueryParameter("pageSize", "50")
                .addQueryParameter("page", "1")
                .addQueryParameter("language", "en");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(response.body());
        String responseBody = response.body().string();
        NewsResponseBody newsResponseBody = objectMapper.readValue(responseBody, NewsResponseBody.class);
        response.close();
        return newsResponseBody;
    }

    //@Test
    public void saveArticlesBeforeLaunching(){
        log.info("update task begins: " + getCurrentTime());
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String todayDate = simpleDateFormat.format(new Date());
        //clean the collection of old articles
        BasicDBObject oldDocument = new BasicDBObject();
        collection.deleteMany(oldDocument);
        //save 20*50 new articles into the collection
        for (Category category : categories) {
            try {
                String categoryName = category.getName_category();
                NewsResponseBody newsResponseBody = callNewsAPI(todayDate, categoryName);
                List<BasicDBObject> documents = new ArrayList<>();
                for (Article article : newsResponseBody.getArticles()) {
                    article.setCategory(category);
                    BasicDBObject document = objectMapper.readValue(objectMapper.writeValueAsString(article), BasicDBObject.class);
                    documents.add(document);
                }
                collection.insertMany(documents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("update task ends: " + getCurrentTime());
    }


}
