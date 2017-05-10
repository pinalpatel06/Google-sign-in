package tekkan.synappz.com.tekkan.custom.network;

import android.net.Uri;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public abstract class TekenRequest<T> extends Request<T> {

    private static final String TAG = TekenRequest.class.getSimpleName();

    private static final String
            JSON_N_STATUS = "status",
            JSON_S_MESSAGE = "message";

    private HashMap<String, String> mParams;
    private HashMap<String, Uri> mFiles;

    public TekenRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mParams = new HashMap<>();
        mFiles = new HashMap<>();
    }

    /*@Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            JSONObject responseObject = new JSONObject(jsonString);

            int status = responseObject.optInt(JSON_N_STATUS);

            if(status == HttpURLConnection.HTTP_OK){

                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            }else{
                return Response.error(new VolleyError(response));
            }
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }*/

    @Override
    public String getUrl() {
        if (getMethod() == Method.GET) {
            String url = super.getUrl();

            Uri uri = Uri.parse(url);
            Uri.Builder builder = uri.buildUpon();

            for (Map.Entry<String, String> param : mParams.entrySet()) {
                    builder.appendQueryParameter(param.getKey(), param.getValue());
            }
            return builder.build().toString();
        } else {
            return super.getUrl();
        }
    }

    @Override
    protected String getParamsEncoding() {
        return super.getParamsEncoding();
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public Map<String, Uri> getFiles() {
        return mFiles;
    }

    public void addParam(String key, String value) {
        mParams.put(key, value);
    }

    public void addFile(String key, Uri fileUri) {
        mFiles.put(key, fileUri);
    }

    public final int getStatus(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject responseObject = new JSONObject(jsonString);
            return responseObject.optInt(JSON_N_STATUS, -1);
        } catch (Exception e) {
            return -1;
        }
    }

    public final String getMessage(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject responseObject = new JSONObject(jsonString);
            return responseObject.optString(JSON_S_MESSAGE, null);
        } catch (Exception e) {
            return null;
        }
    }
}
