package tools.automation;


import article.entity.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import subscription.entity.Category;
import subscription.repository.CategoryRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static tools.Constants.*;
import static tools.database.MongoUtil.getMongoDataBase;

@Slf4j
public class AutomaticTask {
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

    public static Timer startTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //the first time to execute the task
        Date date = calendar.getTime();
        //if the current time is after the date (18:00), add one day
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
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
        };
        Timer timer = new Timer();
        //task starts after 5 seconds, repeats each 24 hours
        timer.schedule(task, date, ONE_DAY_PERIOD);
        return timer;
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

    private static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
