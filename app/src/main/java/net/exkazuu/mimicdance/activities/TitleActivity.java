package net.exkazuu.mimicdance.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

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
        View decorView = this.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

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
