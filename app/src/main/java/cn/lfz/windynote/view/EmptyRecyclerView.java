package cn.lfz.windynote.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/10/3.
 */

public class EmptyRecyclerView extends RecyclerView {

    View emptyView;

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter adapter = getAdapter();
            if(adapter.getItemCount() == 0){
                emptyView.setVisibility(VISIBLE);
                EmptyRecyclerView.this.setVisibility(GONE);

            }else{
                emptyView.setVisibility(GONE);
                EmptyRecyclerView.this.setVisibility(VISIBLE);

            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onChanged();
        }
    };



    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        ((ViewGroup) this.getParent()).addView(emptyView);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(observer);
        observer.onChanged();
    }
}
