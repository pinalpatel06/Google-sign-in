package tekkan.synappz.com.tekkan.utils;

import android.net.Uri;

/**
 * Created by Tejas Sherdiwala on 5/9/2017.
 * &copy; Knoxpo
 */

public class Constants {

    public static class Api {

        private static final String
                SCHEME = "http",
                END_POINT = "dev.bayerpetcare.nl",
                PATH_SCRIPT = "scripts",
                PATH_WEB_SERVICES = "webservice.app.php",
                QUERY_PARAMETER_KEY = "m";

        public static final String
                QUERY_PARAMETER1 = "p1",
                QUERY_PARAMETER2 = "p2";

        private static final Uri
                BASE_URI = new Uri.Builder()
                .scheme(SCHEME)
                .authority(END_POINT)
                .appendPath(PATH_SCRIPT)
                .appendPath(PATH_WEB_SERVICES)
                .build();

        public static final String
                FUNC_LOGIN = "Login",
                FUNC_USER = "GetUser",
                FUNC_CREATE_USER = "CreateUser",
                FUNC_EDIT_USER = "EditUser",
                FUNC_CREATE_TICK__REPORT = "CreateTickReport",
                FUNC_COUNT_FREE_CODES = "CountFreeCodes",
                FUNC_GET_TICK = "GetTickReports",
                FUNC_GET_PRODUCTS = "GetProducts",
                FUNC_GET_INFORMATIONS = "GetInformations",
                FUNC_GET_TIPS = "GetTips",
                FUNC_GET_BREEDS = "GetBreeds",
                FUNC_EDIT_ANIMAL = "EditAnimal",
                FUNC_DELETE_ANIMAL = "DeleteAnimal",
                FUNC_GET_ANIMALS_BY_USER = "GetAnimalsbyUser",
                FUNC_CREATE_ANIMAL = "CreateAnimal";

        public static String getUrl(String funcName) {
            Uri.Builder builder = BASE_URI.buildUpon();
            builder.appendQueryParameter(QUERY_PARAMETER_KEY, funcName);
            return builder.build().toString();
        }
    }

    public static class SP {
        public static final String
                BOOLEAN_LOGED_IN = "loged_in";
    }

    public enum Gender {
        MALE("M"),
        FEMALE("F");

        private String mString;

        Gender(String string) {
            mString = string;
        }

        public String toApi() {
            return mString;
        }
    }

    public enum PetType{
        DOG("D"),
        CAT("C");

        private String mString;

        PetType(String string){
            mString = string;
        }

        public String toApi(){
            return mString;
        }
    }


}
