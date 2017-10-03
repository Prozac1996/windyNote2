package cn.lfz.windynote.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import cn.lfz.windynote.constant.MyApplication;

/**
 * Created by Administrator on 2017/10/3.
 */

public class DialogFactory {

    public interface ClickListener{

        void onSure();
        void onCancel();
        void onDismiss();
    }

    public static void showDeleteDialog(Context context, final ClickListener clickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("删除这个记录？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onSure();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onCancel();
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                clickListener.onDismiss();
            }
        });
        dialog.show();
    }
}
