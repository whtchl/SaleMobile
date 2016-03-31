package cn.bingoogolapple.refreshlayout.salemobile.activitypulllist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jinlin.zxing.example.activity.CaptureActivity;

import cm.hardwarereport.example.Constants;
import cn.bingoogolapple.bgabanner.BGAViewPager;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.NormalListViewFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.SettingFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.StaggeredRecyclerViewFragment;
import cn.bingoogolapple.refreshlayout.salemobile.ui.fragment.SwipeRecyclerViewFragment;

public class StoragePullList extends AppCompatActivity {
    private static final String TAG = StoragePullList.class.getSimpleName();
    public static final int LOADING_DURATION = 3000;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private BGAViewPager mViewPager;
    private String brandNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_pull_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mDrawerLayout = getViewById(R.id.drawerLayoutstorge);
        //mNavigationView = getViewById(R.id.navigationView);

        mViewPager = getViewById(R.id.viewPagerStorage);


        //setUpNavDrawer();
        //setUpNavigationView();
        setUpViewPager();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("tag");
        brandNum = bundle.getString("brandNumber");
        Log.d(TAG,"tchl get brandNum:"+brandNum);
        setmBrandNum(brandNum);

    }
    private void setUpViewPager() {
        mViewPager.setAllowUserScrollable(false);
        mViewPager.setAdapter(new ContentViewPagerAdapter(getSupportFragmentManager(), this));
        mViewPager.setCurrentItem(0, false);
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
        private Class[] mFragments = new Class[]{StoragePullListFragment.class};//{GridViewFragment.class, NormalListViewFragment.class, NormalRecyclerViewFragment.class, SwipeListViewFragment.class, SwipeRecyclerViewFragment.class, StaggeredRecyclerViewFragment.class, ScrollViewFragment.class, NormalViewFragment.class, WebViewFragment.class};  ,StaggeredRecyclerViewFragment.class
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

    public String getmBrandNum() {
        return brandNum;
    }

    public void setmBrandNum(String brandNum) {
        this.brandNum = brandNum;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_storagescan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_storagescan) {
            Intent intent = new Intent();
            intent.setClass(this, Model_storagescan.class);

            Bundle bundle = new Bundle();
            bundle.putString("brandNumber", getmBrandNum());
            intent.putExtra("tag", bundle);

            startActivityForResult(intent, Constants.REQUEST_MODEL_SCAN);
            //startActivity(new Intent(StoragePullList.this, Model_storagescan.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_MODEL_SCAN)
        {
           Log.d(TAG,"tchl **********");

        }
    }
}
