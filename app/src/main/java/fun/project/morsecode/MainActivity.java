package fun.project.morsecode;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;


import fun.project.morsecode.Data.MorseCode;
import fun.project.morsecode.Data.MorseSetting;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityMorse";
    private TextView result,result_morse;
    private Boolean inDoing = false;
    private int dot_duration;
    private long down;
    private long up;
    private MorseSetting morseSetting;
    private String word_code = "";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // load
        morseSetting = load();
        // using PARIS standard
        dot_duration = 1200/morseSetting.getSpeed_gap();

        result = findViewById(R.id.result);
        result_morse = findViewById(R.id.result_morse);
        Button clear = findViewById(R.id.clear);
        ImageButton tap = findViewById(R.id.button);
        Button setting = findViewById(R.id.setting_button);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWPMDialog();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("");
                result_morse.setText("");
            }
        });
        tap.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    down = System.currentTimeMillis();
                    // false allows the application to handle state event
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    up = System.currentTimeMillis();
                    if (!inDoing){
                        start();
                        inDoing = true;
                    }
                    if ((up - down) > dot_duration*3) {
                        /* Implement long click behavior here */
                        word_code+="_";
                        Log.d(TAG, "onTouch: " + word_code);
                        result_morse.append("_");
                        System.out.println("Long Click has happened!");
                        return false;
                    } else {
                        /* Implement short click behavior here or do nothing */
                        word_code+=".";
                        result_morse.append(".");
                        Log.d(TAG, "onTouch: " + word_code);
                        System.out.println("Short Click has happened...");
                        return false;
                    }
                }
                return false;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private void start(){
        Checking checking = new Checking();
        Thread thread = new Thread(checking);
        thread.start();
    }

    private MorseSetting load(){
        MorseSetting loaded = (MorseSetting) SharedPreferenceHelper.loadPreferenceData(MainActivity.this,
                "MorseSetting",
                new TypeToken<MorseSetting>(){}.getType());
        if (loaded==null)
            return new MorseSetting(12,12,1000);
        else
            return loaded;
    }

    private void save(MorseSetting morseSetting){
        SharedPreferenceHelper.savePreferenceData(MainActivity.this,"MorseSetting",morseSetting);
    }

    @SuppressLint("SetTextI18n")
    private void createWPMDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        // the root  does not matter
        View mView = getLayoutInflater().inflate(R.layout.wpm_dialog,null);

        // initialize the setting
        int wpm_val = morseSetting.getWpm();
        int speed_gap_val = morseSetting.getSpeed_gap();
        int frequency_val = morseSetting.getFrequency();
        // for wpm and speed_gap
        final int min = 5;
        int max = 30;

        // for frequency
        final int minf = 400;
        int maxf = 1000;

        // initialize UI
        final SeekBar wpm = mView.findViewById(R.id.wpm_seekBar);
        final SeekBar speed_gap = mView.findViewById(R.id.speed_gap_seekBar);
        final SeekBar frequency = mView.findViewById(R.id.freq_seekBar);
        final TextView wpm_textView_val = mView.findViewById(R.id.wpm_textView_val);
        final TextView speed_gap_textView_val = mView.findViewById(R.id.speed_gap_textView_val);
        final TextView freq_textView_val = mView.findViewById(R.id.freq_textView_val);

        wpm.setMax(max-min);
        speed_gap.setMax(max-min);
        frequency.setMax(maxf-minf);

        wpm.setProgress(wpm_val-min);
        speed_gap.setProgress(speed_gap_val-min);
        frequency.setProgress(frequency_val-minf);

        wpm_textView_val.setText(Integer.toString(wpm_val));
        speed_gap_textView_val.setText(Integer.toString(speed_gap_val));
        freq_textView_val.setText(Integer.toString(frequency_val));

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (seekBar.getId()){
                    case R.id.wpm_seekBar:
                        wpm_textView_val.setText(Integer.toString(i+min));
                        break;
                    case R.id.speed_gap_seekBar:
                        speed_gap_textView_val.setText(Integer.toString(i+min));
                        break;
                    case R.id.freq_seekBar:
                        freq_textView_val.setText(Integer.toString(i+minf));
                        break;

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        // add the listener

        wpm.setOnSeekBarChangeListener(onSeekBarChangeListener);
        speed_gap.setOnSeekBarChangeListener(onSeekBarChangeListener);
        frequency.setOnSeekBarChangeListener(onSeekBarChangeListener);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // variable must be declared final
                // replace the current morseSetting
                morseSetting = new MorseSetting(wpm.getProgress()+min,speed_gap.getProgress()+min,frequency.getProgress()+minf);
                save(morseSetting);
            }
        });
        dialog.show();
    }
    class Checking implements Runnable{
        private boolean added_space = false;
        private long time_checking_timing = 20;
        private int letter_gap = 3*dot_duration;
        private int word_gap = 7*dot_duration;
        private int done_gap = 14*dot_duration;

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        @Override
        public void run() {
            Log.d(TAG, "run: Start");
            while(inDoing){
                // delay 20 millis
                sleep(time_checking_timing);
                if (down>up)
                    continue;
                Log.d(TAG+"check", "run: "+ Math.round(System.currentTimeMillis()-up));
                if ((System.currentTimeMillis()-up)>done_gap){
                    inDoing = false;
                    Log.d(TAG, "run: Stop");
                }
                else if ((System.currentTimeMillis()-up)>word_gap){
                    if (!added_space){
                        result.append(" ");
                        result_morse.append("  ");
                        added_space = true;
                    }
                }
                else if ((System.currentTimeMillis()-up)>letter_gap){
                    // call only when the word code is not empty
                    if (!word_code.equals("")){
                        result.append(MorseCode.getLetter(word_code));
                        result_morse.append(" ");
                        word_code = "";
                        added_space = false;
                    }
                }
            }
        }
    }
}
