package net.exkazuu.mimicdance.models.program;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program implements Parcelable {
    private String[] commands = new String[2];

    public Program() {
        this("", "");
    }

    public Program(String command0, String command1) {
        commands[0] = command0;
        commands[1] = command1;
    }

    public void setCommands(int index, String value) {
        commands[index] = value;
    }

    public String getCommand(int index) {
        return commands[index];
    }

    public String getCode() {
        List<String> codes = Lists.transform(Arrays.asList(commands), new Function<String, String>() {
            @Override
            public String apply(String command) {
                return Command.getCode(command);
            }
        });
        return Joiner.on(" ").skipNulls().join(codes);
    }

    @Override
    public String toString() {
        return String.format("\"%s\", \"%s\"", commands[0], commands[1]);
    }

    public static ArrayList<String> getCodeLines(List<Program> programs) {
        ArrayList<String> lines = Lists.newArrayList(Lists.transform(programs, new Function<Program, String>() {
            @Override
            public String apply(Program program) {
                return program.getCode();
            }
        }));
        for (int i = lines.size() - 1; i >= 0 && Strings.isNullOrEmpty(lines.get(i)); i--) {
            lines.remove(i);
        }
        return lines;
    }

    public static String getMultilineCode(List<Program> programList) {
        return Joiner.on("\n").skipNulls().join(getCodeLines(programList));
    }

    public static Program[] fromMultilineCode(String code) {
        ArrayList<Program> list = new ArrayList<>();
        for (String line : code.split("\n")) {
            String[] pair = line.split(" ", 2);
            String cmd1 = Command.fromJpCmdToEnCmd(pair[0]);
            String cmd2 = pair.length == 2 ? Command.fromJpCmdToEnCmd(pair[1]) : "";
            list.add(new Program(cmd1, cmd2));
        }
        return list.toArray(new Program[0]);
    }

    // region Parcelable

    public static Creator<Program> CREATOR = new Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel source) {
            return new Program(source.readString(), source.readString());
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commands[0]);
        dest.writeString(commands[1]);
    }

    // endregion
}
