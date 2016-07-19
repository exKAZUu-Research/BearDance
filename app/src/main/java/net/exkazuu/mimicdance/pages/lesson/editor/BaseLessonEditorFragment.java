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
import android.widget.Button;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.models.program.ProgramDAO;
import net.exkazuu.mimicdance.pages.editor.EditorFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.BaseJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.DuoJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.NormalJudgeFragment;
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
public abstract class BaseLessonEditorFragment extends EditorFragment {
    public static final String ARGS_LESSON_NUMBER = "lessonNumber";
    public static final String ARGS_CHARACTER_NUMBER = "characterNumber";
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
    @Bind(R.id.user_character_left)
    View userLeftCharacterView;
    @Bind(R.id.user_character_right)
    View userRightCharacterView;
    @Bind(R.id.button_judge)
    Button judgeButton;


    private Handler handler;
    private RobotExecutor robotExecutor;
    protected int lessonNumber;
    protected int characterNumber;
    private CharacterSprite leftCharacterSprite;
    private CharacterSprite rightCharacterSprite;
    private CharacterSprite userLeftCharacterSprite;
    private CharacterSprite userRightCharacterSprite;


    public static BaseLessonEditorFragment newInstance(int lessonNumber, int characterNumber) {
        BaseLessonEditorFragment fragment = Lessons.isNormalLesson(lessonNumber)
            ? new NormalLessonEditorFragment()
            : new DuoLessonEditorFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
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
                List<Program> list = new ArrayList<>();
                for (int i = 0; i < Lessons.getMaxStep(lessonNumber); ++i) {
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
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
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

        setCharacterVisibilities();
        leftCharacterSprite = CharacterSprite.createCoccoLeft(leftCharacterView);
        rightCharacterSprite = CharacterSprite.createCoccoRight(rightCharacterView);
        userLeftCharacterSprite = CharacterSprite.createPiyoLeft(userLeftCharacterView);
        userRightCharacterSprite = CharacterSprite.createPiyoRight(userRightCharacterView);
        this.handler = new Handler();

        if (mTabLayout.getTabCount() >= 5) {
            position2Group = Lists.newArrayList(0, 1, 2, 3, 4);
            mTabLayout.removeTabAt(4);
            position2Group.remove(4);
            if (!Lessons.hasIf(lessonNumber, characterNumber)) {
                mTabLayout.removeTabAt(3);
                mTabLayout.removeTabAt(2);
                position2Group.remove(3);
                position2Group.remove(2);
            }

            if (!Lessons.hasLoop(lessonNumber, characterNumber)) {
                mTabLayout.removeTabAt(1);
                position2Group.remove(1);
            }
        }

        return root;
    }

    abstract void setCharacterVisibilities();

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
    abstract void judgeClicked();

    @OnClick({R.id.character_left, R.id.character_right})
    void checkProgramClicked() {
        String answerCode = Lessons.getCoccoCode(lessonNumber, characterNumber);
        UnrolledProgram leftProgram = CodeParser.parse(answerCode).unroll(EventType.White);
        UnrolledProgram rightProgram = CodeParser.parse(answerCode).unroll(EventType.Yellow);
        checkProgram(leftProgram, rightProgram, leftCharacterSprite, rightCharacterSprite);
    }

    @OnClick({R.id.user_character_left, R.id.user_character_right})
    void checkUserProgramClicked() {
        List<Program> userProgramList = mAdapter.getAsList();
        UnrolledProgram leftProgram = CodeParser.parse(userProgramList).unroll(EventType.White);
        UnrolledProgram rightProgram = CodeParser.parse(userProgramList).unroll(EventType.Yellow);
        checkProgram(leftProgram, rightProgram, userLeftCharacterSprite, userRightCharacterSprite);
    }

    void checkProgram(UnrolledProgram leftProgram, UnrolledProgram rightProgram, CharacterSprite leftCharacterSprite, CharacterSprite rightCharacterSprite) {
        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        List<Interpreter> interpreters = getInterpreters(leftProgram, rightProgram, leftCharacterSprite, rightCharacterSprite);
        robotExecutor = new RobotExecutor(interpreters, handler, 300);
        robotExecutor.start();
    }

    abstract List<Interpreter> getInterpreters(UnrolledProgram leftProgram, UnrolledProgram rightProgram, CharacterSprite leftCharacterSprite, CharacterSprite rightCharacterSprite);
}
