package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.Item;

/**
 * Created by LyHoangVinh on 07/06/2018.
 */
public class BaseItemViewHolder<T extends Item> extends RecyclerView.ViewHolder {

    private T mItem = null;
    private int i = 0;

    public BaseItemViewHolder(ViewGroup parent, int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    public void setItem(T item) {
        mItem = item;
    }


    public T getItem() {
        return mItem;
    }
}
