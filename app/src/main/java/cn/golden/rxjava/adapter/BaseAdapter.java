package cn.golden.rxjava.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * Created by user on 2/19/16.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    private OnItemClickListener onItemClickListener;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {


        if (onItemClickListener != null) {
            holder.getmConvertView().setClickable(true);
            holder.getmConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        onBindView(holder, holder.getAdapterPosition());

    }

    protected abstract void onBindView(BaseViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return getLayoutID(position);
    }


    protected abstract int getLayoutID(int position);


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
