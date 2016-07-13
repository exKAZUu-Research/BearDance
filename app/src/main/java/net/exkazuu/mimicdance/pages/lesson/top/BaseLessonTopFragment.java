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
import net.exkazuu.mimicdance.pages.lesson.editor.BaseLessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Base class of a fragment for Lesson top page
 */
public abstract class BaseLessonTopFragment extends Fragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";
    @Bind(R.id.character_left)
    View leftCharacterView;
    @Bind(R.id.character_right)
    View rightCharacterView;
    @Bind(R.id.image_lesson_logo)
    ImageView lessonLogoImageView;
    protected int lessonNumber;
    protected int characterNumber;
    protected CharacterSprite leftCharacterSprite;
    protected CharacterSprite rightCharacterSprite;
    private Handler handler;
    private RobotExecutor robotExecutor;

    public static BaseLessonTopFragment newInstance(int lessonNumber, int characterNumber) {
        BaseLessonTopFragment fragment = Lessons.isNormalLesson(lessonNumber) ? new NormalLessonTopFragment() : new DuoLessonTopFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);

        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_top, container, false);

        ButterKnife.bind(this, root);
        createCharacters();
        int drawableId = getResources().getIdentifier("lesson_message" + lessonNumber, "drawable", getContext().getPackageName());
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
    void writeClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
        BaseLessonEditorFragment.newInstance(lessonNumber, characterNumber), true);
    }

    @OnClick(R.id.button_move)
    void moveClicked() {
        String coccoCode = Lessons.getCoccoCode(lessonNumber, characterNumber);
        Block program = CodeParser.parse(coccoCode);
        UnrolledProgram leftUnrolledProgram = getLeftUnrolledProgram(program);
        UnrolledProgram rightUnrolledProgram = getRightUnrolledProgram(program);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForCocco(leftUnrolledProgram, leftCharacterSprite),
            Interpreter.createForCocco(rightUnrolledProgram, rightCharacterSprite)), handler, 400);

        robotExecutor.start();
    }

    protected abstract void createCharacters();

    protected abstract UnrolledProgram getLeftUnrolledProgram(Block program);

    protected abstract UnrolledProgram getRightUnrolledProgram(Block program);
}
