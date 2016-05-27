package net.exkazuu.mimicdance.pages.notification;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.models.program.ProgramDAO;
import net.exkazuu.mimicdance.models.program.ProgramDAOImpl;
import net.exkazuu.mimicdance.pages.editor.EditorFragment;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Editor for Notification
 */
public class NotificationEditorFragment extends EditorFragment {
    @Bind(R.id.root)
    View mRootView2;
    @Bind(R.id.toolbar)
    Toolbar mToolbar2;
    @Bind(R.id.tablayout)
    TabLayout mTabLayout2;
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView2;
    @Bind(R.id.character)
    View characterView;
    private CharacterSprite character;
    private Handler handler;
    private RobotExecutor robotExecutor;

    public static NotificationEditorFragment newInstance() {
        NotificationEditorFragment fragment = new NotificationEditorFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected ProgramDAO createProgramDAO() {
        return new ProgramDAOImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        ButterKnife.bind(this, root);
        mRootView = mRootView2;
        mToolbar = mToolbar2;
        mTabLayout = mTabLayout2;
        mRecyclerView = mRecyclerView2;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.HORIZONTAL, false));
        initTab();

        character = CharacterSprite.createKoguma(characterView);
        handler = new Handler();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
    }

    void testEvent(EventType eventType) {
        List<Program> programList = mAdapter.getAsList();

        UnrolledProgram program = CodeParser.parse(programList).unroll(eventType);

        if (robotExecutor != null) {
            robotExecutor.terminate();
        }
        robotExecutor = new RobotExecutor(Lists.newArrayList(
            Interpreter.createForPiyo(program, character, null)), handler, 300);
        robotExecutor.start();
    }

    @OnClick(R.id.button_gmail)
    void testGmailClicked() {
        testEvent(EventType.Gmail);
    }

    @OnClick(R.id.button_facebook)
    void testFacebookClicked() {
        testEvent(EventType.Facebook);
    }

    @OnClick(R.id.button_twitter)
    void testTwitterClicked() {
        testEvent(EventType.Twitter);
    }

    @OnClick(R.id.button_calendar)
    void testCalendarClicked() {
        testEvent(EventType.Calendar);
    }
}
