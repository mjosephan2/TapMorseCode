package fun.project.morsecode.Data;

public class MorseSetting {
    private int wpm;
    private int speed_gap;
    private int frequency;

    public MorseSetting(int wpm, int speed_gap, int frequency) {
        this.wpm = wpm;
        this.speed_gap = speed_gap;
        this.frequency = frequency;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public int getSpeed_gap() {
        return speed_gap;
    }

    public void setSpeed_gap(int speed_gap) {
        this.speed_gap = speed_gap;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
