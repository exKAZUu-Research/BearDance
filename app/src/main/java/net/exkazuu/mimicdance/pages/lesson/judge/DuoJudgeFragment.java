package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.fkmsoft.android.framework.util.FragmentUtils;

public class DuoJudgeFragment extends BaseJudgeFragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";
    private static final String ARGS_LEFT_USER_PROGRAM_LIST = "leftUserProgramList";
    private static final String ARGS_RIGHT_USER_PROGRAM_LIST = "rightUserProgramList";

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

    private CharacterSprite userCharacterSprite;
    private CharacterSprite answerCharacterSprite;
    protected ArrayList<Program> leftProgramList;
    protected ArrayList<Program> rightProgramList;

    public static DuoJudgeFragment newInstance(int lessonNumber, int characterNumber, Program[] leftProgramList, Program[] rightProgramList) {
        DuoJudgeFragment fragment = new DuoJudgeFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
        args.putParcelableArray(ARGS_LEFT_USER_PROGRAM_LIST, leftProgramList);
        args.putParcelableArray(ARGS_RIGHT_USER_PROGRAM_LIST, rightProgramList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
        leftProgramList = convertParcelableArrayToProgramList(args.getParcelableArray(ARGS_LEFT_USER_PROGRAM_LIST));
        rightProgramList = convertParcelableArrayToProgramList(args.getParcelableArray(ARGS_RIGHT_USER_PROGRAM_LIST));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_judge_duo, container, false);
        ButterKnife.bind(this, root);

        userCharacterSprite = CharacterSprite.createPiyoLeft(leftUserCharacter);
        answerCharacterSprite = CharacterSprite.createCoccoLeft(leftAnswerCharacter);
        leftUserCodeView.setText(Program.getMultilineCode(leftProgramList));
        rightUserCodeView.setText(Program.getMultilineCode(rightProgramList));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final UnrolledProgram leftUserUnrolledProgram = UnrolledProgram.convertFromCode(leftProgramList, EventType.White);
        final UnrolledProgram rightUserUnrolledProgram = UnrolledProgram.convertFromCode(rightProgramList, EventType.White);
        String leftAnswerCode = Lessons.getCoccoCode(lessonNumber, 0);
        String rightAnswerCode = Lessons.getCoccoCode(lessonNumber, 1);
        final UnrolledProgram leftAnswerUnrolledProgram = UnrolledProgram.convertFromCode(leftAnswerCode, EventType.White);
        final UnrolledProgram rightAnswerUnrolledProgram = UnrolledProgram.convertFromCode(rightAnswerCode, EventType.White);

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
                        CorrectAnswerFragment.newInstance(lessonNumber, characterNumber), true);
                } else {
                    boolean almostCorrect = diffCount <= size / 3;
                    FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
                        WrongAnswerFragment.newInstance(diffCount, almostCorrect), true);
                }
            }
        };

        List<Interpreter> interpreters = Lists.newArrayList(
            Interpreter.createForPiyo(leftUserUnrolledProgram, userCharacterSprite, leftUserCodeView),
            Interpreter.createForPiyo(rightUserUnrolledProgram, userCharacterSprite, rightUserCodeView),
            Interpreter.createForCocco(leftAnswerUnrolledProgram, answerCharacterSprite),
            Interpreter.createForCocco(rightAnswerUnrolledProgram, answerCharacterSprite)
        );
        robotExecutor = new RobotExecutor(interpreters, handler) {
            @Override
            public void afterRun() {
                judge.run();
            }
        };

        robotExecutor.start();
    }
}
