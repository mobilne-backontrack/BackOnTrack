package pl.krakow.uek.view.todaytasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.krakow.uek.R;
import pl.krakow.uek.view.calendar.TaskItemRecyclerViewAdapter;
import pl.krakow.uek.view.calendar.dummy.TaskContent;

public class TodayTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private Query query;
    private TaskItemRecyclerViewAdapter taskItemRecyclerViewAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_task, container, false);
        initTaskList(view);
        return view;
    }

    private void initTaskList(View view) {
        recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = dateFormat.format(date);
        initDatabase(strDate);
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

        taskItemRecyclerViewAdapter = new TaskItemRecyclerViewAdapter(items);
        recyclerView.setAdapter(taskItemRecyclerViewAdapter);
    }

}
