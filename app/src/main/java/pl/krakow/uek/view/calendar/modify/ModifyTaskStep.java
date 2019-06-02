package pl.krakow.uek.view.calendar.modify;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.krakow.uek.MainActivity;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Query query;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_task, container, false);
        initNachoTextView(view);
        initFields(view);
        initDatabase();
        initActivityValues();
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
        id = getActivity().getIntent().getStringExtra(CalendarFragment.ID);
        if (id != null) {
            query = mFirebaseDatabase.getReference().child("taskItems/" + firebaseUser.getUid()).orderByChild("id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        TaskContent.TaskItem item = ds.getValue(TaskContent.TaskItem.class);
                        try {
                            nachoTextView.setText(item.getTag());
                            taskNameEditText.setText(item.getName());
                            notificationSwitch.setChecked(item.getNotification());
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = dateFormat.parse(item.getDate());
                            singleDateAndTimePicker.setDefaultDate(date);
                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initDatabase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseReference = mFirebaseDatabase.getReference().child("taskItems/" + firebaseUser.getUid());
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
                if (id != null) {
                    TaskContent.TaskItem taskItem =
                            new TaskContent.TaskItem(id, taskNameEditText.getText().toString(), false, dateFormat.format(singleDateAndTimePicker.getDate()), nachoTextView.getChipValues(), notificationSwitch.isChecked());
                    mTasksDatabaseReference.child(id).setValue(taskItem);
                    Toast.makeText(getView().getContext(), "Task updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    id = mTasksDatabaseReference.push().getKey();
                    TaskContent.TaskItem taskItem =
                            new TaskContent.TaskItem(id, taskNameEditText.getText().toString(), false, dateFormat.format(singleDateAndTimePicker.getDate()), nachoTextView.getChipValues(), notificationSwitch.isChecked());
                    mTasksDatabaseReference.child(id).setValue(taskItem);
                    Toast.makeText(getView().getContext(), "Task added successfully!", Toast.LENGTH_SHORT).show();
                }

                callback.complete();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 2000L);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) { }
}
