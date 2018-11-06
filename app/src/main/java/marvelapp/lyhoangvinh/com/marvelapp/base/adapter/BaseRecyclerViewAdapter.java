package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;



import java.util.List;

import lyhoangvinh.com.myutil.adapter.BaseHeaderFooterAdapter;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.ListData;
import marvelapp.lyhoangvinh.com.marvelapp.di.qualifier.ActivityContext;


/**
 * Created by vinh on 9/14/2017.
 */

public abstract class BaseRecyclerViewAdapter<T> extends BaseHeaderFooterAdapter implements ListData {

    protected static final int TYPE_ITEM = 333;

    @Nullable
    private List<T> mAdapterData;

    protected final LayoutInflater mInflater;

    protected final Context context;

    public Context getContext() {
        return context;
    }

    public BaseRecyclerViewAdapter(@ActivityContext @NonNull Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<T> newData) {
        if (newData != null && !newData.equals(mAdapterData)) {
            mAdapterData = newData;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //check what type of view our position is
        if (isHeaderOrFooter(holder.getItemViewType())) {
            super.onBindViewHolder(holder, position);
        } else {
            //it's one of our items, display as required
            T item = getItem(position);
            if (item != null) {
                bindHolder(holder, item);
            }
        }
    }

    @Override
    protected void bindHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    protected abstract void bindHolder(RecyclerView.ViewHolder viewHolder, @NonNull T item);

    /**
     * @param adapterPosition adapter position
     * @return Data at input position
     */
    @Nullable
    protected T getItem(int adapterPosition) {
        if (mAdapterData != null) {
            int realPosition = getRealItemPosition(adapterPosition);
            if (realPosition >= 0 && realPosition < mAdapterData.size()) {
                return mAdapterData.get(realPosition);
            }
        }
        return null;
    }

    protected void removeItem(int adapterPosition) {
        if (mAdapterData != null) {
            int realPosition = getRealItemPosition(adapterPosition);
            if (realPosition >= 0 && realPosition < mAdapterData.size()) {
                mAdapterData.remove(realPosition);
                notifyItemRemoved(adapterPosition);
            }
        }
    }

    protected void removeItem(T item) {
        if (mAdapterData != null) {
            mAdapterData.remove(item);
            notifyDataSetChanged();
        }
    }

    protected void addItem(T item) {
        addItem(item, true);
    }

    protected void addItem(T item, boolean notify) {
        if (mAdapterData != null) {
            mAdapterData.add(item);
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

    @Nullable
    public List<T> getData() {
        return mAdapterData;
    }

    @Override
    public int getItemCountExceptHeaderFooter() {
        return mAdapterData == null ? 0 :mAdapterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        return type == TYPE_NON_FOOTER_HEADER ? TYPE_ITEM : type;
    }

    @Override
    public boolean isDataEmpty() {
        return mAdapterData == null || mAdapterData.size() == 0;
    }

    public void setDataSetToAdapter(boolean added, List<T> data) {
        int positionStart = 0;
        if (added) {
            if (getData().size() > 0) {
                positionStart = getItemCount();
            }
        } else {
            getData().clear();
        }
        getData().addAll(data);
        if (added && positionStart > 0) {
            notifyItemInserted(positionStart);
            notifyItemRangeChanged(positionStart, getItemCount());
        } else {
            notifyDataSetChanged();
        }
    }

    public void addDataSetToAdapter(List<T> data) {
        setDataSetToAdapter(true, data);
    }

    public void setDataSetToAdapter(List<T> data) {
        setDataSetToAdapter(false, data);
    }

    public void resetAdapter() {
        this.getData().clear();
        notifyDataSetChanged();
    }
}
