package net.exkazuu.mimicdance.pages.lesson.judge;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.interpreter.RobotExecutor;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment for Lesson top page
 */
public class WrongAnswerFragment extends Fragment {
    private static final String ARGS_DIFF_COUNT = "diffCount";
    private static final String ARGS_ALMOST_CORRECT = "almostCorrect";

    @Bind(R.id.altPiyo)
    ImageView altPiyoView;
    @Bind(R.id.piyo)
    ImageView piyoView;
    @Bind(R.id.differenceCount)
    TextView diffCountView;
    @Bind(R.id.wrong_background)
    LinearLayout wrongBackground;
    @Bind(R.id.wrong_title)
    TextView wrongTitle;

    private AnimationDrawable altPiyoAnimation;
    private AnimationDrawable piyoAnimation;
    private int diffCount;
    private boolean almostCorrect;

    public static WrongAnswerFragment newInstance(int diffCount, boolean almostCorrect) {
        WrongAnswerFragment fragment = new WrongAnswerFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_DIFF_COUNT, diffCount);
        args.putBoolean(ARGS_ALMOST_CORRECT, almostCorrect);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        diffCount = args.getInt(ARGS_DIFF_COUNT);
        almostCorrect = args.getBoolean(ARGS_ALMOST_CORRECT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wrong_answer, container, false);

        ButterKnife.bind(this, root);

        diffCountView.setText(diffCount + "コのまちがいがあるよ");
        if (almostCorrect) {
            showAltPiyoAnimationForAlmostCorrect(this.getContext(), altPiyoView);
            showPiyoAnimationForAlmostCorrect(this.getContext(), piyoView);
            wrongBackground.setBackgroundColor(0xFF67E47E);
            wrongTitle.setText("おしい！！！");
        } else {
            showAltPiyoAnimationForWrongAnswer(this.getContext(), altPiyoView);
            showPiyoAnimationForWrongAnswer(this.getContext(), piyoView);
        }

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

    @OnClick(R.id.wrong_lesson_list)
    void lessonTopClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
        manager.popBackStack();
        manager.popBackStack();
    }

    @OnClick(R.id.try_again)
    void lessonEditorClicked() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
        manager.popBackStack();
    }

    // endregion

    void showAltPiyoAnimationForWrongAnswer(Context con, View v) {
        altPiyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.alt_korobu_1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.alt_korobu_2);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.alt_korobu_3);

        // 画像をアニメーションのコマとして追加していく
        altPiyoAnimation.addFrame(frame1, 1000);
        altPiyoAnimation.addFrame(frame2, 700);
        altPiyoAnimation.addFrame(frame3, 700);

        altPiyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(altPiyoAnimation);

        // アニメーション開始
        altPiyoAnimation.start();
    }

    void showPiyoAnimationForWrongAnswer(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み
        Drawable frame1 = con.getResources().getDrawable(R.drawable.korobu_1);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.korobu_2);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.korobu_3);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 1000);
        piyoAnimation.addFrame(frame2, 700);
        piyoAnimation.addFrame(frame3, 700);

        piyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }

    void showAltPiyoAnimationForAlmostCorrect(Context con, View v) {
        altPiyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.alt_korobu_3);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.alt_korobu_1);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.alt_piyo_stand);
        Drawable frame4 = con.getResources().getDrawable(R.drawable.alt_piyo_raising_hand);

        // 画像をアニメーションのコマとして追加していく
        altPiyoAnimation.addFrame(frame1, 1500);
        altPiyoAnimation.addFrame(frame2, 700);
        altPiyoAnimation.addFrame(frame3, 700);
        altPiyoAnimation.addFrame(frame4, 700);

        altPiyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(altPiyoAnimation);

        // アニメーション開始
        altPiyoAnimation.start();
    }

    void showPiyoAnimationForAlmostCorrect(Context con, View v) {
        piyoAnimation = new AnimationDrawable();

        // 画像の読み込み //
        Drawable frame1 = con.getResources().getDrawable(R.drawable.korobu_3);
        Drawable frame2 = con.getResources().getDrawable(R.drawable.korobu_1);
        Drawable frame3 = con.getResources().getDrawable(R.drawable.piyo_stand);
        Drawable frame4 = con.getResources().getDrawable(R.drawable.piyo_raising_hand);

        // 画像をアニメーションのコマとして追加していく
        piyoAnimation.addFrame(frame1, 1500);
        piyoAnimation.addFrame(frame2, 700);
        piyoAnimation.addFrame(frame3, 700);
        piyoAnimation.addFrame(frame4, 700);

        piyoAnimation.setOneShot(true);

        // ビューの背景画像にアニメーションを設定
        v.setBackgroundDrawable(piyoAnimation);

        // アニメーション開始
        piyoAnimation.start();
    }
}
