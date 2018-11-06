package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class EndlessAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int VIEW_TYPE_ITEM   = 0;
    protected static final int VIEW_TYPE_FOOTER = 1;

    private boolean mWithFooter = false;

    protected final Context mContext;
    protected List<T> arrayList = null;

    public EndlessAdapter(Context context) {
        this.mContext = context;
        this.arrayList = new ArrayList<>();
    }

    protected abstract RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent);
    protected abstract RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            return getItemView(inflater, parent);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return getFooterView(inflater, parent);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemCount() {
        if (arrayList == null)
            return 0;
        int itemCount = arrayList.size();
        if (mWithFooter)
            itemCount++;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position))
            return VIEW_TYPE_FOOTER;
        return VIEW_TYPE_ITEM;
    }

    public List<T> getItems() {
        return arrayList;
    }

    protected T getItem(int position) {
        if (arrayList == null)
            return null;
        return arrayList.get(position);
    }

    public void removeItem(final int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setEnableFooter(boolean enable) {
        if (mWithFooter != enable) {
            mWithFooter = enable;
            notifyDataSetChanged();
        }
    }

    public boolean isPositionFooter(int position) {
        return (mWithFooter && position == (getItemCount() - 1));
    }

    public void setDataSetToAdapter(List<T> list) {
        this.mWithFooter = false;
        setDataSetToAdapter(list, false);
    }

    public void addDataSetToAdapter(List<T> list) {
        this.mWithFooter = false;
        setDataSetToAdapter(list, true);
    }

    protected void setDataSetToAdapter(List<T> list, boolean added) {
        if (list == null) {
            return;
        }
        int positionStart = 0;
        if (added) {
            if (list.size() > 0) {
                positionStart = getItemCount();
            }
        } else {
            arrayList.clear();
        }
        arrayList.addAll(list);
        if (added && positionStart > 0) {
            notifyItemInserted(positionStart);
            notifyItemRangeChanged(positionStart, getItemCount());
        } else {
            notifyDataSetChanged();
        }
    }

    public void resetAdapter() {
        this.mWithFooter = false;
        this.arrayList.clear();
        notifyDataSetChanged();
    }

    protected void setWithFooter(boolean value) {
        this.mWithFooter = value;
    }

    protected boolean isWithFooter() {
        return this.mWithFooter;
    }
}
