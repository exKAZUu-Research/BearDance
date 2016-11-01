package net.exkazuu.mimicdance.viewholder;

/**
 * Created by t-yokoi on 2016/11/01.
 */

public class PlayerInfo {
    public int num;
    public int mode;
    public long time;

    public PlayerInfo(int n, int m) {
        num = n;
        mode = m;
    }

    public void saveTime(long t) {
        time=t;
    }
}
