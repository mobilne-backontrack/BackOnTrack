package pl.krakow.uek;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.List;

import pl.krakow.uek.view.auth.signin.SignInActivity;
import pl.krakow.uek.view.calendar.CalendarFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarTitle = findViewById(R.id.toolbar_title);
        initToolbar();
        initDrawer(savedInstanceState);
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

    @Override
    public void onItemSelected(int position) {
        if (menuItems[position] == DrawerMenuItem.SING_OUT) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (menuItems[position] == DrawerMenuItem.TODAY_TASKS) {
            Fragment todayTasksFragment = new TodayTasksFragment();
            showFragment(todayTasksFragment);
        } else if (menuItems[position] == DrawerMenuItem.CALENDAR) {
            Fragment calendar = new CalendarFragment();
            showFragment(calendar);
        } else if (menuItems[position] == DrawerMenuItem.SCANNER) {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                PackageManager pm = getBaseContext().getPackageManager();
                final ResolveInfo mInfo = pm.resolveActivity(i, 0);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(intent);
            } catch (Exception e){}
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
}
