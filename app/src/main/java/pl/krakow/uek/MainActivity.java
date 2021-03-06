package pl.krakow.uek;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.krakow.uek.util.PhotoPlusOcrUtil;
import pl.krakow.uek.view.auth.signin.SignInActivity;
import pl.krakow.uek.view.calendar.CalendarFragment;
import pl.krakow.uek.view.calendar.dummy.TaskContent;
import pl.krakow.uek.view.menu.DrawerAdapter;
import pl.krakow.uek.view.menu.DrawerItem;
import pl.krakow.uek.view.menu.DrawerMenuItem;
import pl.krakow.uek.view.todaytasks.TodayTasksFragment;

public class MainActivity extends AppCompatActivity
        implements DrawerAdapter.OnItemSelectedListener {

    private DrawerMenuItem[] menuItems = {
            DrawerMenuItem.TODAY_TASKS,
            DrawerMenuItem.CALENDAR,
            DrawerMenuItem.SCANNER,
            DrawerMenuItem.EMPTY,
            DrawerMenuItem.EMPTY,
            DrawerMenuItem.EMPTY,
            DrawerMenuItem.SING_OUT
    };

    private SlidingRootNav slidingRootNav;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    // ocr
    String datapath = "";
    private Uri fileUri;
    Bitmap image;
    private TessBaseAPI mTess;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEngLanguage();
        setContentView(R.layout.activity_main);
        toolbarTitle = findViewById(R.id.toolbar_title);
        initToolbar();
        initDrawer(savedInstanceState);
        initFirebaseAuth();
        // OCR
        datapath = getFilesDir()+ "/tesseract/";
        PhotoPlusOcrUtil.checkFile(getAssets(), new File(datapath + "tessdata/"), datapath);

        int code = getPackageManager().checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getPackageName());
        if (code == PackageManager.PERMISSION_GRANTED) {
            Log.i("OCR", "permission granted");
        } else {
            Log.i("OCR", "not granted!");
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initDrawer(Bundle savedInstanceState) {
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();


        List<DrawerItem> drawerItem = new ArrayList<>();
        for (DrawerMenuItem item : menuItems) {
            drawerItem.add(item.createItem(this));
        }
        DrawerAdapter adapter = new DrawerAdapter(drawerItem);
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        list.setNestedScrollingEnabled(false);

        adapter.setSelected(0);
    }

    private void initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onItemSelected(int position) {
        if (menuItems[position] == DrawerMenuItem.SING_OUT) {
            logOut();
        } else if (menuItems[position] == DrawerMenuItem.TODAY_TASKS) {
            Fragment todayTasksFragment = new TodayTasksFragment();
            showFragment(todayTasksFragment);
        } else if (menuItems[position] == DrawerMenuItem.CALENDAR) {
            Fragment calendar = new CalendarFragment();
            showFragment(calendar);
        } else if (menuItems[position] == DrawerMenuItem.SCANNER) {
            // ocr
            requestAppPermissions();
            File newFile = PhotoPlusOcrUtil.getOutputMediaFile(this);
            fileUri = Uri.fromFile(newFile);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(intent, 100);
        }


        toolbarTitle.setText(menuItems[position].getTitle(this));
        slidingRootNav.closeMenu();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void logOut() {
        initGoogleSignIn();
        final Intent intent = new Intent(this, SignInActivity.class);

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuth.signOut();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                // text recognition:
                mTess = new TessBaseAPI();
                mTess.init(datapath, "eng");
                image = BitmapFactory.decodeFile(fileUri.getPath());
                mTess.setImage(image);
                String text = mTess.getUTF8Text();

                Log.i("OCR", text);

                // saving text to database as new task item
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mTasksDatabaseReference = database.getReference().child("taskItems/" + firebaseUser.getUid());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String id = mTasksDatabaseReference.push().getKey();
                TaskContent.TaskItem taskItem =
                        new TaskContent.TaskItem(id, text, false, dateFormat.format(new Date()), new ArrayList<String>(), false);
                mTasksDatabaseReference.child(id).setValue(taskItem);
                Log.i("OCR", "ocr task has been added");

            }

        }

    }

    // ------------------------------- check write / read access

    private void requestAppPermissions() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //?
        StrictMode.setVmPolicy(builder.build()); //?

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 100); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void initEngLanguage() {
        String languageToLoad  = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
