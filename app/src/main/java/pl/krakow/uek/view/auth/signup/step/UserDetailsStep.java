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

public class UserDetailsStep extends Fragment implements Step {

    private EditText firstName;
    private EditText lastName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        initFirstName(view);
        initLastName(view);
        return view;
    }

    private void initFirstName(View view) {
        firstName = view.findViewById(R.id.first_name);
    }
    private void initLastName(View view) {
        lastName = view.findViewById(R.id.last_name);
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
