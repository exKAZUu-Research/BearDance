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
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";
    private static final String ARGS_USER_PROGRAM_LIST = "userProgramList";
    protected ArrayList<Program> programList;
    protected int lessonNumber;
    protected int characterNumber;
    protected Handler handler;
    protected RobotExecutor robotExecutor;

    public static BaseJudgeFragment newInstance(int lessonNumber, int characterNumber, Program[] programList) {
        BaseJudgeFragment fragment = Lessons.isNormalLesson(lessonNumber) ? new NormalJudgeFragment() : new DuoJudgeFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
        args.putParcelableArray(ARGS_USER_PROGRAM_LIST, programList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        this.characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
        this.programList = new ArrayList<>();
        Parcelable[] list = args.getParcelableArray(ARGS_USER_PROGRAM_LIST);
        if (list != null) {
            for (Parcelable p : list) {
                this.programList.add((Program) p);
            }
        }
        this.handler = new Handler();
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
