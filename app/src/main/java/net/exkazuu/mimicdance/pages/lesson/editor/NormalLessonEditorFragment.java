package net.exkazuu.mimicdance.pages.lesson.editor;

import android.view.View;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.pages.lesson.judge.BaseJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.NormalJudgeFragment;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.List;

import jp.fkmsoft.android.framework.util.FragmentUtils;

public class NormalLessonEditorFragment extends BaseLessonEditorFragment {

    public static NormalLessonEditorFragment newInstance(Lesson lesson) {
        NormalLessonEditorFragment fragment = new NormalLessonEditorFragment();
        lesson.saveToArguments(fragment);
        return fragment;
    }

    @Override
    void judgeClicked() {
        String program = Program.getMultilineCode(mAdapter.getAsArray());
        BaseJudgeFragment judgeFragment = NormalJudgeFragment.newInstance(lesson, program);
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container, judgeFragment, true, STACK_TAG);
    }

    @Override
    void setCharacterVisibilities() {
        boolean hasIf = lesson.hasIf();
        rightCharacterView.setVisibility(hasIf ? View.VISIBLE : View.INVISIBLE);
        userRightCharacterView.setVisibility(hasIf ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    List<Interpreter> getInterpreters(UnrolledProgram leftProgram, UnrolledProgram rightProgram, CharacterSprite leftCharacterSprite, CharacterSprite rightCharacterSprite) {
        return Lists.newArrayList(
            Interpreter.createForCocco(leftProgram, leftCharacterSprite),
            Interpreter.createForCocco(rightProgram, rightCharacterSprite)
        );
    }
}
