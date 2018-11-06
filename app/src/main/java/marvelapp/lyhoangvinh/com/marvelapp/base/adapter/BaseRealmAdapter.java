package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public abstract class BaseRealmAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder> extends RealmRecyclerViewAdapter<T, VH> {

    public BaseRealmAdapter(RealmResults<T> data) {
        super(data, true);
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
            this.onBindViewHolder(holder, this.getItem(position));
        }
    }

    protected abstract void onBindViewHolder(VH vh, @NonNull T dto);

    public T getItem(int adapterPosition) {
        return adapterPosition >= 0 && adapterPosition < getData().size() ? getData().get(adapterPosition) : null;
    }

    public int getItemCount() {
        return getData() == null ? 0 : getData().size();
    }


    public void setDataSetToAdapter(boolean added, List<T> data) {
        if (getData() == null){
            return;
        }
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
        if (getData() == null){
            return;
        }
        getData().clear();
        notifyDataSetChanged();
    }
}
