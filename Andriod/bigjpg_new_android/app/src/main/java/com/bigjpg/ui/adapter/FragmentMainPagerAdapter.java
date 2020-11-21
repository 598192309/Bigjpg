package com.bigjpg.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

/** 
 * 描述:FragmentMainPagerAdapter 不带标题的FragmentPagerAdatper
 *
 * @author mofx 
 * @date 2014年10月28日 下午2:40:18 
 */
public class FragmentMainPagerAdapter extends FragmentViewPagerAdapter{

	public FragmentMainPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
		super(fm, fragmentList);
	}

	
	@Override
	public String makeFragmentName(int position, String fragmentName) {
		//重写返回的Tag，适用于存放不同类型Fragment的ViewPager,为了对应 HomeActivity中的fragmentManager.findFragmentByTag(MineFragment.class.getName());
		return fragmentName;
	}
}
