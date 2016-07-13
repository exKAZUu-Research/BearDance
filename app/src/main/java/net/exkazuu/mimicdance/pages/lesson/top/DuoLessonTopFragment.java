package net.exkazuu.mimicdance.pages.lesson.top;

import net.exkazuu.mimicdance.CharacterSprite;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.UnrolledProgram;

/**
 * Fragment for Duo lesson top page
 */
public class DuoLessonTopFragment extends BaseLessonTopFragment {
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
}
