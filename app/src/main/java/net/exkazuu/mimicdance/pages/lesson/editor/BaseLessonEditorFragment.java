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
import android.widget.TextView;

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
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Editor for Lesson
 */
public abstract class BaseLessonEditorFragment extends EditorFragment {
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
    @Bind(R.id.waiting_partner_msg)
    protected TextView waitingMsg;

    protected Lesson lesson;
    private Handler handler;
    private RobotExecutor robotExecutor;
    private CharacterSprite leftCharacterSprite;
    private CharacterSprite rightCharacterSprite;
    private CharacterSprite userLeftCharacterSprite;
    private CharacterSprite userRightCharacterSprite;

    @Override
    protected ProgramDAO createProgramDAO() {
        return new ProgramDAO() {
            @Override
            public void save(List<Program> programList) {
            }

            @Override
            public List<Program> load() {
                List<Program> list = new ArrayList<>();
                for (int i = 0; i < Lessons.getMaxStep(lesson.getLessonNumber()); ++i) {
                    list.add(new Program());
                }
                return list;
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lesson = Lesson.loadFromArguments(getArguments());
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
            if (!lesson.hasIf()) {
                mTabLayout.removeTabAt(3);
                mTabLayout.removeTabAt(2);
                position2Group.remove(3);
                position2Group.remove(2);
            }

            if (!lesson.hasLoop()) {
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
        String answerCode = lesson.getCoccoCode();
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
