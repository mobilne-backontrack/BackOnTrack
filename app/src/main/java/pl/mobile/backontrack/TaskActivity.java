package pl.mobile.backontrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
