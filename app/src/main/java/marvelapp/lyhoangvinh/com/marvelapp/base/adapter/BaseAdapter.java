package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @NonNull
    private List<T> data;

    public BaseAdapter(@NonNull List<T> data) {
        this.data = data;
    }

    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(this.getItemLayoutResource(), parent, false);
        return this.createViewHolder(v);
    }

    public abstract int getItemLayoutResource();

    public abstract VH createViewHolder(View var1);

    public void onBindViewHolder(VH holder, int position) {
        T item = this.getItem(position);
        if (item != null) {
            this.onBindViewHolder(holder, this.getItem(position), position);
        }
    }

    protected abstract void onBindViewHolder(VH vh, @NonNull T dto, int position);

    public T getItem(int adapterPosition) {
        return adapterPosition >= 0 && adapterPosition < this.data.size() ? this.data.get(adapterPosition) : null;
    }

    public int getItemCount() {
        return this.data.size();
    }

    @NonNull
    public List<T> getData() {
        return this.data;
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
