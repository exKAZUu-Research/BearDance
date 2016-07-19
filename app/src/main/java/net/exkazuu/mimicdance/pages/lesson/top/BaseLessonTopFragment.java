package net.exkazuu.mimicdance.pages.lesson.top;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Base class of a fragment for Lesson top page
 */
public abstract class BaseLessonTopFragment extends Fragment {
    @Bind(R.id.character_left)
    View leftCharacterView;
    @Bind(R.id.character_right)
    View rightCharacterView;
    @Bind(R.id.image_lesson_logo)
    ImageView lessonLogoImageView;
    protected CharacterSprite leftCharacterSprite;
    protected CharacterSprite rightCharacterSprite;
    protected Lesson lesson;
    private Handler handler;
    private RobotExecutor robotExecutor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        lesson = Lesson.loadFromArguments(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_top, container, false);

        ButterKnife.bind(this, root);
        createCharacters();
        int drawableId = getResources().getIdentifier("lesson_message" + lesson.getLessonNumber(), "drawable", getContext().getPackageName());
        lessonLogoImageView.setImageResource(drawableId);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    @OnClick(R.id.button_back)
    void backClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }

    @OnClick(R.id.button_write)
    abstract void writeClicked();

    @OnClick(R.id.button_move)
    void moveClicked() {
        String leftCoccoCode = Lessons.getCoccoCode(lesson.getLessonNumber(), 0);
        String rightCoccoCode = Lessons.getCoccoCode(lesson.getLessonNumber(), 1);
        Block leftProgram = CodeParser.parse(leftCoccoCode);
        Block rightProgram = CodeParser.parse(rightCoccoCode);
        UnrolledProgram leftUnrolledProgram = getLeftUnrolledProgram(leftProgram);
        UnrolledProgram rightUnrolledProgram = getRightUnrolledProgram(rightProgram);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForCocco(leftUnrolledProgram, leftCharacterSprite, 0),
            Interpreter.createForCocco(rightUnrolledProgram, rightCharacterSprite, 1)), handler, 400);

        robotExecutor.start();
    }

    protected abstract void createCharacters();

    protected abstract UnrolledProgram getLeftUnrolledProgram(Block program);

    protected abstract UnrolledProgram getRightUnrolledProgram(Block program);
}
