package tekkan.synappz.com.tekkan.custom.network;

import com.android.volley.VolleyError;

/**
 * Created by Tejas Sherdiwala on 13/05/17.
 */

public interface TekenErrorListener {
    void onErrorResponse(int requestCode, VolleyError error, int status, String message);
}
