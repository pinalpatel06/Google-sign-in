package tekkan.synappz.com.tekkan.custom.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.net.HttpURLConnection;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class TekenStringRequest extends TekenRequest<String> {

    public TekenStringRequest(int method, String url, TekenResponseListener<String> listener, TekenErrorListener errorListener, int requestCode) {
        super(method, url, listener, errorListener, requestCode);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (getStatus(response) != HttpURLConnection.HTTP_OK) {
            Log.d("TAG", getStatus(response) + "" + getMessage(response));
            return Response.error(new VolleyError(response));
        } else {
            return Response.success(getMessage(response), HttpHeaderParser.parseCacheHeaders(response));
        }
    }
}
