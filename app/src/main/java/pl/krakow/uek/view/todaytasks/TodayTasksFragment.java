package pl.krakow.uek.view.todaytasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.krakow.uek.R;
import pl.krakow.uek.view.calendar.InvoiceItemRecyclerViewAdapter;
import pl.krakow.uek.view.calendar.dummy.TaskContent;

public class TodayTasksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_task, container, false);
        initTaskList(view);
        return view;
    }

    private void initTaskList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new InvoiceItemRecyclerViewAdapter(TaskContent.ITEMS));
    }

}
