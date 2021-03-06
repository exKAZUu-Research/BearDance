package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.interpreter.IconType;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.models.program.Program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeParser {
    private int lineIndex;

    private CodeParser() {
    }

    public static Block parse(List<Program> programs) {
        ArrayList<String> lines = Program.getCodeLines(programs);
        return new CodeParser().parseBlock(lines, new String[]{});
    }

    public static Block parse(String commandsText) {
        List<String> lines = Arrays.asList(commandsText.split("\n")); // List型配列に変換
        return new CodeParser().parseBlock(lines, new String[]{});
    }

    private Block parseBlock(List<String> lines, String[] endLineTokens) {
        ArrayList<Statement> statements = new ArrayList<>();
        for (; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex);
            if (contains(line, endLineTokens)) {
                break;
            }
            statements.add(parseStatement(lines));
        }
        return new Block(statements);
    }

    private Statement parseStatement(List<String> lines) {
        String line = lines.get(lineIndex);
        if (line.contains(IconType.Loop.code)) {
            return parseLoop(lines);
        } else if (line.contains(IconType.If.code)) {
            return parseIf(lines);
        }
        return new Action(line, lineIndex);
    }

    private IfStatement parseIf(List<String> lines) {
        String firstLine = lines.get(lineIndex++);
        assert firstLine.contains(IconType.If.code);
        Block trueBlock = parseBlock(lines, new String[]{IconType.Else.code, IconType.EndIf.code});
        Block falseBlock;
        if (lineIndex < lines.size() && lines.get(lineIndex++).contains(IconType.Else.code)) {
            falseBlock = parseBlock(lines, new String[]{IconType.EndIf.code});
        } else {
            falseBlock = new Block();
        }
        return new IfStatement(trueBlock, falseBlock, readEventType(firstLine));

    }

    private LoopStatement parseLoop(List<String> lines) {
        String firstLine = lines.get(lineIndex++);
        assert firstLine.contains(IconType.Loop.code);
        Block block = parseBlock(lines, new String[]{IconType.EndLoop.code});
        return new LoopStatement(block, readCount(firstLine));
    }

    private static int readCount(String loopCount) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(loopCount);
        if (!m.find()) {
            return 0; // 0回繰り返し
        } else {
            return Integer.parseInt(m.group());
        }
    }

    private static EventType readEventType(String conditionString) {
        for (EventType eventType : EventType.values()) {
            if (conditionString.contains(eventType.text)) {
                return eventType;
            }
        }
        return EventType.White;
    }

    private static boolean contains(String line, String[] endLineTokens) {
        for (String endLineToken : endLineTokens) {
            if (line.contains(endLineToken)) {
                return true;
            }
        }
        return false;
    }
}
