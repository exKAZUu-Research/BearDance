package net.exkazuu.mimicdance;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import net.exkazuu.mimicdance.viewholder.PlayerInfo;

public class Timer {
    private static long start;
    private static long end;
    private static int flag = 0;

    private static PlayerInfo p;

    public static void start(int n, int m) {
        if (flag == 0) {    //Timer is not working
            p = new PlayerInfo(n, m);
            Log.v("timer", "timer started:" + n + ", " + "mode:" + m);
            start = System.currentTimeMillis();
            flag = 1;
        }
    }

    public static long stop() {
        if (flag == 1) {    //Timer is working
            end = System.currentTimeMillis();
            flag = 0;
            p.saveTime(end - start);
            Log.v("timer", "timer ended in " + p.time/1000 + "sec, " + p.num + ", " + "mode:" + p.mode);
            return end - start;
        } else return 0;
    }

    public static long getTime() {
        return (end - start) / 1000;
    }
}
