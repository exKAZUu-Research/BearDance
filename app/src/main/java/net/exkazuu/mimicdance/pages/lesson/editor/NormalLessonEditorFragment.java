package net.exkazuu.mimicdance.pages.lesson.editor;

import android.os.Bundle;
import android.view.View;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.Interpreter;
import net.exkazuu.mimicdance.pages.lesson.LessonFragmentVariables;
import net.exkazuu.mimicdance.pages.lesson.judge.BaseJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.DuoJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.NormalJudgeFragment;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import java.util.List;

import jp.fkmsoft.android.framework.util.FragmentUtils;

public class NormalLessonEditorFragment extends BaseLessonEditorFragment {

    public static NormalLessonEditorFragment newInstance(int lessonNumber, int characterNumber) {
        NormalLessonEditorFragment fragment = new NormalLessonEditorFragment();
        LessonFragmentVariables.setFragmentArguments(fragment, lessonNumber, characterNumber);
        return fragment;
    }

    @Override
    void judgeClicked() {
        BaseJudgeFragment judgeFragment = NormalJudgeFragment.newInstance(lessonFragmentVariables.getLessonNumber(), lessonFragmentVariables.getCharacterNumber(), mAdapter.getAsArray());
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container, judgeFragment, true, STACK_TAG);
    }

    @Override
    void setCharacterVisibilities() {
        boolean hasIf = Lessons.hasIf(lessonFragmentVariables.getLessonNumber(), lessonFragmentVariables.getCharacterNumber());
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
