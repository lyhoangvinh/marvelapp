package marvelapp.lyhoangvinh.com.marvelapp.base.presenter;


import android.content.Context;
import android.support.annotation.CallSuper;

import marvelapp.lyhoangvinh.com.marvelapp.base.api.ServiceManager;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.LoadMoreable;
import marvelapp.lyhoangvinh.com.marvelapp.base.interfaces.Refreshable;
import marvelapp.lyhoangvinh.com.marvelapp.base.view.BaseView;


/**
 * Base presenter with paging
 */

public abstract class BaseListPresenter<V extends BaseView> extends BasePresenter<V> implements Refreshable, LoadMoreable {

    private boolean isRefreshed = false;

    public void setRefreshed(boolean refreshed) {
        isRefreshed = refreshed;
    }

    public boolean isRefreshed() {
        return isRefreshed;
    }

    public BaseListPresenter(Context context, ServiceManager serviceManager) {
        super(context, serviceManager);
    }

    /**
     * refresh all paging date and re-fetch data
     */
    @CallSuper
    @Override
    public void refresh() {
        isRefreshed = true;
        loadData();
    }

    /**
     * load next page
     */
    @CallSuper
    public void loadMore() {
        if (canLoadMore()) {
            fetchData();
        }
    }

    /**
     * Fetch data from server
     */
    protected abstract void fetchData();

    protected abstract void loadData();
}
