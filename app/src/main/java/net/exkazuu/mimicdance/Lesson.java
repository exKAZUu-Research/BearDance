package net.exkazuu.mimicdance;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class Lesson {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";

    private final int lessonNumber;
    private final int characterNumber;

    // region static methods

    public static Lesson loadFromArguments(Bundle args) {
        int lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        int characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
        return new Lesson(lessonNumber, characterNumber);
    }

    // endregion

    public Lesson(int lessonNumber, int characterNumber) {
        this.lessonNumber = lessonNumber;
        this.characterNumber = characterNumber;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    public String getCoccoCode() {
        return Lessons.getCoccoCode(lessonNumber, characterNumber);
    }

    public boolean hasLoop() {
        return Lessons.hasLoop(lessonNumber, characterNumber);
    }

    public boolean hasIf() {
        return Lessons.hasIf(lessonNumber, characterNumber);
    }

    public void saveToArguments(Fragment fragment) {
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
            fragment.setArguments(args);
        }
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
    }

    public boolean hasNextLesson() {
        int maxNumber = Lessons.isNormalLesson(lessonNumber)
            ? Lessons.getLessonCount(true)
            : Lessons.getLessonCount(true) + Lessons.getLessonCount(false);
        return lessonNumber < maxNumber;
    }
}
