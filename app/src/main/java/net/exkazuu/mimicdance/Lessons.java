package net.exkazuu.mimicdance;

import net.exkazuu.mimicdance.interpreter.IconType;

public class Lessons {
    private static final String LU = IconType.LeftHandUp.code;
    private static final String LD = IconType.LeftHandDown.code;
    private static final String RU = IconType.RightHandUp.code;
    private static final String RD = IconType.RightHandDown.code;

    private static final String DO = IconType.Loop.code;
    private static final String DONE = IconType.EndLoop.code;
    private static final String IF = IconType.If.code;
    private static final String ELSE = IconType.Else.code;
    private static final String FI = IconType.EndIf.code;
    private static final String WHITE = IconType.White.code;
    private static final String _3 = IconType.Number3.code;

    private static String[] coccoCodes = {
        commands(RU, LU, RD, LD),
        commands(LU, LD, RU, RD, LU, LD, RU, RD),
        commands(DO + _3, LU, LD, RU, RD, DONE),
        commands(DO + _3, LU, LD, DONE, DO + _3, RU, RD, DONE),
        commands(IF + WHITE, LU, LD, ELSE, RU, RD, FI),
        commands(RU, IF + WHITE, RD, ELSE, LU, FI),
        commands(DO + _3, IF + WHITE, RU, RD, ELSE, LU, LD, FI, DONE),
    };

    private static int[] maxSteps = {6, 8, 8, 8, 7, 10, 10};

    private static String commands(String... args) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String str : args) {
            if(first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public static String getCoccoCode(int lessonNumber) {
        return coccoCodes[lessonNumber - 1];
    }

    public static int getMaxStep(int lessonNumber) {
        return maxSteps[lessonNumber - 1];
    }

    public static int getLessonCount() {
        return coccoCodes.length;
    }

    public static boolean hasLoop(int lessonNumber) {
        return coccoCodes[lessonNumber - 1].contains(DO);
    }

    public static boolean hasIf(int lessonNumber) {
        return coccoCodes[lessonNumber - 1].contains(IF);
    }
}
