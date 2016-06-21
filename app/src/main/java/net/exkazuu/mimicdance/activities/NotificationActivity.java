package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.program.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by t-yokoi on 2015/10/13.
 */
public class NotificationActivity extends BaseActivity {
    Program[] mCommands = new Program[12];
    String[] commands = new String[4];
    ArrayList<String> Gcom = new ArrayList<String>();
    ArrayList<String> Ccom = new ArrayList<String>();
    ArrayList<String> Tcom = new ArrayList<String>();
    ArrayList<String> Fcom = new ArrayList<String>();
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification);

        //コードの初期化
        for (int i = 0; i < 12; i++) {
            mCommands[i] = new Program();
        }

        //記述可能部分
        initializeCanWrite();
        initializeProgramIcons();


    }

    private void initializeCanWrite() {
        WritableView wv[][] = new WritableView[12][2];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 2; j++) {
                int id = this.getResources().getIdentifier("canwrite" + j + "_" + i, "id", this.getPackageName());
                Log.v("id", "i=" + i + " j=" + j + " id=" + id);
                wv[i][j] = (WritableView) findViewById(id);
                wv[i][j].setLocate(i, j);
                wv[i][j].setCommand("");

            }
        }
    }

    private void initializeProgramIcons() {
        Map<Integer, String> map = new HashMap<>();
        map.put(R.id.action_0, "right_hand_up");
        map.put(R.id.action_1, "right_hand_down");
        map.put(R.id.action_2, "left_hand_up");
        map.put(R.id.action_3, "left_hand_down");
        map.put(R.id.number_0, "num_1");
        map.put(R.id.number_1, "num_2");
        map.put(R.id.number_2, "num_3");
        map.put(R.id.number_3, "num_4");
        map.put(R.id.number_4, "num_5");
        map.put(R.id.number_5, "num_6");
        map.put(R.id.number_6, "num_7");
        map.put(R.id.number_7, "num_8");
        map.put(R.id.number_8, "num_9");
        map.put(R.id.loop_0, "loop");
        map.put(R.id.loop_1, "loop_end");
        map.put(R.id.branch_0, "if");
        map.put(R.id.branch_1, "if");

        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            int id = entry.getKey();
            ProgramIconView piv = (ProgramIconView) findViewById(id);
            piv.setVisibility(View.VISIBLE);
            piv.setCommand(entry.getValue());

        }
    }

    public void onClickReturnButton(View view) {
        finish();
    }

    public void onClickSaveButton(View view) {
        Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
        finish();
    }
}

