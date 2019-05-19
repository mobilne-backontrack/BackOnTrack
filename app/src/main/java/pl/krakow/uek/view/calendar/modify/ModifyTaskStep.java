package pl.krakow.uek.view.calendar.modify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import pl.krakow.uek.R;
import pl.krakow.uek.customfont.LatoEditText;
import pl.krakow.uek.customfont.LatoSwitch;
import pl.krakow.uek.view.calendar.CalendarFragment;
import pl.krakow.uek.view.calendar.dummy.TaskContent;

public class ModifyTaskStep extends Fragment implements Step {

    public static final String[] suggestions = new String[]{"Tortilla Chips", "Melted Cheese", "Salsa", "Guacamole", "Mexico", "Jalapeno"};

    private NachoTextView nachoTextView;
    private LatoEditText taskNameEditText;
    private LatoSwitch notificationSwitch;
    private SingleDateAndTimePicker singleDateAndTimePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_task, container, false);
        initNachoTextView(view);
        initFields(view);
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
        int id = getActivity().getIntent().getIntExtra(CalendarFragment.ID, 0);
        if (id == 0) {
            return;
        }
        TaskContent.TaskItem taskItem = TaskContent.ITEM_MAP.get(id);
        nachoTextView.setText(taskItem.tag);
        taskNameEditText.setText(taskItem.name);
        notificationSwitch.setChecked(taskItem.notification);
        singleDateAndTimePicker.setDefaultDate(taskItem.date);
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
