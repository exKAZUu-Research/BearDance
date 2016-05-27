package net.exkazuu.mimicdance.activities;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Set;

import net.exkazuu.mimicdance.interpreter.ActionType;
import net.exkazuu.mimicdance.interpreter.EventType;
import net.exkazuu.mimicdance.models.program.Command;
import net.exkazuu.mimicdance.models.program.Program;
import net.exkazuu.mimicdance.program.Block;
import net.exkazuu.mimicdance.program.CodeParser;
import net.exkazuu.mimicdance.program.UnrolledProgram;

public class MiniBearHandler {

    private final Block program;
    private final Bear bear;

    public MiniBearHandler(List<Program> programList, Bear bear) {
        this.program = CodeParser.parse(programList);
        this.bear = bear;
    }

    public void main(EventType eventType) {
        UnrolledProgram unrolledProgram = this.program.unroll(eventType);
        for (int i = 0; i < unrolledProgram.size(); i++) {
            Set<ActionType> actionSet = unrolledProgram.getActionSet(i);
            for (ActionType actionType : actionSet) {
                switch (actionType) {
                    case LeftHandUp:
                        bear.raiseLeftHand();
                        break;
                    case LeftHandDown:
                        bear.downLeftHand();
                        break;
                    case RightHandUp:
                        bear.raiseRightHand();
                        break;
                    case RightHandDown:
                        bear.downRightHand();
                        break;
                }
            }
            bear.excecute();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
