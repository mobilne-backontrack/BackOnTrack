package pl.krakow.uek.view.calendar;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hootsuite.nachos.NachoTextView;

import java.text.DateFormat;
import java.util.List;

import pl.krakow.uek.R;
import pl.krakow.uek.view.calendar.dummy.TaskContent;
import pl.krakow.uek.view.calendar.modify.ModifyTaskActivity;

public class InvoiceItemRecyclerViewAdapter extends RecyclerView.Adapter<InvoiceItemRecyclerViewAdapter.ViewHolder> {

    private DateFormat dateFormat = DateFormat.getTimeInstance();
    private final List<TaskContent.TaskItem> values;

    public InvoiceItemRecyclerViewAdapter(List<TaskContent.TaskItem> items) {
        values = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = values.get(position);
        holder.nameTextView.setText(values.get(position).name);
        holder.tagTextView.setText(values.get(position).tag);
        holder.finishedCheckBox.setChecked(values.get(position).finished);
        holder.notificationImageView.setVisibility(values.get(position).notification ? View.VISIBLE : View.INVISIBLE);
        holder.dateTextView.setText(dateFormat.format(values.get(position).date));
        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ModifyTaskActivity.class);
                    intent.putExtra(CalendarFragment.ID, holder.mItem.id);
                    view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView nameTextView;
        private final NachoTextView tagTextView;
        private final CheckBox finishedCheckBox;
        private final TextView dateTextView;
        private final ImageView notificationImageView;

        private TaskContent.TaskItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = view.findViewById(R.id.name);
            tagTextView = view.findViewById(R.id.tag);
            finishedCheckBox = view.findViewById(R.id.finished);
            notificationImageView = view.findViewById(R.id.notification);
            dateTextView = view.findViewById(R.id.date);
        }
    }
}
