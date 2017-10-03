package cn.lfz.windynote.view;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.lfz.windynote.R;
import cn.lfz.windynote.model.Category;
import cn.lfz.windynote.model.Note;
import cn.lfz.windynote.tool.DividerItemDecoration;
import cn.lfz.windynote.tool.MyNoteAdapter;
import cn.lfz.windynote.util.LogUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private CoordinatorLayout main;             //主布局
    private Toolbar toolbar;                    //控制条
    private DrawerLayout drawerLayout;          //滑动布局
    private NavigationView navigationView;      //左侧菜单栏内容
    private EmptyRecyclerView recyclerView;           //笔记视图
    private MyNoteAdapter adapter;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateNoteList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNoteList();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        main = (CoordinatorLayout) findViewById(R.id.main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_manage);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        View emptyView = View.inflate(this,R.layout.item_empty,null);
        recyclerView.setEmptyView(emptyView);
        fab.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNoteList();
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(main,"同步成功",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNoteList(){
        if(noteList != null) {
            noteList.clear();
            List newList = DataSupport.findAll(Note.class);
            noteList.addAll(newList);
        }
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //上菜单选项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_upload:
                Snackbar.make(main,"Click Upload",Snackbar.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //左菜单选项
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_add_category:

                //生成布局
                LinearLayout view = new LinearLayout(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
                LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                vlp.setMargins(10,0,10,0);
                final EditText et_category_name = new EditText(this);
                et_category_name.setLayoutParams(vlp);
                view.addView(et_category_name);

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("添加分类")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Category category = new Category();
                                String categoryName = et_category_name.getText().toString();
                                if(!categoryName.equals("")){
                                    category.setName(categoryName);
                                    category.save();
                                    Toast.makeText(MainActivity.this, "保存分类成功！", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(MainActivity.this, "分类名称不能为空！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create();
                alertDialog.show();
                break;
        }
//        drawerLayout.closeDrawers();
        return true;
    }

    //初始化数据
    private void initData() {
        noteList = DataSupport.findAll(Note.class);
        adapter = new MyNoteAdapter(this,noteList);
        recyclerView.setAdapter(adapter);
    }

    //FloatButton的点击事件
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,EditActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
