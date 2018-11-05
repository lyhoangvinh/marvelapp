package marvelapp.lyhoangvinh.com.marvelapp.base.interfaces;

/**
 * Indicate refreshable ui objects, eg. activity, fragment...
 */

public interface UiRefreshable extends Refreshable{
    void doneRefresh();
    void refreshWithUi();
    void refreshWithUi(int delay);
    void setRefreshEnabled(boolean enabled);
}
