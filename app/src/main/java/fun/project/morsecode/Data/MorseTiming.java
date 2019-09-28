package fun.project.morsecode.Data;

public class MorseTiming {
    // all timing is in ms
    public static int dotGap;
    public static int letterGap;
    public static int wordGap;
    public static int done_gap;
    public static int longClickDuration;
    MorseTiming (){}

    public static void setDotDuration(int dotDuration){
        dotGap = dotDuration;
        longClickDuration = 3 * dotDuration;
        letterGap = 3 * dotGap;
        wordGap = 7 * dotGap;
        done_gap = 14 * dotGap;
    }
}
