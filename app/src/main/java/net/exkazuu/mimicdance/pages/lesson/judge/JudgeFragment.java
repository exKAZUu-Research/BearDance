package net.exkazuu.mimicdance.pages.lesson.judge;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import net.exkazuu.mimicdance.pages.lesson.editor.LessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Lesson top page
 */
public class JudgeFragment extends Fragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_USER_PROGRAM_LIST = "userProgramList";

    private int lessonNumber;

    @Bind(R.id.user_character)
    View userCharacter;
    @Bind(R.id.answer_character)
    View answerCharacter;
    @Bind(R.id.user_code)
    TextView userCodeView;

    private CharacterSprite userCharacterSprite;
    private CharacterSprite answerCharacterSprite;

    private Handler handler;
    private RobotExecutor robotExecutor;
    private ArrayList<Program> programList;

    public static JudgeFragment newInstance(int lessonNumber, Program[] programList) {
        JudgeFragment fragment = new JudgeFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putParcelableArray(ARGS_USER_PROGRAM_LIST, programList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        this.programList = new ArrayList<>();
        Parcelable[] list = args.getParcelableArray(ARGS_USER_PROGRAM_LIST);
        if (list != null) {
            for (Parcelable p : list) {
                this.programList.add((Program) p);
            }
        }
        this.handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_judge, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userCharacterSprite = CharacterSprite.createPiyoLeft(userCharacter);
        answerCharacterSprite = CharacterSprite.createCoccoLeft(answerCharacter);
        userCodeView.setText(Joiner.on("\n").skipNulls().join(Program.getCodeLines(programList)));

        String answerCode = Lessons.getCoccoCode(lessonNumber);
        Block userProgram = CodeParser.parse(programList);
        Block answerProgram = CodeParser.parse(answerCode);
        final UnrolledProgram userUnrolledProgram = userProgram.unroll(EventType.White);
        final UnrolledProgram answerUnrolledProgram = answerProgram.unroll(EventType.White);
        final UnrolledProgram altUserUnrolledProgram = userProgram.unroll(EventType.Yellow);
        final UnrolledProgram altAnswerUnrolledProgram = answerProgram.unroll(EventType.Yellow);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }

        robotExecutor = new RobotExecutor(Lists.newArrayList(Interpreter.createForPiyo(userUnrolledProgram, userCharacterSprite, userCodeView),
            Interpreter.createForCocco(answerUnrolledProgram, answerCharacterSprite)), handler) {
            @Override
            public void afterRun() {
                int diffCount = userUnrolledProgram.countDifferences(answerUnrolledProgram);
                int size = answerUnrolledProgram.size();
                if (Lessons.hasIf(lessonNumber)) {
                    diffCount += altUserUnrolledProgram.countDifferences(altAnswerUnrolledProgram);
                    size += altAnswerUnrolledProgram.size();
                }
                if (diffCount == 0) {
                    //startCorrectAnswerActivity(lessonNumber, piyoCode, true);
                } else {
                    boolean almostCorrect = diffCount <= size / 3;
                    FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
                        WrongAnswerFragment.newInstance(diffCount, almostCorrect), true);
                }
            }
        };

        robotExecutor.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
        robotExecutor.terminate();
    }

    // region UI event

    @OnClick(R.id.button_lesson_top)
    void lessonTopClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
        manager.popBackStack();
    }

    @OnClick(R.id.button_lesson_editor)
    void lessonEditorClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }

    // endregion
}
