package net.exkazuu.mimicdance.pages.lesson.top;

import android.view.View;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.pages.lesson.editor.NormalLessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Normal lesson top page
 */
public class NormalLessonTopFragment extends BaseLessonTopFragment {
    public static NormalLessonTopFragment newInstance(Lesson lesson) {
        NormalLessonTopFragment fragment = new NormalLessonTopFragment();
        lesson.saveToArguments(fragment);
        return fragment;
    }

    @Override
    protected void createCharacters() {
        boolean hasIf = lesson.hasIf();
        rightCharacterView.setVisibility(hasIf ? View.VISIBLE : View.INVISIBLE);
        leftCharacterSprite = CharacterSprite.createCoccoLeft(leftCharacterView);
        rightCharacterSprite = CharacterSprite.createCoccoRight(rightCharacterView);
    }

    @Override
    protected UnrolledProgram getLeftUnrolledProgram(Block program) {
        return program.unroll(EventType.White);
    }

    @Override
    protected UnrolledProgram getRightUnrolledProgram(Block program) {
        return program.unroll(EventType.Yellow);
    }

    @Override
    void writeClicked() {
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container,
            NormalLessonEditorFragment.newInstance(lesson), true);
    }
}
