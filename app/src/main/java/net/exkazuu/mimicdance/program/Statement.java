package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.interpreter.EventType;

public abstract class Statement {
    protected abstract void unroll(UnrolledProgram program, EventType eventType);

    public UnrolledProgram unroll(EventType eventType) {
        UnrolledProgram program = new UnrolledProgram();
        unroll(program, eventType);
        return program;
    }
}
