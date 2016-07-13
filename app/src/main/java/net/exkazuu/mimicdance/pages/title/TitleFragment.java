package net.exkazuu.mimicdance.pages.title;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.exkazuu.mimicdance.BuildConfig;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.activities.TitleActivity;
import net.exkazuu.mimicdance.models.APIClient;
import net.exkazuu.mimicdance.models.lesson.LessonDAO;
import net.exkazuu.mimicdance.pages.lesson.list.LessonListFragment;
import net.exkazuu.mimicdance.pages.notification.NotificationEditorFragment;
import net.exkazuu.mimicdance.pages.settings.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * タイトル画面
 */
public class TitleFragment extends Fragment {

    private LessonDAO lessonDAO;

    @Bind(R.id.duo_button)
    Button duoButton;

    public static TitleFragment newInstance() {
        TitleFragment fragment = new TitleFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.title, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TitleActivity activity = (TitleActivity) getActivity();
        lessonDAO = activity.getLessonDAO();
    }

    @Override
    public void onResume() {
        super.onResume();

        APIClient.ClientType type = APIClient.getClientType(getContext());
        boolean enableDuoMode = type == APIClient.ClientType.A || type == APIClient.ClientType.B;
        duoButton.setEnabled(BuildConfig.OFFLINE_MODE || enableDuoMode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    // regions UI event

    @OnClick(R.id.help_button)
    void helpClicked() {
//        lessonDAO.upload();
//        FragmentManager manager = getFragmentManager();
//        if (manager == null) { return; }
//
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.addToBackStack("");
//        transaction.replace(R.id.container, HelpFragment.newInstance());
//        transaction.commit();

        /*
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
        */
    }


    @OnClick(R.id.notification_button)
    void notificationClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            NotificationEditorFragment.newInstance(), true);
    }

    @OnClick(R.id.start_button)
    void startClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            LessonListFragment.newInstance(true), true);
    }

    @OnClick(R.id.duo_button)
    void duoClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            LessonListFragment.newInstance(false), true);
    }

    @OnClick(R.id.setting_button)
    void settingsClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            SettingFragment.newInstance(), true);
    }

    // endregion
}
