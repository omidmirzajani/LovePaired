package dimo.applycaran;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImageAPI extends AsyncTask {
    private String mImageUrl;
    private Activity mActivity;
    public static String result="";
    String filename = "";
    public UploadImageAPI(Activity activity, String imgURL) {
        this.mActivity = activity;
        this.mImageUrl = imgURL;
        this.result = "";
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            return sendPost( mImageUrl);
        } catch (Exception e) {
            result = e.getMessage();
        }
        return "Hi";
    }

    private String sendPost(String path) throws Exception {
        File f = new File(path);
        filename = f.getName().substring(0,f.getName().length()-9);


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("image",f);
        post(params,"http://192.168.1.106:8000/textract/omid/");

        return "Hi";
    }


    public void post(Map<String, Object> params, String Link) {
        try {
            HttpUrl url = HttpUrl.parse(Link);
            MultipartBody.Builder b = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            if (params != null) {
                Object[] keys = params.keySet().toArray();
                Object[] values = params.values().toArray();
                for (int i = 0; i < params.size(); i++) {
                    Object key = keys[i];
                    Object value = values[i];
                    if (value instanceof File) {
                        File f = (File) value;
                        b.addFormDataPart(key.toString(), f.getName(), RequestBody.create(null, f));
                    } else
                        b.addFormDataPart(key.toString(), value.toString());
                }
            }

            OkHttpClient client;
            client = new OkHttpClient.Builder()
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();

            RequestBody body = b.build();
            Request req = new Request.Builder()
                    .tag(System.currentTimeMillis())
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(req).execute();
            String actualResp = response.body().string();
            actualResp = actualResp.substring(2, actualResp.length() - 2);

            try {
                // Yes baby
                String finalActualResp = actualResp;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, finalActualResp,Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, filename,Toast.LENGTH_LONG).show();
                    }
                });
                result = e.getMessage();

            }
        } catch (Exception e) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, filename,Toast.LENGTH_LONG).show();
                }
            });
            result = e.getMessage();
        }
    }//192.168.232.2
}