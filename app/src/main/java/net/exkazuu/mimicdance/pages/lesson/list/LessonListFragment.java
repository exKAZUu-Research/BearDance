package net.exkazuu.mimicdance.pages.lesson.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.APIClient;
import net.exkazuu.mimicdance.pages.lesson.top.BaseLessonTopFragment;
import net.exkazuu.mimicdance.pages.lesson.top.DuoLessonTopFragment;
import net.exkazuu.mimicdance.pages.lesson.top.NormalLessonTopFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for lesson list
 */
public class LessonListFragment extends Fragment {
    private static final String ARGS_NORMAL_MODE = "normalMode";

    boolean normalMode;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    public static LessonListFragment newInstance(boolean normalMode) {
        LessonListFragment fragment = new LessonListFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARGS_NORMAL_MODE, normalMode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        normalMode = args.getBoolean(ARGS_NORMAL_MODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        ButterKnife.bind(this, root);

        toolbar.setTitle(R.string.title_lesson_list);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(this.backListener);

        Context context = inflater.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        LessonListAdapter adapter = new LessonListAdapter(context, this.clickListener);
        int lessonCount = Lessons.getLessonCount(normalMode);
        int lessonStart = Lessons.getLessonStart(normalMode);
        for (int i = lessonStart; i < lessonStart + lessonCount; ++i) {
            adapter.addItem("レッスン" + i);
        }
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager manager = getFragmentManager();
            if (manager == null) {
                return;
            }
            manager.popBackStack();
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            int lessonStart = Lessons.getLessonStart(normalMode);
            int lessonNumber = lessonStart + position;

            APIClient.ClientType clientType = APIClient.getClientType(getContext());
            int characterNumber = clientType == APIClient.ClientType.B ? 1 : 0;

            Lesson lesson = new Lesson(lessonNumber, characterNumber);
            BaseLessonTopFragment lessonTopFragment = Lessons.isNormalLesson(lessonNumber)
                ? NormalLessonTopFragment.newInstance(lesson)
                : DuoLessonTopFragment.newInstance(lesson);
            FragmentUtils.toNextFragment(getFragmentManager(), R.id.container, lessonTopFragment, true);
        }
    };
}
