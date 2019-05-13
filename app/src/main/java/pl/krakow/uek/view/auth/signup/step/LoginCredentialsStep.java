package pl.krakow.uek.view.auth.signup.step;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import pl.krakow.uek.R;

public class LoginCredentialsStep extends Fragment implements Step {

    private EditText username;
    private EditText password;
    private EditText confirmationPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_credentials, container, false);
        initUsername(view);
        initPassword(view);
        initPasswordConfirmation(view);
        return view;
    }


    private void initUsername(View view) {
        username = view.findViewById(R.id.username);
    }

    private void initPassword(View view) {
        password = view.findViewById(R.id.password);
    }

    private void initPasswordConfirmation(View view) {
        confirmationPassword = view.findViewById(R.id.password_confirmation);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error) {}
}
