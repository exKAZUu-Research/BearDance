package net.exkazuu.mimicdance.program;

import net.exkazuu.mimicdance.BuildConfig;
import net.exkazuu.mimicdance.interpreter.EventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class CodeParserTest {
    @Test
    public void parseSimpleProgram() {
        String code = "ひだりてを上げるみぎてを上げる\nひだりてを下げる\nみぎてを下げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(code));
    }

    @Test
    public void parseLoopProgram() {
        String code = "ジャンプする\nくりかえし2\nひだりてを上げるみぎてを上げる\nみぎてを下げる\nここまで";
        String unrolledCode =
            "ジャンプする\nひだりてを上げるみぎてを上げる\nみぎてを下げる\nひだりてを上げるみぎてを上げる\nみぎてを下げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(unrolledCode));
    }

    @Test
    public void parseIfProgram() {
        String code = "みぎてを上げる\nもしもしろ\nみぎてを下げる\nもしくは\nひだりてを上げる\nもしおわり";
        String unrolledNormalCode = "みぎてを上げる\nみぎてを下げる";
        String unrolledAltCode = "みぎてを上げる\nひだりてを上げる";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(EventType.Yellow).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseEmptyIfProgram() {
        String code = "もしもしろ\nもしおわり";
        String unrolledNormalCode = "";
        String unrolledAltCode = "";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(EventType.Yellow).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseIncompleteIfProgram() {
        String code = "もしもしろ\n";
        String unrolledNormalCode = "";
        String unrolledAltCode = "";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(EventType.Yellow).getCode(), is(unrolledAltCode));
    }

    @Test
    public void parseLoopIfProgram() {
        String code = "くりかえし2\nもしもきいろ\nひだりてを上げる\nもしくは\nみぎてを上げる\nみぎてを下げる\nもしおわり\nここまで";
        String unrolledNormalCode = "みぎてを上げる\nみぎてを下げる\nみぎてを上げる\nみぎてを下げる";
        String unrolledAltCode = "ひだりてを上げる\n\nひだりてを上げる\n";
        Block block = CodeParser.parse(code);
        assertThat(block.unroll(EventType.White).getCode(), is(unrolledNormalCode));
        assertThat(block.unroll(EventType.Yellow).getCode(), is(unrolledAltCode));
    }
}
