package pl.krakow.uek.view.calendar.modify;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.krakow.uek.R;
import pl.krakow.uek.customfont.LatoEditText;
import pl.krakow.uek.customfont.LatoSwitch;
import pl.krakow.uek.view.calendar.CalendarFragment;
import pl.krakow.uek.view.calendar.dummy.TaskContent;

public class ModifyTaskStep extends Fragment implements BlockingStep {

    public static final String[] suggestions = new String[]{"Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Mexico", "Jalapeno"};

    private NachoTextView nachoTextView;
    private LatoEditText taskNameEditText;
    private LatoSwitch notificationSwitch;
    private SingleDateAndTimePicker singleDateAndTimePicker;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTasksDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_task, container, false);
        initNachoTextView(view);
        initFields(view);
        initActivityValues();
        initDatabase();
        return view;
    }

    private void initFields(View view) {
        taskNameEditText = view.findViewById(R.id.task_name);
        notificationSwitch = view.findViewById(R.id.notification);
        singleDateAndTimePicker = view.findViewById(R.id.date);
    }

    private void initNachoTextView(View view) {
        nachoTextView = view.findViewById(R.id.tag);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, suggestions);
        nachoTextView.setAdapter(adapter);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.enableEditChipOnTouch(true, true);
    }

    private void initActivityValues() {
        int id = getActivity().getIntent().getIntExtra(CalendarFragment.ID, 0);
        if (id == 0) {
            return;
        }
        try {
            TaskContent.TaskItem taskItem = TaskContent.ITEM_MAP.get(id);
            nachoTextView.setText(taskItem.getTag());
            taskNameEditText.setText(taskItem.getName());
            notificationSwitch.setChecked(taskItem.getNotification());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = dateFormat.parse(taskItem.getDate());
            singleDateAndTimePicker.setDefaultDate(date);
        } catch (Exception e) {

        }
    }

    private void initDatabase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseReference = mFirebaseDatabase.getReference().child("taskItems");
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
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        }, 2000L);
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                TaskContent.TaskItem taskItem =
                        new TaskContent.TaskItem(1, taskNameEditText.getText().toString(), false, dateFormat.format(singleDateAndTimePicker.getDate()), nachoTextView.getChipValues(), notificationSwitch.isChecked());
                mTasksDatabaseReference.push().setValue(taskItem);

                Toast.makeText(getView().getContext(), "Zadanie zostało dodane!", Toast.LENGTH_SHORT).show();
                callback.complete();
            }
        }, 2000L);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) { }
}
