package net.exkazuu.mimicdance;

public class Lessons {
    private static String[] coccoCodes = { // hard
        "みぎてを上げる\nひだりてを上げる\nみぎてを下げる\nひだりてを下げる",
        "ひだりてを上げる\nひだりてを下げる\nみぎてを上げる\nみぎてを下げる\nひだりてを上げる\nひだりてを下げる\nみぎてを上げる\nみぎてを下げる",
        "くりかえし3\nひだりてを上げる\nひだりてを下げる\nみぎてを上げる\nみぎてを下げる\nここまで",
        "くりかえし3\nひだりてを上げる\nひだりてを下げる\nここまで\nくりかえし3\nみぎてを上げる\nみぎてを下げる\nここまで",
        "もしもしろ\nひだりてを上げる\nひだりてを下げる\nもしくは\nみぎてを上げる\nみぎてを下げる\nもしおわり",
        "みぎてを上げる\nもしもしろ\nみぎてを下げる\nもしくは\nひだりてを上げる\nもしおわり",
        "くりかえし3\nもしもしろ\nみぎてを上げる\nみぎてを下げる\nもしくは\nひだりてを上げる\nひだりてを下げる\nもしおわり\nここまで",
        //"くりかえし2\nもしもしろ\nひだりてを上げる\nひだりてを下げる\nもしくは\nみぎてを上げる\nみぎてを下げる\nもしおわり\nここまで",
    };


    private static int[] maxSteps = {6, 8, 8, 8, 7, 10, 10};

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
        return coccoCodes[lessonNumber - 1].contains("くりかえし");
    }

    public static boolean hasIf(int lessonNumber) {
        return coccoCodes[lessonNumber - 1].contains("もしも");
    }
}
