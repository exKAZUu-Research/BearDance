package net.exkazuu.mimicdance.interpreter;

import android.os.Handler;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.List;

public class RobotExecutor extends Thread {
    private final List<Interpreter> interpreters;
    private final Handler handler;
    private final int sleepTime;
    private boolean terminated;

    public RobotExecutor(List<Interpreter> interpreters, Handler handler) {
        this(interpreters, handler, 500);
    }

    public RobotExecutor(List<Interpreter> interpreters, Handler handler, int sleepTime) {
        this.interpreters = interpreters;
        this.handler = handler;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        while (!terminated) {
            if (Iterables.all(interpreters, new Predicate<Interpreter>() {
                @Override
                public boolean apply(Interpreter interpreter) {
                    return interpreter.finished();
                }
            })) {
                break;
            }

            // アニメーションは2コマから構成
            for (int j = 0; j < 2; j++) {
                for (Interpreter interpreter : interpreters) {
                    handler.post(interpreter);
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!terminated) {
            for (Interpreter interpreter : interpreters) {
                handler.post(interpreter);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    afterRun();
                }
            });
        }
    }

    public void afterRun() {
    }

    public void terminate() {
        terminated = true;
        for (Interpreter interpreter : interpreters) {
            interpreter.reset();
        }
    }
}
