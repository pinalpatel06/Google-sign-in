package tekkan.synappz.com.tekkan.custom.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class TekenJsonObjectRequest extends TekenRequest<JSONObject> {

    public TekenJsonObjectRequest(int method, String url, TekenResponseListener<JSONObject> listener, TekenErrorListener errorListener, int requestCode) {
        super(method, url, listener, errorListener,requestCode);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        if(getStatus(response) != HttpURLConnection.HTTP_OK){
            Log.d("TAG", getStatus(response) + "" + getMessage(response));
            return Response.error(new VolleyError(response));
        }else{
            try {
                return Response.success(new JSONObject(getMessage(response)), HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new VolleyError(e));
            }
        }
    }
}
