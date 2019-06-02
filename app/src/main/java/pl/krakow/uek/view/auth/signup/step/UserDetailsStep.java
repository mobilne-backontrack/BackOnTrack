package pl.krakow.uek.view.auth.signup.step;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import pl.krakow.uek.R;

public class UserDetailsStep extends Fragment implements BlockingStep {

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        initFirstName(view);
        initLastName(view);
        initPhoneNumber(view);
        initFirebaseAuth();
        return view;
    }

    private void initFirstName(View view) {
        firstName = view.findViewById(R.id.first_name);
    }
    private void initLastName(View view) {
        lastName = view.findViewById(R.id.last_name);
    }
    private void initPhoneNumber(View view) {
        phoneNumber = view.findViewById(R.id.phone_number);
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) { }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        final SharedPreferences userDetails = getContext().getSharedPreferences("userdetails", Context.MODE_PRIVATE);

        if (userDetails.getString("email", null) != null && userDetails.getString("password", null) != null) {
            firebaseAuth.createUserWithEmailAndPassword(userDetails.getString("email", null), userDetails.getString("password", null))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getView().getContext(), "Registration completed!", Toast.LENGTH_SHORT).show();
                                userDetails.edit().clear().apply();
                                firstName.setText("");
                                lastName.setText("");
                                phoneNumber.setText("");
                            } else {
                                Toast.makeText(getView().getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
