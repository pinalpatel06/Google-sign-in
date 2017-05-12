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

    private Response.Listener<String> mListener;

    public TekenStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if(getStatus(response) != HttpURLConnection.HTTP_OK){
            Log.d("TAG", getStatus(response) + "" + getMessage(response));
            return Response.error(new VolleyError(response));
        }else{
            return Response.success(getMessage(response), HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        if(mListener!=null){
            mListener.onResponse(response);
        }
    }
}
