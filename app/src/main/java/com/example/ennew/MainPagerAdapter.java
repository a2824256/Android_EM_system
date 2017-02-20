package com.example.ennew;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 挂载三个Fragment界面
 * @author LHH
 *
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    @SuppressWarnings("rawtypes")
	private Class[] mFragment=new Class[] {
    		HistoryFragment.class,
    		LiveFragment.class,
    		AboutFragment.class,
    		
    };
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int position) {
		try {
			return	(Fragment) mFragment[position].newInstance();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return 4;
		return mFragment.length;
	}
}
