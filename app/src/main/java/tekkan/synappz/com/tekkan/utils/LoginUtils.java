package tekkan.synappz.com.tekkan.utils;

import android.util.Base64;

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class LoginUtils {

    public static String encode(String pwd) {
        try {
            String key = "Hf3rf8KB4"; // 128 bit key
            byte[] bKeys = key.getBytes();


            Key aesKey = new SecretKeySpec(Arrays.copyOf(bKeys, 16), "AES");
            Cipher cipher = Cipher.getInstance("AES");


            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encodedBytes = Base64.encode(cipher.doFinal(pwd.getBytes()), Base64.DEFAULT);
            return new String(encodedBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
