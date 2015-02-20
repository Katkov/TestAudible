package audible.katkov.com.testaudible.async;

import android.util.Log;


public class WLog {

    private static boolean debug = false;

    public static void d(String TAG, String message){
        if(!debug) {
            Log.d(TAG, message);
        }
    }

    public static void i(String TAG, String message){
        if(!debug) {
            Log.i(TAG, message);
        }
    }
}
