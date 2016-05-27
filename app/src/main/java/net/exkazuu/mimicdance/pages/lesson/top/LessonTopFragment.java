package net.exkazuu.mimicdance.pages.lesson.top;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.pages.lesson.editor.LessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Lesson top page
 */
public class LessonTopFragment extends Fragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private int lessonNumber;

    @Bind(R.id.character_left)
    View leftCharacter;
    @Bind(R.id.character_right)
    View rightCharacter;
    private CharacterSprite leftCharacterSprite;
    private CharacterSprite rightCharacterSprite;

    private Handler handler;
    private RobotExecutor robotExecutor;

    public static LessonTopFragment newInstance(int lessonNumber) {
        LessonTopFragment fragment = new LessonTopFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.lessonNumber = args.getInt(ARGS_LESSON_NUMBER);

        this.handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_top, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.leftCharacterSprite = CharacterSprite.createCoccoLeft(leftCharacter);
        this.rightCharacterSprite = CharacterSprite.createCoccoRight(rightCharacter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    // region UI event

    @OnClick(R.id.button_back)
    void backClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }

    @OnClick(R.id.button_write)
    void writeClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            LessonEditorFragment.newInstance(lessonNumber), true);
    }

    @OnClick(R.id.button_move)
    void moveClicked() {
        String coccoCode = Lessons.getCoccoCode(this.lessonNumber);
        Block program = CodeParser.parse(coccoCode);
        UnrolledProgram leftUnrolledProgram = program.unroll(true);
        UnrolledProgram rightUnrolledProgram = program.unroll(false);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForCocco(leftUnrolledProgram, leftCharacterSprite),
            Interpreter.createForCocco(rightUnrolledProgram, rightCharacterSprite)), handler);

        robotExecutor.start();
    }

    // endregion
}
