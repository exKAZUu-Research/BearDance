package net.exkazuu.mimicdance.pages.lesson.top;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.Lesson;
import net.exkazuu.mimicdance.pages.lesson.editor.DuoLessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.UnrolledProgram;

import jp.fkmsoft.android.framework.util.FragmentUtils;

/**
 * Fragment for Duo lesson top page
 */
public class DuoLessonTopFragment extends BaseLessonTopFragment {
    public static DuoLessonTopFragment newInstance(Lesson lesson) {
        DuoLessonTopFragment fragment = new DuoLessonTopFragment();
        lesson.saveToArguments(fragment);
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
            DuoLessonEditorFragment.newInstance(lesson), true);
    }
}
