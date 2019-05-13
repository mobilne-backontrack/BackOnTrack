package pl.krakow.uek.view.auth.signup;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import pl.krakow.uek.view.auth.signup.step.LoginCredentialsStep;
import pl.krakow.uek.view.auth.signup.step.UserDetailsStep;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private final LoginCredentialsStep loginCredentialsStep = new LoginCredentialsStep();
    private final UserDetailsStep userDetailsStep = new UserDetailsStep();

    StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_STEP_POSITION_KEY, position);

        switch (position) {
            case 0:
                loginCredentialsStep.setArguments(bundle);
                return loginCredentialsStep;
            case 1:
                userDetailsStep.setArguments(bundle);
                return userDetailsStep;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context).create();
    }
}
