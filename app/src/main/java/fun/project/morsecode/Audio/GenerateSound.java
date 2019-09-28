package fun.project.morsecode.Audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import fun.project.morsecode.Data.Constant;
import fun.project.morsecode.Data.MorseCode;
import fun.project.morsecode.Data.MorseSetting;
import fun.project.morsecode.Data.MorseTiming;

import static fun.project.morsecode.Data.Constant.SAMPLERATE;

public class GenerateSound {
    private static final String TAG = "GenerateSound";
    private static int numSamples;
    private static AudioTrack audioTrack = null;
    public GenerateSound(){};
    private static short[] genTone(int frequency){
        // create a sine wave
        int duration = 3;
        numSamples = duration * SAMPLERATE;
        short[] generatedSnd = new short[numSamples];

        // create 16 bit pcm sound array
        for (int i = 0; i < numSamples; ++i) {
            // sine wave
            generatedSnd[i++] = (short) (Math.sin(2 * Math.PI * i / ((double) SAMPLERATE /frequency)) * 32767);
        }
        return generatedSnd;
    }

    private static short[] genSentenceTone(int frequency, String sentence){
        int durations = 0;
        // dot = 1, dash = 3, word gap = 7, letter gap = 4, morse gap = 2
        ArrayList<Integer> sound = new ArrayList<>();
        String[] words = sentence.split("\\s+");
        Log.d(TAG, "genSentenceTone: words " + Arrays.toString(words));
        for (String word : words){
            char[] letters = word.toCharArray();
            Log.d(TAG, "genSentenceTone: letters " + Arrays.toString(letters));
            for (int j = 0; j < letters.length; j++){
                char letter = letters[j];
                String code = MorseCode.getCode(letter);
                Log.d(TAG, "genSentenceTone: code " + code);
                if (code != null){
                    char[] codeArray = code.toCharArray();
                    for (int i = 0; i < codeArray.length; i++){
                        char c = codeArray[i];
                        if (c == '.'){
                            durations+= MorseTiming.dotGap;
                            sound.add(1);
                        }
                        else{
                            durations+=MorseTiming.longClickDuration;
                            sound.add(3);
                        }
                        if (i < codeArray.length - 1){
                            durations+= MorseTiming.dotGap;
                            sound.add(2);
                        }
                    }
                    if (j < letters.length - 1){
                        durations+= MorseTiming.letterGap;
                        sound.add(4);
                    }
                }
            }
            durations+= MorseTiming.wordGap;
            sound.add(7);
        }
        numSamples = durations * SAMPLERATE / 1000;
        Log.d(TAG, "genSentenceTone: sound " + sound.toString());
        Log.d(TAG, "genSentenceTone: numsamples " + numSamples);
        short[] generatedSnd = new short[numSamples];

        // create 16 bit pcm sound array
        int i = 0;
        for (int value : sound) {
            boolean silence = false;
            int numSample = 0;
            switch(value){
                case 1:
                    numSample = MorseTiming.dotGap * SAMPLERATE / 1000;
                    break;
                case 2:
                    numSample = MorseTiming.dotGap * SAMPLERATE / 1000;
                    silence = true;
                    break;
                case 3:
                    numSample = MorseTiming.longClickDuration * SAMPLERATE / 1000;
                    break;
                case 4:
                    numSample = MorseTiming.letterGap * SAMPLERATE / 1000;
                    silence = true;
                    break;
                case 7:
                    numSample = MorseTiming.wordGap* SAMPLERATE / 1000;
                    silence = true;
                    break;
            }
            numSample += i;
            while (i < numSample){
                // sine wave
                generatedSnd[i++] = !silence ? (short) (Math.sin(2 * Math.PI * i / ((double) SAMPLERATE /frequency)) * 32767): 0;
            }
        }
        Log.d(TAG, "genSentenceTone: sample " + Arrays.toString(generatedSnd));
        return generatedSnd;
    }

    public static void  playSound(int frequency, String sentence){
        short[] generatedSnd = sentence == null ? genTone(frequency) : genSentenceTone(frequency,sentence.toLowerCase());
        if (audioTrack != null){
            audioTrack.pause();
            audioTrack.flush();
        }
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                Constant.SAMPLERATE, AudioFormat. CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples * 2,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, numSamples);
        audioTrack.play();
    }

    public static void stopPlay(){
        if (audioTrack != null){
            audioTrack.pause();
            audioTrack.flush();
        }
    }
}
