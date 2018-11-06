package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.v7.widget.RecyclerView;



import java.util.List;

import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.Item;


/**
 * Created by LyHoangVinh on 07/06/2018.
 */
public abstract class BaseItemAdapter extends RecyclerView.Adapter<BaseItemViewHolder> {

    private List<Item> mItemList = null;

    public void notifyAdapter(List<Item> list) {
        this.mItemList = list;
        notifyDataSetChanged();
    }

    public void clearList() {
        if (mItemList != null) {
            this.mItemList.clear();
            notifyDataSetChanged();
        }
    }

    public void addItemLoadMore(List<Item> list) {
        if (mItemList != null && mItemList.size() > 10) {
            mItemList.addAll(list);
            notifyItemInserted(mItemList.size() - 1);
        }
    }

    public void setData(List<Item> list) {
        this.mItemList = list;
    }



    @Override
    public void onBindViewHolder(BaseItemViewHolder holder, int position) {
        holder.setItem(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : 0;
    }

    public Item getItemAt(int position) {
        return mItemList != null ? mItemList.get(position) : null;
    }

    private void setDataSetToAdapter(boolean added, List<Item> data) {
        int positionStart = 0;
        this.mItemList = data;
        if (mItemList != null){
            if (added) {
                if (mItemList.size() > 0) {
                    positionStart = getItemCount();
                }
            } else {
                mItemList.clear();
            }
        }
        if (added && positionStart > 0) {
            notifyItemInserted(positionStart);
            notifyItemRangeChanged(positionStart, getItemCount());
        } else {
            notifyDataSetChanged();
        }
    }

    public void addDataSetToAdapter(List<Item> data) {
        setDataSetToAdapter(true, data);
    }

    public void setDataSetToAdapter(List<Item> data) {
        setDataSetToAdapter(false, data);
    }

    public void editItemAdapter(Item item, int i){
        mItemList.set(i, item);
        notifyItemChanged(i);
    }

    public void deleteItemAdapter(int index) {
        mItemList.remove(index);
        notifyItemRemoved(index);
    }
}
