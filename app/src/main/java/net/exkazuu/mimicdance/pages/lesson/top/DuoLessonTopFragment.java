package net.exkazuu.mimicdance.pages.lesson.top;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.pages.lesson.LessonFragmentVariables;
import net.exkazuu.mimicdance.pages.lesson.editor.BaseLessonEditorFragment;
import net.exkazuu.mimicdance.pages.lesson.editor.DuoLessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Duo lesson top page
 */
public class DuoLessonTopFragment extends BaseLessonTopFragment {
    public static DuoLessonTopFragment newInstance(int lessonNumber, int characterNumber) {
        DuoLessonTopFragment fragment = new DuoLessonTopFragment();
        LessonFragmentVariables.setFragmentArguments(fragment, lessonNumber, characterNumber);
        return fragment;
    }

    @Override
    protected void createCharacters() {
        leftCharacterSprite = CharacterSprite.createCoccoLeft(leftCharacterView);
        rightCharacterSprite = CharacterSprite.createCoccoRight(rightCharacterView);
    }

    @Override
    protected UnrolledProgram getLeftUnrolledProgram(Block program) {
        return program.unroll(EventType.White);
    }

    @Override
    protected UnrolledProgram getRightUnrolledProgram(Block program) {
        return program.unroll(EventType.White);
    }

    @Override
    void writeClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            DuoLessonEditorFragment.newInstance(lessonFragmentVariables.getLessonNumber(), lessonFragmentVariables.getCharacterNumber()), true);
    }
}
