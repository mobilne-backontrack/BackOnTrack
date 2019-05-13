package pl.krakow.uek.view.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import pl.krakow.uek.R;
import pl.krakow.uek.view.calendar.dummy.TaskContent;
import pl.krakow.uek.view.calendar.modify.ModifyTaskActivity;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    public final static String ID = "id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new InvoiceItemRecyclerViewAdapter(TaskContent.ITEMS));
        setHasOptionsMenu(true);

        FloatingActionButton fab = view.findViewById(R.id.add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ModifyTaskActivity.class);
                // EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

            }
        });
        initCalendar(view);

        return view;
    }

    private void initCalendar(View view) {
        calendarView = view.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //TODO: ADD LOAD ITEMS ON CHANGE
            }
        });
    }
}
