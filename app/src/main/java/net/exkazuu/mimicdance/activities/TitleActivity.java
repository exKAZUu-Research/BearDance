package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.lesson.LessonDAO;
import net.exkazuu.mimicdance.models.lesson.LessonDAOImpl;
import net.exkazuu.mimicdance.pages.title.TitleFragment;

public class TitleActivity extends AppCompatActivity {

    private LessonDAO lessonDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArduinoManager.register(this);
        setContentView(R.layout.activity_main);

        lessonDAO = new LessonDAOImpl(this);
        lessonDAO.init();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.add(R.id.container, TitleFragment.newInstance());

            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlugManager.register(this);
        ArduinoManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlugManager.unregister(this);
        ArduinoManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ArduinoManager.unregister(this);
    }

    public LessonDAO getLessonDAO() {
        return lessonDAO;
    }
}
