package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

public class DuoJudgeFragment extends BaseJudgeFragment {
    private static final String ARGS_LEFT_USER_CODE = "leftUserCode";
    private static final String ARGS_RIGHT_USER_CODE = "rightUserCode";

    @Bind(R.id.left_user_character)
    View leftUserCharacter;
    @Bind(R.id.left_answer_character)
    View leftAnswerCharacter;
    @Bind(R.id.right_user_character)
    View rightUserCharacter;
    @Bind(R.id.right_answer_character)
    View rightAnswerCharacter;
    @Bind(R.id.left_user_code)
    TextView leftUserCodeView;
    @Bind(R.id.right_user_code)
    TextView rightUserCodeView;

    private CharacterSprite leftUserCharacterSprite;
    private CharacterSprite leftAnswerCharacterSprite;
    private CharacterSprite rightUserCharacterSprite;
    private CharacterSprite rightAnswerCharacterSprite;
    protected String leftCode;
    protected String rightCode;

    public static DuoJudgeFragment newInstance(Lesson lesson, String leftCode, String rightCode) {
        DuoJudgeFragment fragment = new DuoJudgeFragment();

        Bundle args = new Bundle();
        args.putString(ARGS_LEFT_USER_CODE, leftCode);
        args.putString(ARGS_RIGHT_USER_CODE, rightCode);
        fragment.setArguments(args);
        lesson.saveToArguments(fragment);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        lesson = Lesson.loadFromArguments(args);
        leftCode = args.getString(ARGS_LEFT_USER_CODE);
        rightCode = args.getString(ARGS_RIGHT_USER_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_judge_duo, container, false);
        ButterKnife.bind(this, root);

        leftUserCharacterSprite = CharacterSprite.createPiyoLeft(leftUserCharacter);
        rightUserCharacterSprite = CharacterSprite.createPiyoRight(rightUserCharacter);
        leftAnswerCharacterSprite = CharacterSprite.createCoccoLeft(leftAnswerCharacter);
        rightAnswerCharacterSprite = CharacterSprite.createCoccoRight(rightAnswerCharacter);
        leftUserCodeView.setText(leftCode);
        rightUserCodeView.setText(rightCode);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final UnrolledProgram leftUserUnrolledProgram = CodeParser.parse(leftCode).unroll(EventType.White);
        final UnrolledProgram rightUserUnrolledProgram = CodeParser.parse(rightCode).unroll(EventType.White);
        String leftAnswerCode = Lessons.getCoccoCode(lesson.getLessonNumber(), 0);
        String rightAnswerCode = Lessons.getCoccoCode(lesson.getLessonNumber(), 1);
        final UnrolledProgram leftAnswerUnrolledProgram = CodeParser.parse(leftAnswerCode).unroll(EventType.White);
        final UnrolledProgram rightAnswerUnrolledProgram = CodeParser.parse(rightAnswerCode).unroll(EventType.White);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }

        final Runnable judge = new Runnable() {
            @Override
            public void run() {
                int leftDiffCount = leftUserUnrolledProgram.countDifferences(leftAnswerUnrolledProgram);
                int rightDiffCount = rightUserUnrolledProgram.countDifferences(rightAnswerUnrolledProgram);
                int diffCount = leftDiffCount + rightDiffCount;
                int size = leftAnswerUnrolledProgram.size() + rightAnswerUnrolledProgram.size();
                if (diffCount == 0) {
                    FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
                        CorrectAnswerFragment.newInstance(lesson), true);
                } else {
                    boolean almostCorrect = diffCount <= size / 3;
                    FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
                        WrongAnswerFragment.newInstance(diffCount, almostCorrect), true);
                }
            }
        };

        List<Interpreter> interpreters = Lists.newArrayList(
            Interpreter.createForPiyo(leftUserUnrolledProgram, leftUserCharacterSprite, leftUserCodeView, 0),
            Interpreter.createForPiyo(rightUserUnrolledProgram, rightUserCharacterSprite, rightUserCodeView, 1),
            Interpreter.createForCocco(leftAnswerUnrolledProgram, leftAnswerCharacterSprite, 0),
            Interpreter.createForCocco(rightAnswerUnrolledProgram, rightAnswerCharacterSprite, 1)
        );
        robotExecutor = new RobotExecutor(interpreters, handler) {
            @Override
            public void afterRun() {
                judge.run();
            }
        };

        robotExecutor.start();
    }

    @OnClick(R.id.button_lesson_editor_duo)
    void lessonEditorClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }
}
