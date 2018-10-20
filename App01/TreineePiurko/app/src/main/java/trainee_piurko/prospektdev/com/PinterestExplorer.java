package trainee_piurko.prospektdev.com;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PinterestExplorer {

    private static final String TAG = "PinterestExplorer";
    private static final String API_ID = "4974189791598881784";
    private static final String API_SECRET_KEY = "619fe19eefcc356a32841e3f51c237dad6779b09584e5d1dd6e176d1051916b8";

    public String getJSONString(String urlSpec) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlSpec)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }


    public List<AppItem> feachItems() {
        List<AppItem> appItems = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.pinterest.com")
                    .buildUpon()
                    .appendQueryParameter("method", "/v1/pins/<pin>")
                    .appendQueryParameter("api_key", API_SECRET_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojesoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(appItems,jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Ошибка загрузки", ioe);
        } catch (JSONException joe) {
            Log.e(TAG, "Ошибка парсинга JSON", joe);
        }
        return appItems;
    }

    private void parseItems (List<AppItem> items, JSONObject jsonBody) throws IOException, JSONException{
        JSONObject photoJSONObject = jsonBody.getJSONObject("pins");
        JSONArray photoJSONArray = photoJSONObject.getJSONArray("image");

        for (int i=0; i < photoJSONArray.length(); i++){
            JSONObject photoJsonObject = photoJSONArray.getJSONObject(i);
            AppItem item = new AppItem();
            item.setId(photoJsonObject.getString("Id"));
            item.setCaption(photoJsonObject.getString("title"));

            if(!photoJsonObject.has("url_s")){
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);

        }


    }
}
