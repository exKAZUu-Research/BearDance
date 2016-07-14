package net.exkazuu.mimicdance.pages.lesson.editor;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.exkazuu.mimicdance.BuildConfig;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.APIClient;
import net.exkazuu.mimicdance.models.program.Command;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.pages.lesson.judge.BaseJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.DuoJudgeFragment;

import java.util.Arrays;
import java.util.Date;

import jp.fkmsoft.android.framework.util.FragmentUtils;

public class DuoLessonEditorFragment extends BaseLessonEditorFragment {

    private static final int SECONDS = 1000;

    private Handler handler;
    private volatile boolean isReady;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.tab_duo_action)));
        position2Group.add(Command.GROUP_DUO_ACTION);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler();
        handler.post(new ConnectionTask());
    }

    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onPause();
    }

    @Override
    void judgeClicked() {
        if (BuildConfig.OFFLINE_MODE) {
            String partnerCode = Lessons.getCoccoCode(lessonNumber, characterNumber ^ 1);
            startJudge(partnerCode);
            return;
        }

        if (isReady == false) {
            isReady = true;
            handler.post(new ConnectionTask());
        }
        judgeButton.setEnabled(false); // 2回押しを防ぐ
    }

    private void startJudge(String partnerCodes) {
        isReady = false;

        Program[] partnerPrograms = Program.fromMultilineCode(partnerCodes);
        BaseJudgeFragment judgeFragment = DuoJudgeFragment.newInstance(lessonNumber, characterNumber, mAdapter.getAsArray(), partnerPrograms);
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container, judgeFragment, true, STACK_TAG);
    }

    private void changeJudgeButtonState(APIClient.PartnerState partnerState) {
        if (BuildConfig.OFFLINE_MODE || !partnerState.isNone()) {
            judgeButton.setText(R.string.check_answer);
            judgeButton.setEnabled(!isReady);
        } else {
            judgeButton.setText(R.string.partner_is_offline);
            judgeButton.setEnabled(false);
        }
    }

    private String getProgram() {
        return Program.getMultilineCode(Arrays.asList(mAdapter.getAsArray()));
    }

    class ConnectionTask implements Runnable {
        @Override
        public void run() {
            // 2個以上のタスクがhandlerにつっこまれてたら、自分以外を消す。
            handler.removeCallbacksAndMessages(null);
            if (isReady) {
                new AsyncTask<Void, Void, APIClient.PartnerState>() {

                    @Override
                    protected APIClient.PartnerState doInBackground(Void... params) {
                        return APIClient.ready(getContext(), String.valueOf(lessonNumber), getProgram());
                    }

                    @Override
                    protected void onPostExecute(final APIClient.PartnerState partnerState) {
                        Log.v("Mimic", String.format("partnerState:%s, now:%s", partnerState, new Date()));
                        if (partnerState.isReady()) {
                            long rest = Math.max(0, partnerState.playAt.getTime() - System.currentTimeMillis());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.v("Mimic", "startJudge");
                                    startJudge(partnerState.program);
                                }
                            }, rest);
                            Log.v("Mimic", "restTime: " + rest);
                        } else {
                            handler.postDelayed(ConnectionTask.this, 1 * SECONDS);
                            changeJudgeButtonState(partnerState);
                        }
                    }
                }.execute();

            } else {
                new AsyncTask<Void, Void, APIClient.PartnerState>() {

                    @Override
                    protected APIClient.PartnerState doInBackground(Void... params) {
                        return APIClient.connect(getContext(), String.valueOf(lessonNumber));
                    }

                    @Override
                    protected void onPostExecute(APIClient.PartnerState partnerState) {
                        handler.postDelayed(ConnectionTask.this, 7 * SECONDS);
                        changeJudgeButtonState(partnerState);
                    }
                }.execute();
            }
        }
    }
}
