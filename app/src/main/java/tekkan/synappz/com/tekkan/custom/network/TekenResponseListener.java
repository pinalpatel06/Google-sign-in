package tekkan.synappz.com.tekkan.custom.network;

/**
 * Created by Tejas Sherdiwala on 13/05/17.
 */

public interface TekenResponseListener<T> {
    public void onResponse(int requestCode, T response);
}
