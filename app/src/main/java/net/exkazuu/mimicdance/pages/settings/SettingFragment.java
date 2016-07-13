package net.exkazuu.mimicdance.pages.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.exkazuu.mimicdance.R;
import net.exkazuu.mimicdance.models.APIClient;

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
        saveButton.setEnabled(enableToSave(clientId));
    }

    @OnClick(R.id.save_button)
    void saveClientId() {
        String clientId = editText.getText().toString();
        if (enableToSave(clientId)) {
            APIClient.setClientId(getContext(), clientId);
            Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean enableToSave(String clientId) {
        return clientId == null || clientId.length() == 0 || APIClient.isValidClientId(clientId);
    }
}
