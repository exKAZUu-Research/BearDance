package net.exkazuu.mimicdance.pages.lesson.editor;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Constants;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.models.program.ProgramDAO;
import net.exkazuu.mimicdance.pages.editor.EditorFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.JudgeFragment;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Editor for Lesson
 */
public class LessonEditorFragment extends EditorFragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    public static final String STACK_TAG = "lessonEdit";

    @Bind(R.id.root)
    View mRootView2;
    @Bind(R.id.toolbar)
    Toolbar mToolbar2;
    @Bind(R.id.tablayout)
    TabLayout mTabLayout2;
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView2;
    @Bind(R.id.character_left)
    View leftCharacterView;
    @Bind(R.id.character_right)
    View rightCharacterView;
    private Handler handler;
    private RobotExecutor robotExecutor;
    private int lessonNumber;
    private CharacterSprite leftCharacter;
    private CharacterSprite rightCharacter;

    public static LessonEditorFragment newInstance(int lessonNumber) {
        LessonEditorFragment fragment = new LessonEditorFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected ProgramDAO createProgramDAO() {
        return new ProgramDAO() {
            @Override
            public void save(List<Program> programList) {
            }

            @Override
            public List<Program> load() {
                List<Program> list = new ArrayList<>(Constants.NUM_PROGRAM_LINE);
                for (int i = 0; i < Constants.NUM_PROGRAM_LINE; ++i) {
                    list.add(new Program());
                }
                return list;
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_editor, container, false);

        ButterKnife.bind(this, root);
        mRootView = mRootView2;
        mToolbar = mToolbar2;
        mTabLayout = mTabLayout2;
        mRecyclerView = mRecyclerView2;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.HORIZONTAL, false));
        initTab();

        leftCharacter = CharacterSprite.createCoccoLeft(leftCharacterView);
        rightCharacter = CharacterSprite.createCoccoRight(rightCharacterView);
        this.handler = new Handler();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_lesson_editor, menu);
    }

    @OnClick(R.id.button_back)
    void backClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }

    @OnClick(R.id.button_judge)
    void judgeClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            JudgeFragment.newInstance(lessonNumber, mAdapter.getAsArray()), true, STACK_TAG);
    }

    @OnClick({R.id.character_left, R.id.character_right})
    void checkProgramClicked() {
        // Get current program
        List<Program> programList = mAdapter.getAsList();

        String answerCode = Lessons.getCoccoCode(lessonNumber);
        UnrolledProgram leftProgram = CodeParser.parse(answerCode).unroll(EventType.White);
        UnrolledProgram rightProgram = CodeParser.parse(answerCode).unroll(EventType.Yellow);
        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        robotExecutor = new RobotExecutor(Lists.newArrayList(
            Interpreter.createForCocco(leftProgram, leftCharacter),
            Interpreter.createForCocco(rightProgram, rightCharacter)), handler, 300);
        robotExecutor.start();
    }
}
