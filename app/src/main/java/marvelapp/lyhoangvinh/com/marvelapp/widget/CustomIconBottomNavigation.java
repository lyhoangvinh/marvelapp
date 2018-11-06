package marvelapp.lyhoangvinh.com.marvelapp.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


import java.util.ArrayList;

import marvelapp.lyhoangvinh.com.marvelapp.R;
import marvelapp.lyhoangvinh.com.marvelapp.base.adapter.BasePagerAdapterWithIcon;
import marvelapp.lyhoangvinh.com.marvelapp.widget.ahbottomnavigation.AHBottomNavigation;
import marvelapp.lyhoangvinh.com.marvelapp.widget.ahbottomnavigation.AHBottomNavigationItem;

public class CustomIconBottomNavigation extends AHBottomNavigation {

    public CustomIconBottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CustomIconBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomIconBottomNavigation(Context context) {
        super(context);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        if (viewPager != null) {
            PagerAdapter adapter = viewPager.getAdapter();
            ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
            if (adapter instanceof BasePagerAdapterWithIcon) {
                int n = adapter.getCount();
                for (int i = 0; i < n; i++) {
                    AHBottomNavigationItem item = new AHBottomNavigationItem("",((BasePagerAdapterWithIcon) adapter).getPageIcon(i), R.color.colorPrimaryDark);
                    bottomNavigationItems.add(item);
                }
                addItems(bottomNavigationItems);
            }

            setOnTabSelectedListener((position, wasSelected) -> {
                viewPager.setCurrentItem(position);
                return true;
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    setCurrentItem(i);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }
}
