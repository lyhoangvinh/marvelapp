package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD_DEFAULT = 3;

    private int visibleThreshold = VISIBLE_THRESHOLD_DEFAULT;
    private boolean mIsLoadingMore = false;

    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this(layoutManager, VISIBLE_THRESHOLD_DEFAULT);
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager, int visibleThreshold) {
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold;
        this.visibleThreshold = this.visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @SuppressWarnings("ConstantConditions")
    private int getLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;
        if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        }
        return lastVisibleItemPosition;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy <= 0) {
            return;
        }

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItemPosition = getLastVisibleItemPosition();

        if (visibleItemCount == totalItemCount) {
            return;
        }
        if (!mIsLoadingMore && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            mIsLoadingMore = true;
            onLoadMore(totalItemCount);
        }
    }

    public void onLoadMoreComplete() {
        mIsLoadingMore = false;
    }

    public abstract void onLoadMore(int totalItemsCount);
}
