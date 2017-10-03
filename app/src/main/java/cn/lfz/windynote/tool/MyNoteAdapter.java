package cn.lfz.windynote.tool;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.lfz.windynote.R;
import cn.lfz.windynote.model.Note;
import cn.lfz.windynote.util.DialogFactory;
import cn.lfz.windynote.view.EditActivity;
import cn.lfz.windynote.view.MainActivity;

/**
 * Created by Administrator on 2017/9/25.
 * 笔记列表适配器
 */

public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.ViewHolder>{

    private Context mContext;
    private List<Note> notes;

    public MyNoteAdapter(Context mContext, List<Note> notes) {
        this.mContext = mContext;
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Note note = notes.get(position);
        //点击跳出详情页面
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = notes.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("id",note.getId());
                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((MainActivity)mContext).toBundle());
            }
        });
        //长按提示是否删除
        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogFactory.showDeleteDialog(mContext,new DialogFactory.ClickListener() {
                    @Override
                    public void onSure() {
                        notes.get(position).delete();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onDismiss() {
                        ((MainActivity)mContext).updateNoteList();
                    }
                });
                return true;
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        holder.note_title.setText(note.getTitle());
        holder.note_time.setText(sdf.format(note.getEditTime()));
        holder.note_content.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout ll;
        TextView note_title;
        TextView note_time;
        TextView note_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView;
            note_title = itemView.findViewById(R.id.item_title);
            note_time = itemView.findViewById(R.id.item_time);
            note_content = itemView.findViewById(R.id.item_content);
        }

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            ll = (LinearLayout) itemView;
        }
    }

}
