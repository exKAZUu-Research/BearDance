package net.exkazuu.mimicdance.pages.lesson.editor;

import android.os.Bundle;

import net.exkazuu.mimicdance.Lessons;
import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.pages.lesson.judge.BaseJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.DuoJudgeFragment;
import net.exkazuu.mimicdance.pages.lesson.judge.NormalJudgeFragment;

import jp.fkmsoft.android.framework.util.FragmentUtils;

public class NormalLessonEditorFragment extends BaseLessonEditorFragment {

    @Override
    void judgeClicked() {
        BaseJudgeFragment judgeFragment = NormalJudgeFragment.newInstance(lessonNumber, characterNumber, mAdapter.getAsArray());
        FragmentUtils.toNextFragment(getFragmentManager(), R.id.container, judgeFragment, true, STACK_TAG);
    }

}
