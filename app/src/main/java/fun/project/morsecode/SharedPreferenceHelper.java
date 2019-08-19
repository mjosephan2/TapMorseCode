package fun.project.morsecode;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SharedPreferenceHelper {
    private static final String key = "morseCode";
    public static void savePreferenceData(Context context,String subKey, Object object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        editor.putString(subKey,json);
        editor.apply();
    }

    public static Object loadPreferenceData(Context context, String subKey, Type type){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(subKey,null);
        return gson.fromJson(json,type);
    }
}
