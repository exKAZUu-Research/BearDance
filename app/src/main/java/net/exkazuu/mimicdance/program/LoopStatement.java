package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.interpreter.EventType;

public class LoopStatement extends Statement {
    private final Block block;
    private final int loopCount;

    public LoopStatement(Block block, int loopCount) {
        this.block = block;
        this.loopCount = loopCount;
    }

    @Override
    public void unroll(UnrolledProgram program, EventType eventType) {
        for (int i = 0; i < loopCount; i++) {
            for (Statement statement : block) {
                statement.unroll(program, eventType);
            }
        }
    }
}
