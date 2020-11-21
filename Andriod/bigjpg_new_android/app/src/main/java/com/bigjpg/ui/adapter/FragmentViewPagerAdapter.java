package com.bigjpg.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;

/** 
 *  描述:FragmentViewPagerAdapter实现类
 *
 * @author mfx
 * @date 2014年4月19日 下午8:09:12
 */
public class FragmentViewPagerAdapter extends BasePagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    
    private final FragmentManager mFragmentManager;
    
    private FragmentTransaction mCurTransaction = null;
    
    public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        mFragmentManager = fm;
        this.mFragmentList = fragmentList;
    }
    
    public void setFragments(ArrayList<Fragment> fragmentList){
        this.mFragmentList = fragmentList;
        notifyDataSetChanged();
    }

    public void replaceItem(int index,Fragment newfragment){
        if(index > getCount()-1 || index < 0){
            return;
        }
        mFragmentList.remove(index);
        mFragmentList.add(index,newfragment);
        notifyDataSetChanged();
    }
    
    public void addItem(Fragment fragment){
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }
    
    public void removeItem(int index){
        if(index > getCount()-1 || index < 0){
            return;
        }
        mFragmentList.remove(index);
        notifyDataSetChanged();
    }
    
    public void removeItem(Fragment fragment){
        mFragmentList.remove(fragment);
        notifyDataSetChanged();
    }
    
    @Override
    public Fragment getItem(int index) {
        return mFragmentList.get(index);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    
    /**
     *  重写该方法解决  notifyDataSetChanged();不更新Viewpager的问题
     *  getItemPosition让它返回POSITOIN_ NONE;这样做的目的是notifyDataSetChanged时返回空，
     *  这样就会从数据重新填充，再外部更新数据然后调用。开销较大，如果简单的更新View的显示，可以设置Tag，通过findViewByTag来更新
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    /**
     * 重写该方法，不做实现，可防止子fragment被回收
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
    
    public void destoryAllItems(){
    	if(mCurTransaction == null){
			mCurTransaction = mFragmentManager.beginTransaction();
		}
    	for(int i=0,size = getCount() ;i<size;i++){
    		String tag = makeFragmentName(i, getItem(i).getClass().getName());
    		Fragment f = mFragmentManager.findFragmentByTag(tag);
    		if(f != null){
    			mCurTransaction.detach(f);
    		}
    	}
    	
    	mCurTransaction.commitAllowingStateLoss();
    	mCurTransaction = null;
    }
    
    @Override
    public String makeFragmentName(int position, String fragmentName) {
    	return  "position : "+position+ " name : " + fragmentName;
    }
}
