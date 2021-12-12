package tools;

public class Constants {
    /**
     * JWT signature secret key
     */
    public static String TOKEN_SECRET = "jWnZr4u7x!A%D*G-KaNdRgUkXp2s5v8y/B?E(H+MbQeShVmYq3t6w9z$C&F)J@Nc";

    /**
     * JWT expires in 24 hours
     */
    public static long TOKEN_EXPIRATION = 60 * 60 * 24L;

    /**
     * JWT in response
     */
    public static String TOKEN_HEADER = "Authorization";
    public static String TOKEN_PREFIX = "Bearer ";
    public static String TOKEN_TYPE = "JWT";

    /**
     * News API Request
     */
    public static String apiKey = "secret";
    public static String BASE_URL = "https://newsapi.org/v2/everything";

    /**
     * Automatic task
     */
    public static long ONE_DAY_PERIOD = 60 * 60 * 1000 * 24;

}
