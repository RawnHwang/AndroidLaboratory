package me.hwang.materialdesigntest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import me.hwang.materialdesigntest.fragment.FirstContentFragment;
import me.hwang.materialdesigntest.fragment.SecondContentFragment;
import me.hwang.materialdesigntest.fragment.ThirdContentFragment;
import me.hwang.materialdesigntest.util.ScreenUtil;

public class MainActivity extends AppCompatActivity {

    // Toolbar part
    private Toolbar mToolbar;
    private SearchView mSearchView;
    // DrawerLayout part
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    // Child fragment part
    private final String FRAGMENT_TAG_PREFIX = "content_fragment_";
    private final int INIT_FRAGMENT_INDEX = 0;
    private int currentFragmentIndex = INIT_FRAGMENT_INDEX;
    private HashMap<Integer, Class> fragmentDictionary;
    // FloatingActionButton part
    private FloatingActionButton btnTestSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试Toolbar
        testToolbar();
        // 初始化Fragment
        initFragmentDic();
        // 测试DrawerLayout
        testDrawerLayout();
        // 测试FloatingActionButton及Snackbar
        testFABAndSnackbar();
    }

    private void testToolbar() {
        mToolbar = findViewById(R.id.my_toolbar);

        // logo
        // mToolbar.setLogo(R.drawable.ic_md);

        // 主标题
        mToolbar.setTitle("材料设计");

        // 副标题
        mToolbar.setSubtitle("Material Design");

        // 标题左外边距
        // int margin = ScreenUtil.dip2px(this, 35);
        // mToolbar.setTitleMarginStart(margin);

        // 将ActionBar替换为ToolBar
        setSupportActionBar(mToolbar);

        // 显示Home导航键 (但是配合DrawerLayout使用时，如何使用了Toggle，则无需以下配置)
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true); // home键是否可以点击
        // getSupportActionBar().setDisplayShowHomeEnabled(true); // 是否允许home键显示
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        initSearchView(menu);
        return true;
    }

    private void initSearchView(Menu menu) {
        mSearchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "search clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String hint = null;

        switch (item.getItemId()) {
            case R.id.item_backup:
                hint = "you clicked item backup";
                break;
            case R.id.item_delete:
                hint = "you clicked item delete";
                break;
            case R.id.item_setting:
                hint = "you clicked item setting";
                break;
        }

        if (hint != null)
            Toast.makeText(this, hint, Toast.LENGTH_SHORT).show();

        return true;
    }

    private void initFragmentDic() {
        fragmentDictionary = new HashMap<>();
        fragmentDictionary.put(0, FirstContentFragment.class);
        fragmentDictionary.put(1, SecondContentFragment.class);
        fragmentDictionary.put(2, ThirdContentFragment.class);

        checkFragment(currentFragmentIndex);
    }

    private void checkFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment = fm.findFragmentByTag(FRAGMENT_TAG_PREFIX + index);
        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentDictionary.get(index).newInstance();
                ft.add(R.id.fl_content_container, fragment, FRAGMENT_TAG_PREFIX + index);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Fragment needHide;
        if ((needHide = fm.findFragmentByTag(FRAGMENT_TAG_PREFIX + currentFragmentIndex)) != null)
            ft.hide(needHide);

        ft.show(fragment)
                .commit();

        currentFragmentIndex = index;
    }

    private void testDrawerLayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setDrawerToggle();
        setNavigationView();
    }

    private void setDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_toggle_opened, R.string.drawer_toggle_closed);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void setNavigationView() {
        mNavigationView = findViewById(R.id.nav_main);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = -1;
                switch (item.getItemId()) {
                    case R.id.nav_goto_first:
                        index = 0;
                        break;
                    case R.id.nav_goto_second:
                        index = 1;
                        break;
                    case R.id.nav_goto_third:
                        index = 2;
                        break;

                }

                if (index != -1)
                    checkFragment(index);

                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    private void testFABAndSnackbar() {
        btnTestSnackbar = findViewById(R.id.test_snack_bar);
        btnTestSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"This is the Snackbar!",Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"you clicked ok!",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }
}
