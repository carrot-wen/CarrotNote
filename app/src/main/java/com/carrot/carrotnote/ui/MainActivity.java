package com.carrot.carrotnote.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.carrot.carrotnote.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";



    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private RecyclerView.Adapter mAdapter;


    private TabLayoutOnPageChangeCallback mOnPageChangeCallback;
    private TabLayout.OnTabSelectedListener mOnTabSelectedListener;
    private RecyclerView.AdapterDataObserver mPagerAdapterObserver;
    private boolean mAttached;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tabs);
        mAdapter = new FragmentStateAdapter(this) {

            private Fragment[] mFragments = {
                    new FormulaFragment(),
                    ListFragment.getInstance(),
                    new ChartsFragment()
            };

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getItemCount() {
                return mFragments.length;
            }
        };
        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(1);

        attach();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detach();
    }

    private void attach() {

        mOnPageChangeCallback = new TabLayoutOnPageChangeCallback(mTabLayout);
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);
        mPagerAdapterObserver = new PagerAdapterObserver();
        mAdapter.registerAdapterDataObserver(mPagerAdapterObserver);
        mOnTabSelectedListener = new ViewPagerOnTabSelectedListener(mViewPager);
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);
        populateTabsFromPagerAdapter();
        // Now update the scroll position to match the ViewPager's current item
        //mTabLayout.setScrollPosition(mViewPager.getCurrentItem(), 0f, true);

        mAttached = true;
    }

    private void detach() {
        mAdapter.unregisterAdapterDataObserver(mPagerAdapterObserver);
        mTabLayout.removeOnTabSelectedListener(mOnTabSelectedListener);
        mViewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback);
        mPagerAdapterObserver = null;
        mOnTabSelectedListener = null;
        mOnPageChangeCallback = null;
        mAttached = false;
    }


//    private void showFragment(Fragment fragment) {
//
//        FragmentManager fm = getSupportFragmentManager();
//
//        Fragment OldFragment = fm.findFragmentByTag(fragment.getClass().getName());
//        FragmentTransaction ft = fm.beginTransaction();
//        if (OldFragment == null) {
//           ft.add(R.id.main_container,fragment,fragment.getTag());
//        }
//
//
//        if (mShowFragment != null){
//            ft.hide(mShowFragment);
//        }
//        mShowFragment = fragment;
//        ft.show(mShowFragment);
//        ft.commit();
//    }

    private String[] names = new String[]{"Formula","list","charts"};

    void populateTabsFromPagerAdapter() {
        mTabLayout.removeAllTabs();

        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            for (int i = 0; i < adapterCount; i++) {
                TabLayout.Tab tab = mTabLayout.newTab();
                tab.setText(names[i]);
                mTabLayout.addTab(tab, false);
            }

            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                int currItem = mViewPager.getCurrentItem();
                if (currItem != mTabLayout.getSelectedTabPosition()) {
                    mTabLayout.getTabAt(currItem).select();
                }
            }
        }
    }


    private static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        private final ViewPager2 mViewPager;

        ViewPagerOnTabSelectedListener(ViewPager2 viewPager) {
            this.mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }


    private class PagerAdapterObserver extends RecyclerView.AdapterDataObserver {
        PagerAdapterObserver() {}

        @Override
        public void onChanged() {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            populateTabsFromPagerAdapter();
        }
    }

    private static class TabLayoutOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private final WeakReference<TabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        TabLayoutOnPageChangeCallback(TabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
            reset();
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                boolean updateText = mScrollState != SCROLL_STATE_SETTLING
                        || mPreviousScrollState == SCROLL_STATE_DRAGGING;
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                boolean updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                setScrollPosition(tabLayout, position, positionOffset, updateText, updateIndicator);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null
                    && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                boolean updateIndicator = mScrollState == SCROLL_STATE_IDLE
                        || (mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                selectTab(tabLayout, tabLayout.getTabAt(position), updateIndicator);
            }
        }

        void reset() {
            mPreviousScrollState = mScrollState = SCROLL_STATE_IDLE;
        }
    }

    private static Method sSetScrollPosition;
    private static Method sSelectTab;
    private static final String SET_SCROLL_POSITION_NAME = "TabLayout.setScrollPosition(int, float,"
            + " boolean, boolean)";
    private static final String SELECT_TAB_NAME = "TabLayout.selectTab(TabLayout.Tab, boolean)";

    static {
        try {
            sSetScrollPosition = TabLayout.class.getDeclaredMethod("setScrollPosition", int.class,
                    float.class, boolean.class, boolean.class);
            sSetScrollPosition.setAccessible(true);

            sSelectTab = TabLayout.class.getDeclaredMethod("selectTab", TabLayout.Tab.class,
                    boolean.class);
            sSelectTab.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Can't reflect into method TabLayout"
                    + ".setScrollPosition(int, float, boolean, boolean)");
        }
    }

    @SuppressWarnings("WeakerAccess")
    static void setScrollPosition(TabLayout tabLayout, int position, float positionOffset,
                                  boolean updateSelectedText, boolean updateIndicatorPosition) {
        try {
            if (sSetScrollPosition != null) {
                sSetScrollPosition.invoke(tabLayout, position, positionOffset, updateSelectedText,
                        updateIndicatorPosition);
            } else {
                throwMethodNotFound(SET_SCROLL_POSITION_NAME);
            }
        } catch (Exception e) {
            throwInvokeFailed(SET_SCROLL_POSITION_NAME);
        }
    }

    @SuppressWarnings("WeakerAccess")
    static void selectTab(TabLayout tabLayout, TabLayout.Tab tab, boolean updateIndicator) {
        try {
            if (sSelectTab != null) {
                sSelectTab.invoke(tabLayout, tab, updateIndicator);
                tab.select();
            } else {
                throwMethodNotFound(SELECT_TAB_NAME);
            }
        } catch (Exception e) {
            throwInvokeFailed(SELECT_TAB_NAME);
        }
    }

    private static void throwMethodNotFound(String method) {
        throw new IllegalStateException("Method " + method + " not found");
    }

    private static void throwInvokeFailed(String method) {
        throw new IllegalStateException("Couldn't invoke method " + method);
    }

}
