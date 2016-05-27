/**
 * Created by t-yokoi on 2015/10/06.
 */
package net.exkazuu.mimicdance.activities;

import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.EventLog;
import android.util.Log;

import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.models.program.Command;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.models.program.ProgramDAO;
import net.exkazuu.mimicdance.models.program.ProgramDAOImpl;

import java.util.ArrayList;
import java.util.List;

public class NotificationListener extends NotificationListenerService {

    private Handler handler = new Handler();
    private ProgramDAO mProgramDAO;

    // [1]
    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        final String msg = sbn.getPackageName();
        handler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Log.v("test", msg);
                ArrayList<Program> com = new ArrayList<Program>();
                int flag = 0;
                EventType eventType;
                if (msg.equals("jp.mynavi.notification.android.notificationsample")) {
                    eventType = null;
                } else if (msg.equals("com.google.android.gm")) {
                    eventType = EventType.Gmail;
                } else if (msg.equals("com.google.android.calendar")) {
                    eventType = EventType.Calendar;
                } else if (msg.equals("com.twitter.android")) {
                    eventType = EventType.Twitter;
                } else if (msg.equals("com.facebook.katana")) {
                    eventType = EventType.Facebook;
                } else {
                    eventType = null;
                    flag = 1;
                }

                if (flag != 1) {
                    List<Program> programList;

                    mProgramDAO = new ProgramDAOImpl(getApplicationContext());

                    programList = mProgramDAO.load();

                    if (eventType != null) {
                        // 再接続
                        ArduinoManager.resume();
                        MiniBearHandler miniBear = new MiniBearHandler(programList, new ArduinoBear());
                        miniBear.main(eventType);
//                        Toast.makeText(getApplicationContext(), programList.get(0).getCommand(0), Toast.LENGTH_SHORT).show();
//                        Log.v("command", programList.get(0).getCommand(0));
                    }
                    System.out.println(msg);

                }
            }
        });
    }

    // [2]
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        final String msg = sbn.getPackageName();
        handler.post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), msg + "消えた", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
