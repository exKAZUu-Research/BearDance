package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.interpreter.ActionType;
import net.exkazuu.mimicdance.interpreter.EventType;

import java.util.Set;

public class Action extends Statement {
    private final Set<ActionType> actionSet;
    private final int lineIndex;

    public Action(String actionSet, int lineIndex) {
        this.actionSet = ActionType.parse(actionSet);
        this.lineIndex = lineIndex;
    }

    @Override
    public void unroll(UnrolledProgram program, EventType eventType) {
        program.actionSets.add(actionSet);
        program.lineIndexes.add(lineIndex);
    }
}
