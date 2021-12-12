import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import static tools.Constants.BASE_URL;
import static tools.Constants.apiKey;

public class NewsAPITest {
    @Test
    public void testAPICall() throws IOException {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String todayDate = simpleDateFormat.format(new Date());

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("qInTitle", "Apple")
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
        Response response = call.execute();
        assertNotNull(response.body());
        assertTrue(response.isSuccessful());
        response.close();
    }
}
