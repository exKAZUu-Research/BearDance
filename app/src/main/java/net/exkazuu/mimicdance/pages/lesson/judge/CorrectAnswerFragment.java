package net.exkazuu.mimicdance.pages.lesson.judge;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.pages.lesson.top.LessonTopFragment;
import net.exkazuu.mimicdance.pages.title.TitleFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Lesson top page
 */
public class CorrectAnswerFragment extends Fragment {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";

    @Bind(R.id.cocco)
    ImageView coccoView;
    @Bind(R.id.piyo)
    ImageView piyoView;

    private AnimationDrawable coccoAnimation;
    private AnimationDrawable piyoAnimation;
    private int lessonNumber;

    public static CorrectAnswerFragment newInstance(int lessonNumber) {
        CorrectAnswerFragment fragment = new CorrectAnswerFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_correct_answer, container, false);

        ButterKnife.bind(this, root);

        startCoccoAnimation(this.getContext(), coccoView);
        startPiyoAnimation(this.getContext(), piyoView);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }

    // region UI event

    @OnClick(R.id.lesson_list)
    void lessonTopClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
        manager.popBackStack();
        manager.popBackStack();
    }

    @OnClick(R.id.next_lesson)
    void nextLessonClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        if (lessonNumber + 1 > Lessons.getLessonCount()) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, TitleFragment.newInstance());
            transaction.commit();
        } else {
            manager.popBackStack();
            manager.popBackStack();
            manager.popBackStack();
            manager.popBackStack();
            FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
                LessonTopFragment.newInstance(Math.min(lessonNumber + 1, Lessons.getLessonCount())), true);
        }
    }

    // endregion

    void startCoccoAnimation(Context con, View v) {
        coccoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.cocco_jump1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.cocco_jump2);

        // 画像をアニメーションのコマとして追加していく
        coccoAnimation.addFrame(frame1, 500);
        coccoAnimation.addFrame(frame2, 500);

        // 繰り返し設定
        coccoAnimation.setOneShot(false);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(coccoAnimation);

        // アニメーション開始
        coccoAnimation.start();
    }

    void startPiyoAnimation(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.piyo_jump1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.piyo_jump2);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 500);
        piyoAnimation.addFrame(frame2, 500);

        // 繰り返し設定
        piyoAnimation.setOneShot(false);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }
}
