package pl.krakow.uek.view.calendar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.krakow.uek.R;
import pl.krakow.uek.customfont.LatoEditText;
import pl.krakow.uek.customfont.LatoSwitch;
import pl.krakow.uek.customfont.LatoTextView;
import pl.krakow.uek.view.calendar.dummy.TaskContent;
import pl.krakow.uek.view.calendar.modify.ModifyTaskActivity;
import pl.krakow.uek.view.calendar.modify.ModifyTaskStep;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    public final static String ID = "id";
    private RecyclerView recyclerView;
    private TaskItemRecyclerViewAdapter invoiceItemRecyclerViewAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private Query query;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = dateFormat.format(date);
        initDatabase(strDate);

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
                String date = getFormattedDate(year, month+1, dayOfMonth);
                initDatabase(date);
            }
        });
    }

    private void initDatabase(String date) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        query = mFirebaseDatabase.getReference().child("taskItems/" + firebaseUser.getUid()).orderByChild("date").startAt(date).endAt(date + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        List<TaskContent.TaskItem> items = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            TaskContent.TaskItem item = ds.getValue(TaskContent.TaskItem.class);
            items.add(item);
        }

        invoiceItemRecyclerViewAdapter = new TaskItemRecyclerViewAdapter(items);
        recyclerView.setAdapter(invoiceItemRecyclerViewAdapter);
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        String mm = month < 10 ? "0" + month : String.valueOf(month);
        String dd = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        String date = year + "/" + mm + "/" + dd;
        return date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.task_filter) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.fragment_filter);
        dialog.setTitle(R.string.filter);
        initNachoTextView(dialog);

        LatoTextView filterButton = dialog.findViewById(R.id.filter_button);
        final LatoEditText taskNameEditText = dialog.findViewById(R.id.task_name);
        final LatoSwitch notificationSwitch = dialog.findViewById(R.id.notification);
        final NachoTextView nachoTextView = dialog.findViewById(R.id.tag);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatabaseByFilter(taskNameEditText.getText().toString(), notificationSwitch.isChecked(), nachoTextView.getChipValues());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initDatabaseByFilter(String name, final boolean notification, final List<String> tag) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        query = mFirebaseDatabase.getReference().child("taskItems/" + firebaseUser.getUid()).orderByChild("name").equalTo(name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showDataByFilter(dataSnapshot, notification, tag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDataByFilter(DataSnapshot dataSnapshot, boolean notification, List<String> tag) {
        List<TaskContent.TaskItem> filteredItems = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            TaskContent.TaskItem item = ds.getValue(TaskContent.TaskItem.class);
            if (item.getNotification() != notification)
                continue;
            if (item.getTag() != null && !item.getTag().equals(tag))
                continue;
            if (item.getTag() == null && tag.size() != 0)
                continue;
            filteredItems.add(item);
        }

        invoiceItemRecyclerViewAdapter = new TaskItemRecyclerViewAdapter(filteredItems);
        recyclerView.setAdapter(invoiceItemRecyclerViewAdapter);
    }

    private void initNachoTextView(Dialog dialog) {
        NachoTextView nachoTextView = dialog.findViewById(R.id.tag);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, ModifyTaskStep.suggestions);
        nachoTextView.setAdapter(adapter);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL);
        nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.enableEditChipOnTouch(true, true);
    }
}
