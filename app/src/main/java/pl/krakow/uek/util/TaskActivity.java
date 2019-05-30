package pl.krakow.uek.util;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.krakow.uek.R;

public class TaskActivity extends AppCompatActivity {

    private EditText etTaskName;
    private Button btnAddTask;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mTasksDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");

        etTaskName = (EditText) findViewById(R.id.taskNameET);
        btnAddTask = (Button) findViewById(R.id.addTaskBtn);

        // Send button sends a task and clears the EditText
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task(etTaskName.getText().toString());
                mTasksDatabaseReference.push().setValue(task);

                // Clear input box
                etTaskName.setText("");
            }
        });
    }
}