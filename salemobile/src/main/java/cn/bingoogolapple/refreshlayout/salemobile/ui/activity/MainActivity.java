package cn.bingoogolapple.refreshlayout.salemobile.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import cn.bingoogolapple.bgabanner.BGAViewPager;
import cn.bingoogolapple.refreshlayout.salemobile.App;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.SettingFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.SwipeRecyclerViewFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.NormalListViewFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.StaggeredRecyclerViewFragment;

import cn.bingoogolapple.refreshlayout.salemobile.util.ToastUtil;
import com.jinlin.zxing.example.activity.*;
import com.parse.ParseObject;

import java.util.List;
import java.util.Iterator;
import android.util.Log;
//import
/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/7/10 14:11
 * 描述:
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int LOADING_DURATION = 3000;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private BGAViewPager mViewPager;
    private MenuItem mi=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDrawerLayout = getViewById(R.id.drawerLayout);
        mNavigationView = getViewById(R.id.navigationView);
        mToolbar = getViewById(R.id.toolbar);
        mViewPager = getViewById(R.id.viewPager);

        setSupportActionBar(mToolbar);
        setTitle(R.string.shipment);

        setUpNavDrawer();
        setUpNavigationView();
        setUpViewPager();
        if(!App.getInstance().getIsManager()){
            mNavigationView.getMenu().findItem(R.id.navigation_main_normallistview).setVisible(false);
        }
    }
    private void setUpNavDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void setUpNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());

               // mNavigationView.findViewById(R.id.navigation_main_settings).setVisibility(View.INVISIBLE);
                hideDrawer();
                switch (menuItem.getItemId()) {

/*                  case R.id.navigation_main_normallistview:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.navigation_main_normalrecyclerview:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.navigation_main_swipelistview:
                        mViewPager.setCurrentItem(3, false);
                        break;*/
                    case R.id.navigation_main_swiperecyclerview:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.navigation_main_normallistview:
                        mViewPager.setCurrentItem(1, false);
                        break;
/*                    case R.id.navigation_main_gridview:
                        mViewPager.setCurrentItem(1, false);
                        break;*/
/*                    case R.id.navigation_main_scan:
                        startActivity(new Intent(MainActivity.this, CaptureActivity.class));
                        break;
                    case R.id.navigation_main_staggeredgridlayoutmanager:
                        mViewPager.setCurrentItem(2, false)

                        break;*/
                    case R.id.navigation_main_settings:
                        mViewPager.setCurrentItem(3,false);
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void objectRetrievalFailed(){
        ToastUtil.show("Get info failed! ");
    }

    private void objectsWereRetrievedSuccessfully(List<ParseObject> objects){
        ParseObject fetchObject;
        for(Iterator i = objects.iterator();i.hasNext(); ){
             //  i.next().get
            //fetchObject = i.next();
            //fetchObject.get("Bu");
/*            String str = (String) i.next();

            i.getString("objectId")
            // System.out.println(str);i.
            //Log.i("mainactivity","str:"+str);*/
        }
    }

    private void setUpViewPager() {
        mViewPager.setAllowUserScrollable(false);
        mViewPager.setAdapter(new ContentViewPagerAdapter(getSupportFragmentManager(), this));
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    private static class ContentViewPagerAdapter extends FragmentPagerAdapter {
        private Class[] mFragments = new Class[]{SwipeRecyclerViewFragment.class,NormalListViewFragment.class,StaggeredRecyclerViewFragment.class,SettingFragment.class};//{GridViewFragment.class, NormalListViewFragment.class, NormalRecyclerViewFragment.class, SwipeListViewFragment.class, SwipeRecyclerViewFragment.class, StaggeredRecyclerViewFragment.class, ScrollViewFragment.class, NormalViewFragment.class, WebViewFragment.class};  ,StaggeredRecyclerViewFragment.class
        private Context mContext;

        public ContentViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "tchl  mFragments[position].getName()" + mFragments[position].getName());
            return Fragment.instantiate(mContext, mFragments[position].getName());
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }


/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.navigation_main_normallistview).setVisible(false);
        return true;
    }*/
}