package net.exkazuu.mimicdance.pages.lesson;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class LessonFragmentVariables {
    private static final String ARGS_LESSON_NUMBER = "lessonNumber";
    private static final String ARGS_CHARACTER_NUMBER = "characterNumber";

    private int lessonNumber;
    private int characterNumber;

    public static void setFragmentArguments(Fragment fragment, int lessonNumber, int characterNumber) {
        Bundle args = fragment.getArguments();
        if (args == null) {
            args = new Bundle();
            fragment.setArguments(args);
        }
        args.putInt(ARGS_LESSON_NUMBER, lessonNumber);
        args.putInt(ARGS_CHARACTER_NUMBER, characterNumber);
    }

    public LessonFragmentVariables(Bundle args) {
        lessonNumber = args.getInt(ARGS_LESSON_NUMBER);
        characterNumber = args.getInt(ARGS_CHARACTER_NUMBER);
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }
}
