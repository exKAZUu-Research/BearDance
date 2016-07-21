package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.Lesson;

import java.util.ArrayList;

import butterknife.ButterKnife;

public abstract class BaseJudgeFragment extends Fragment {
    protected Handler handler;
    protected RobotExecutor robotExecutor;
    protected Lesson lesson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lesson = Lesson.loadFromArguments(getArguments());
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
}
