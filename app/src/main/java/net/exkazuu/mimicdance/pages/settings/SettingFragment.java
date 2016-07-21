package net.exkazuu.mimicdance.pages.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.collect.Lists;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.APIClient;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.editText)
    EditText editText;

    @Bind(R.id.save_button)
    Button saveButton;


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, v);

        toolbar.setTitle(R.string.settings);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        editText.setText(APIClient.getClientId(getContext()));

        return v;
    }

    @OnClick(R.id.toolbar)
    void back() {
        FragmentManager manager = getFragmentManager();
        if (manager == null) {
            return;
        }
        manager.popBackStack();
    }

    @OnTextChanged(R.id.editText)
    void textChange() {
        String clientId = editText.getText().toString();
        saveButton.setEnabled(APIClient.getClientType(clientId) != null);
    }

    @OnClick(R.id.save_button)
    void saveClientId() {
        String clientId = editText.getText().toString();
        saveId(clientId);
    }

    @OnClick(R.id.random_gen_button)
    void clickRandomGen() {
        String newId = String.format("%04dA", (int) (Math.random() * 10000));
        editText.setText(newId);
        saveId(newId);
    }

    @OnClick({R.id.id_button_1, R.id.id_button_2, R.id.id_button_3, R.id.id_button_4, R.id.id_button_5,
        R.id.id_button_6, R.id.id_button_7, R.id.id_button_8, R.id.id_button_9, R.id.id_button_0, R.id.id_button_a, R.id.id_button_b})
    void onClickIdButton(Button button) {
        String s;
        int id = button.getId();
        List<Integer> numButtons = Lists.newArrayList(
            R.id.id_button_0,
            R.id.id_button_1,
            R.id.id_button_2,
            R.id.id_button_3,
            R.id.id_button_4,
            R.id.id_button_5,
            R.id.id_button_6,
            R.id.id_button_7,
            R.id.id_button_8,
            R.id.id_button_9
        );
        if (numButtons.contains(id)) {
            s = String.valueOf(numButtons.indexOf(id));
        } else if (id == R.id.id_button_a) {
            s = "A";
        } else {
            s = "B";
        }
        editText.setText(editText.getText() + s);
    }

    @OnClick(R.id.clear_button)
    void onClickClearButton() {
        editText.setText("");
    }

    private void saveId(String id) {
        boolean saved = APIClient.setClientId(getContext(), id);
        if (saved) {
            Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
        }
    }
}
