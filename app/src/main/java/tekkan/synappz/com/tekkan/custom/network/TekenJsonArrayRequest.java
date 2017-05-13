package tekkan.synappz.com.tekkan.custom.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class TekenJsonArrayRequest extends TekenRequest<JSONArray> {

    public TekenJsonArrayRequest(int method, String url, TekenResponseListener<JSONArray> listener, TekenErrorListener errorListener, int requestCode) {
        super(method, url, listener, errorListener,requestCode);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        if(getStatus(response) != HttpURLConnection.HTTP_OK){
            return Response.error(new VolleyError(response));
        }else{
            try {
                return Response.success(new JSONArray(getMessage(response)), HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new VolleyError(e));
            }
        }
    }
}
