package com.example.ennew;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ruin.util.Config;

/**
 * 主界面类
 * 
 * @author LHH
 *
 */

public class MainActivity extends FragmentActivity {

	private NavigationBarView mNavBarView;
	private ViewPager mViewPager;
	private long firstTime = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {// 如果安卓系统是4.4以上，显示该系统沉浸式界面
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {// 如果安卓系统是5.0以上，显示该系统沉浸式界面
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			window.setNavigationBarColor(Color.TRANSPARENT);
		}
		if (!Config.pdir.exists()) { // 如果数据库目录不存在
			Config.pdir.mkdir(); // 创建数据库目录
		}

		mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
		mNavBarView = (NavigationBarView) findViewById(R.id.main_navigation_bar);

		mNavBarView.setViewPager(mViewPager);

		MainPagerAdapter adapter = new MainPagerAdapter(
				getSupportFragmentManager());
		mViewPager.setAdapter(adapter);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(MainActivity.this, "再按一次退出程序...",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime,记录上一次按返回时间
				return true;
			} else {
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
