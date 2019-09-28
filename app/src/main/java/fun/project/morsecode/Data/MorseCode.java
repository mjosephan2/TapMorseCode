package fun.project.morsecode.Data;

import java.util.HashMap;
import java.util.Map;

public class MorseCode {
    private static Map<String,String> MORSE_CODE = new HashMap<>();
    private static Map<String,String> MORSE_CODE_LETTER_TO_CODE = new HashMap<>();

    private static void initialize(){
        MORSE_CODE.put("._","a");
        MORSE_CODE.put("_...","b");
        MORSE_CODE.put("_._.","c");
        MORSE_CODE.put("_..","d");
        MORSE_CODE.put(".","e");
        MORSE_CODE.put(".._.","f");
        MORSE_CODE.put("__.","g");
        MORSE_CODE.put("....","h");
        MORSE_CODE.put("..","i");
        MORSE_CODE.put(".___","j");
        MORSE_CODE.put("_._","k");
        MORSE_CODE.put("._..","l");
        MORSE_CODE.put("__","m");
        MORSE_CODE.put("_.","n");
        MORSE_CODE.put("___","o");
        MORSE_CODE.put(".__.","p");
        MORSE_CODE.put("__._","q");
        MORSE_CODE.put("._.","r");
        MORSE_CODE.put("...","s");
        MORSE_CODE.put("_","t");
        MORSE_CODE.put(".._","u");
        MORSE_CODE.put("..._","v");
        MORSE_CODE.put(".__","w");
        MORSE_CODE.put("_.._","x");
        MORSE_CODE.put("_.__","y");
        MORSE_CODE.put("__..","z");
        MORSE_CODE.put(".____","1");
        MORSE_CODE.put("..___","2");
        MORSE_CODE.put("...__","3");
        MORSE_CODE.put("...._","4");
        MORSE_CODE.put(".....","5");
        MORSE_CODE.put("_....","6");
        MORSE_CODE.put("__...","7");
        MORSE_CODE.put("___..","8");
        MORSE_CODE.put("____.","9");
        MORSE_CODE.put("_____","0");
        for (Map.Entry<String,String> entry: MORSE_CODE.entrySet()){
            MORSE_CODE_LETTER_TO_CODE.put(entry.getValue(),entry.getKey());
        }
    }
    public static String getLetter(String code){
        if (MORSE_CODE.isEmpty())
            initialize();
        if (MORSE_CODE.containsKey(code)){
            return MORSE_CODE.get(code);
        }
        else{return "";}
    }

    public static String getCode(char letter){
        if (MORSE_CODE_LETTER_TO_CODE.isEmpty())
            initialize();
        if (MORSE_CODE_LETTER_TO_CODE.containsKey(String.valueOf(letter))){
            return MORSE_CODE_LETTER_TO_CODE.get(String.valueOf(letter));
        }
        else{return null;}
    }
}
