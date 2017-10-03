package cn.lfz.windynote.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.lfz.windynote.R;
import cn.lfz.windynote.model.Category;
import cn.lfz.windynote.model.Note;
import cn.lfz.windynote.tool.MySpinnerAdapter;

/**
 * Created by Administrator on 2017/9/25.
 * 编辑笔记的界面
 */

public class EditActivity extends AppCompatActivity {

    private EditText et_title;  //题目
    private EditText et_content;    //内容
    private TextView tv_time;       //时间
    private Spinner spr_category;   //分类
    private Toolbar edit_toolbar;   //菜单栏

    private MySpinnerAdapter adapter;
    private boolean isModify = false;
    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initData() {

        //初始化分类
        List categoryList = DataSupport.findAll(Category.class);
        adapter = new MySpinnerAdapter(this,categoryList);
        spr_category.setAdapter(adapter);
        //初始化笔记数据
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Intent intent = getIntent();
        long id = intent.getLongExtra("id",0);
        if(id != 0){         //修改模式
            isModify = true;
            note = DataSupport.find(Note.class,id,true);
            et_title.setText(note.getTitle());
            et_content.setText(note.getContent());
            tv_time.setText(sdf.format(note.getEditTime()));
            Category category = note.getCategory();
            setSpinnerValue(category);
        }else{                      //添加模式
            Date date = new Date();
            String dateString = sdf.format(date);
            tv_time.setText(dateString);
        }
    }

    public void setSpinnerValue(Category category){
        if(category == null)
            return;
        for (int i = 0; i < adapter.getCount(); i++) {
            Category temp = (Category) adapter.getItem(i);
            if(temp.getId() == category.getId()){
                spr_category.setSelection(i);
                break;
            }

        }
    }

    private void initView() {
        setContentView(R.layout.activity_edit);
        getWindow().setEnterTransition(new Slide().setDuration(500));
        edit_toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        et_content = (EditText) findViewById(R.id.edit_content);
        et_title = (EditText) findViewById(R.id.edit_title);
        tv_time = (TextView) findViewById(R.id.edit_time);
        spr_category = (Spinner) findViewById(R.id.edit_spinner_category);
        setSupportActionBar(edit_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu_save:
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                Date date = new Date();
                if(note == null)
                    note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setEditTime(date);
                if(spr_category.getSelectedItemPosition() != 0)
                    note.setCategory((Category) adapter.getItem(spr_category.getSelectedItemPosition()));
                else
                    note.setCategory(null);
                if(note.save()){
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().setExitTransition(new Slide().setDuration(500));
    }
}
