package util;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpClientUtil {
    public final  static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public final static Gson GSON_INST = new Gson();
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

/*    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }*/

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(Request request, Callback callback) {
/*        Request request = new Request.Builder()
                .url(finalUrl)
                .build();*/

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static Response execute(Request request) {
        Response response;
        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);
        try {
            response = call.execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}