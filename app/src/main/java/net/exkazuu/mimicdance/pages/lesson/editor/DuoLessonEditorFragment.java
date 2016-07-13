package net.exkazuu.mimicdance.pages.lesson.editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.APIClient;

import java.util.Timer;
import java.util.TimerTask;

public class DuoLessonEditorFragment extends BaseLessonEditorFragment {

    private static final int SECONDS = 1000;
    private Timer timer;

    @Override
    public void onResume() {
        super.onResume();
        this.timer = new Timer();
        timer.schedule(checkPartnerOnline(), 0, 10 * SECONDS);
    }

    @Override
    public void onPause() {
        timer.cancel();
        timer = null;
        super.onPause();
    }


    private TimerTask checkPartnerOnline() {
        return new TimerTask() {
            @Override
            public void run() {
                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        return APIClient.connect(getContext(), String.valueOf(lessonNumber));
                    }

                    @Override
                    protected void onPostExecute(Boolean isPartnerOnline) {
                        changeJudgeButtonState(isPartnerOnline);
                    }
                }.execute();
            }
        };
    }

    private void changeJudgeButtonState(boolean isPartnerOnline) {
        if (isPartnerOnline) {
            judgeButton.setText(R.string.check_answer);
            judgeButton.setEnabled(true);
        } else {
            judgeButton.setText(R.string.partner_is_offline);
            judgeButton.setEnabled(false);
        }
    }
}
