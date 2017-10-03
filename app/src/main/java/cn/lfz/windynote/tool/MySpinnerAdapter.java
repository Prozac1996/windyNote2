package cn.lfz.windynote.tool;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.lfz.windynote.R;
import cn.lfz.windynote.model.Category;

/**
 * Created by Administrator on 2017/10/3.
 */

public class MySpinnerAdapter extends BaseAdapter{

    private Context mContext;
    private List<Category> categoryList;

    public MySpinnerAdapter(Context mContext, List<Category> categoryList) {
        this.mContext = mContext;
        this.categoryList = new ArrayList<>();

        Category category = new Category();
        category.setId(-1);
        category.setName("默认分类");
        this.categoryList.add(0,category);
        this.categoryList.addAll(categoryList);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categoryList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_category,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = view.findViewById(R.id.item_title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_title.setText(categoryList.get(i).getName());
        return view;
    }

    class ViewHolder{
        TextView tv_title;
    }
}
