package marvelapp.lyhoangvinh.com.marvelapp.base.adapter;

import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by LyHoangVinh on 17/06/2018.
 */
public abstract class BasePagerAdapterWithIcon extends FragmentStatePagerAdapter {

    public BasePagerAdapterWithIcon(FragmentManager fm) {
        super(fm);
    }

    @DrawableRes
    public abstract int getPageIcon(int drawable);
}
