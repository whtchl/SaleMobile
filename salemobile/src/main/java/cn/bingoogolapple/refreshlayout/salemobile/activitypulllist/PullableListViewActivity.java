package cn.bingoogolapple.refreshlayout.salemobile.activitypulllist;

/**
 * Created by tchl on 2016/1/28.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.bingoogolapple.refreshlayout.salemobile.R;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
public class PullableListViewActivity extends ActionBarActivity
{
	private ListView listView;
	private PullToRefreshLayout ptrl;
	private boolean isFirstIn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new MyListener());
		listView = (ListView) findViewById(R.id.content_view);
		initListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate our menu from the resources by using the menu inflater.
	//	getMenuInflater().inflate(R.menu.activitypulllist, menu);

		// It is also possible add items here. Use a generated id from
		// resources (ids.xml) to ensure that all menu ids are distinct.
/*
		MenuItem locationItem = menu.add(0, R.id.menu_location, 0, R.string.menu_location);
		locationItem.setIcon(R.drawable.ic_action_location);

		// Need to use MenuItemCompat methods to call any action item related methods
		MenuItemCompat.setShowAsAction(locationItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
*/

		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		// 第一次进入自动刷新
		if (isFirstIn)
		{
			ptrl.autoRefresh();
			isFirstIn = false;
		}
	}

	/**
	 * ListView初始化方法
	 */
	private void initListView()
	{
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 30; i++)
		{
			items.add("这里是item " + i);
		}
		MyAdapter adapter = new MyAdapter(this, items);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(
						PullableListViewActivity.this,
						"LongClick on "
								+ parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(PullableListViewActivity.this,
						" Click on " + parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
