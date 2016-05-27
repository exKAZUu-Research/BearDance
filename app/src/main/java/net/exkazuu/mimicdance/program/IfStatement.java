package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.interpreter.ActionType;
import net.exkazuu.mimicdance.interpreter.EventType;

import java.util.HashSet;

public class IfStatement extends Statement {
    private final Block trueBlock;
    private final Block falseBlock;
    private final EventType eventType;

    public IfStatement(Block trueBlock, Block falseBlock, EventType eventType) {
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;
        this.eventType = eventType;
    }

    @Override
    public void unroll(UnrolledProgram program, EventType eventType) {
        Block statements, others;
        if (this.eventType == eventType) {
            statements = trueBlock;
            others = falseBlock;
        } else {
            statements = falseBlock;
            others = trueBlock;
        }
        int initialSize = program.actionSets.size();
        for (Statement statement : statements) {
            statement.unroll(program, eventType);
        }

        int lastLineIndex = 0;
        if (program.actionSets.size() > 0) {
            lastLineIndex = program.lineIndexes.get(program.lineIndexes.size() - 1);
        }

        int otherSize = getOtherSize(others, eventType);
        for (int i = program.actionSets.size() - initialSize; i < otherSize; i++) {
            program.actionSets.add(new HashSet<ActionType>());
            program.lineIndexes.add(lastLineIndex);
        }
    }

    private int getOtherSize(Block others, EventType eventType) {
        UnrolledProgram program = new UnrolledProgram();
        for (Statement statement : others) {
            statement.unroll(program, eventType);
        }
        return program.actionSets.size();
    }
}
