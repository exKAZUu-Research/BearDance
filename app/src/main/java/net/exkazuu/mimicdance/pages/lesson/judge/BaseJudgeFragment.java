package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseJudgeFragment extends Fragment {
    protected int lessonNumber;
    protected int characterNumber;
    protected Handler handler;
    protected RobotExecutor robotExecutor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler();
    }

    protected static ArrayList<Program> convertParcelableArrayToProgramList(Parcelable[] parcelableArray) {
        ArrayList<Program> programList = new ArrayList<>();
        if (parcelableArray != null) {
            for (Parcelable p : parcelableArray) {
                programList.add((Program) p);
            }
        }
        return programList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
        robotExecutor.terminate();
    }

    @OnClick(R.id.button_lesson_editor)
    void lessonEditorClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }
}
