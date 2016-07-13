package net.exkazuu.mimicdance.pages.lesson.top;

import android.view.View;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.pages.lesson.editor.BaseLessonEditorFragment;
import net.exkazuu.mimicdance.pages.lesson.editor.NormalLessonEditorFragment;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.UnrolledProgram;

/**
 * Fragment for Normal lesson top page
 */
public class NormalLessonTopFragment extends BaseLessonTopFragment {

    @Override
    protected void createCharacters() {
        rightCharacterView.setVisibility(Lessons.hasIf(lessonNumber, characterNumber) ? View.VISIBLE : View.INVISIBLE);
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
}
