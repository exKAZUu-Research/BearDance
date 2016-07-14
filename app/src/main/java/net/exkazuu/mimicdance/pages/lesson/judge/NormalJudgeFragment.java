package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.fkmsoft.android.framework.util.FragmentUtils;

public class NormalJudgeFragment extends BaseJudgeFragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";
    private static final String ARGS_USER_PROGRAM_LIST = "userProgramList";

    @Bind(R.id.user_character)
    View userCharacter;
    @Bind(R.id.answer_character)
    View answerCharacter;
    @Bind(R.id.user_code)
    TextView userCodeView;
    @Bind(R.id.white_or_orange)
    TextView whiteOrYellow;

    protected ArrayList<Program> programList;
    private CharacterSprite userCharacterSprite, altUserCharacterSprite;
    private CharacterSprite answerCharacterSprite, altAnswerCharacterSprite;

    public static NormalJudgeFragment newInstance(int lessonNumber, int characterNumber, Program[] programList) {
        NormalJudgeFragment fragment = new NormalJudgeFragment();

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
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
        programList = convertParcelableArrayToProgramList(args.getParcelableArray(ARGS_USER_PROGRAM_LIST));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_judge, container, false);
        ButterKnife.bind(this, root);

        altUserCharacterSprite = CharacterSprite.createPiyoRight(userCharacter);
        altAnswerCharacterSprite = CharacterSprite.createCoccoRight(answerCharacter);
        userCharacterSprite = CharacterSprite.createPiyoLeft(userCharacter);
        answerCharacterSprite = CharacterSprite.createCoccoLeft(answerCharacter);
        userCodeView.setText(Program.getMultilineCode(programList));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String answerCode = Lessons.getCoccoCode(lessonNumber, characterNumber);
        Block userProgram = CodeParser.parse(programList);
        Block answerProgram = CodeParser.parse(answerCode);
        final UnrolledProgram userUnrolledProgram = userProgram.unroll(EventType.White);
        final UnrolledProgram answerUnrolledProgram = answerProgram.unroll(EventType.White);
        final UnrolledProgram altUserUnrolledProgram = userProgram.unroll(EventType.Yellow);
        final UnrolledProgram altAnswerUnrolledProgram = answerProgram.unroll(EventType.Yellow);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }

        final Runnable judge = new Runnable() {
            @Override
            public void run() {
                int diffCount = userUnrolledProgram.countDifferences(answerUnrolledProgram);
                int size = answerUnrolledProgram.size();
                if (Lessons.hasIf(lessonNumber, characterNumber)) {
                    diffCount += altUserUnrolledProgram.countDifferences(altAnswerUnrolledProgram);
                    size += altAnswerUnrolledProgram.size();
                }
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

        if (Lessons.hasIf(lessonNumber, characterNumber)) {
            whiteOrYellow.setText("しろいひよこのばあい");
            whiteOrYellow.setTextColor(0xFF807700);
        }

        robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForPiyo(userUnrolledProgram, userCharacterSprite, userCodeView),
            Interpreter.createForCocco(answerUnrolledProgram, answerCharacterSprite)), handler) {
            @Override
            public void afterRun() {
                if (Lessons.hasIf(lessonNumber, characterNumber)) {
                    whiteOrYellow.setText("きいろいひよこのばあい");
                    whiteOrYellow.setTextColor(0xFFFF3300);
                    robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForPiyo(altUserUnrolledProgram, altUserCharacterSprite, userCodeView),
                        Interpreter.createForCocco(altAnswerUnrolledProgram, altAnswerCharacterSprite)), handler) {
                        @Override
                        public void afterRun() {
                            judge.run();
                        }
                    };
                    robotExecutor.start();
                } else {
                    judge.run();
                }
            }
        };

        robotExecutor.start();
    }
}
