package com.bigjpg.ui.simpleback;


import com.bigjpg.R;

/**
 * @author Momo
 * @date 2019-01-30 12:17
 */
public enum SimpleBackPage {

    ChangePasswordFragment(1, R.string.change_password, com.bigjpg.ui.fragment.ChangePasswordFragment.class),

    ResetPasswordFragment(2, R.string.input_username, com.bigjpg.ui.fragment.ResetPasswordFragment.class),

    UpgradeFragment(3, R.string.upgrade, com.bigjpg.ui.fragment.UpgradeFragment.class),

    PayFragment(4, R.string.upgrade, com.bigjpg.ui.fragment.PayFragment.class),

    MoreSettingFragment(5, R.string.conf, com.bigjpg.ui.fragment.MoreSettingFragment.class);


    private Class<?> clazz;
    private int title;
    private int value;

    SimpleBackPage(int value, int title, Class<?> clazz){
        this.value = value;
        this.title = title;
        this.clazz = clazz;
    }

    public int getTitle(){
        return title;
    }

    public void setTitle(int title){
        this.title = title;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public Class<?> getClazz(){
        return clazz;
    }

    public void setClazz(Class<?> clazz){
        this.clazz = clazz;
    }

    public static SimpleBackPage getPageByValue(int value){
        for (SimpleBackPage page : values()){
            if(page.getValue() == value){
                return page;
            }
        }
        return null;
    }

}
