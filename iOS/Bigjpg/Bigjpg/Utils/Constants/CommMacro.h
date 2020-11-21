//
//  CommMacro.h
//  FullShareTop
//
//  Created by lqq on 17/3/21.
//  Copyright © 2017年 FSB. All rights reserved.
//

#ifndef CommMacro_h
#define CommMacro_h



/**
 *  非DEBUG模式，禁用NSLog();
 */
#if DEBUG
#define NSLog(FORMAT, ...) fprintf(stderr,"\nfunction:%s line:%d content:%s\n", __FUNCTION__, __LINE__, [[NSString stringWithFormat:FORMAT, ##__VA_ARGS__] UTF8String]);
#else
#define NSLog(...) {}
#endif



/**
 *  发送通知
 */
#define POST_NOTIFY(_name_,_content_) ([[NSNotificationCenter defaultCenter] postNotificationName:_name_ object:_content_])

/**
 *  系统版本
 */
#define SYSTEM_VERSION  [[[UIDevice currentDevice] systemVersion] floatValue]


/**
 *  屏幕尺寸
 */
#define kDeviceWidth        [UIScreen mainScreen].bounds.size.width

#define kDeviceHeight        [UIScreen mainScreen].bounds.size.height

#define kNavigationH      64

/**
 *  真1像素
 */
#define kOnePX            1/[[UIScreen mainScreen] scale]


// 颜色
#define RGB(r,g,b)      [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:1.0f]


// 生成颜色支持透明度
#define RGBA(r,g,b,a)   [UIColor colorWithRed:r/255.0f green:g/255.0f blue:b/255.0f alpha:a]



// 加载图片
#define IMG_NAME(name)   [UIImage imageNamed:name]

//弱引用
#define WeakSelf(type)  __weak typeof(type) weak##type = type;

//强引用
#define StrongSelf(type)  __strong typeof(type) type = weak##type;




// 是否iPad
#define isPad                   (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
/*************** 尺寸 *******************/
#define LQScreemW [UIScreen mainScreen].bounds.size.width
#define LQScreemH [UIScreen mainScreen].bounds.size.height
#define IS_IPHONE_4 (fabs((double)[[UIScreen mainScreen] bounds].size.height - (double )480) < DBL_EPSILON )
#define IS_IPHONE_5 (fabs((double)[[UIScreen mainScreen] bounds].size.height - (double )568) < DBL_EPSILON )
#define IS_IPHONE_6 (fabs((double)[[UIScreen mainScreen] bounds].size.height - (double )667) < DBL_EPSILON )
#define IS_IPHONE_6_PLUS (fabs((double)[[UIScreen mainScreen] bounds].size.height - (double )736) < DBL_EPSILON )
#define IS_IPHONEX ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1125,2436), [[UIScreen mainScreen] currentMode].size) : NO)
//判断iPhoneXr
#define IS_IPHONE_Xr ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(828, 1792), [[UIScreen mainScreen] currentMode].size) && !isPad : NO)
//判断iPhoneXsMax
#define IS_IPHONE_Xs_Max ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1242, 2688), [[UIScreen mainScreen] currentMode].size)&& !isPad : NO)

//判断iPhoneX所有系列
#define IS_PhoneXAll (IS_IPHONEX || IS_IPHONE_Xr || IS_IPHONE_Xs_Max)
//判断是iPhone
#define IS_Phone UI_USER_INTERFACE_IDIOM()==UIUserInterfaceIdiomPhone
#define TabbarH  (IS_PhoneXAll ? 83 : 49)
#define NavMaxY  (IS_PhoneXAll ? 88 : 64)
#define SafeAreaTopHeight  (IS_PhoneXAll ? 24 : 0) //导航栏 粪叉显示栏多了24
#define SafeAreaBottomHeight  (IS_PhoneXAll ? 34 : 0) //导航栏 粪叉显示栏多了24
#define TitleViewH Adaptor_Value(45)


//不同屏幕尺寸字体适配（375，667是因为效果图为IPHONE6 如果不是则根据实际情况修改）
#define kScreenWidthRatio  1
#define kScreenHeightRatio 1
#define AdaptedWidth(x)  ceilf((x) * kScreenWidthRatio)
#define Adaptor_Value(v)        v
#define BottomAdaptor_Value(v)        (v + SafeAreaBottomHeight)*kScreenWidthRatio
#define TopAdaptor_Value(v)       (v + SafeAreaTopHeight)*kScreenWidthRatio
//字体
//中文字体
#define Regular_FONT_NAME  @"PingFangSC-Regular"
#define Bold_FONT_NAME  @"PingFangSC-Semibold"

#define CHINESE_SYSTEMRegular(x) [UIFont fontWithName:Regular_FONT_NAME size:x]
#define CHINESE_SYSTEMBold(x) [UIFont fontWithName:Bold_FONT_NAME size:x]

#define AdaptedFontSize(R)    CHINESE_SYSTEMRegular(AdaptedWidth(R))
#define AdaptedBoldFontSize(R) CHINESE_SYSTEMBold(AdaptedWidth(R))

//颜色
#define TitleBlackColor   (RI.isNight ? RGB(175,175,175) : RGB(50,50,50))
#define BackGroundColor   ( RI.isNight ? RGB(20,20,20) : [UIColor lq_colorWithHexString:@"ffffff"])
#define TitleGrayColor   RGB(175,175,175)
#define TabbarGrayColor  ( RI.isNight ? RGB(31,31,31) : RGB(247,247,247))
#define LineGrayColor    RGB(240,240,240)
#define BackGrayColor   [UIColor lq_colorWithHexString:@"f8f8f8"]
#define DeepGreenColor   RGB(20,118,103)
#define LihgtGreenColor   RGB(30,161,20)
#define RedColor   RGB(220,44,50)
#define YellowBackColor   RGB(247,189,19)
#define BlueBackColor   RGB(30,128,240)
#define WhiteBackColor   ( RI.isNight ? RGB(31,31,31) : [UIColor lq_colorWithHexString:@"ffffff"])


//国际化
#define lqLocalized(key,comment) NSLocalizedStringFromTable(key, @"lqlocal", comment)
#define lqStrings(string) NSLocalizedStringFromTable(string, @"lqlocal", nil)

#define LanguageStrings(string) [ConfManager.shared strContentWith:string]


//number转String
#define IntTranslateStr(int_str) [NSString stringWithFormat:@"%ld",(long)int_str]
#define FloatTranslateStr(float_str) [NSString stringWithFormat:@"%.2d",float_str]

#define APPDelegate     [UIApplication sharedApplication].delegate


// View 圆角
#define ViewRadius(View, Radius)\
\
[View.layer setCornerRadius:(Radius)];\
[View.layer setMasksToBounds:YES]


// View 圆角和加边框
#define ViewBorderRadius(View, Radius, Width, Color)\
\
[View.layer setCornerRadius:(Radius)];\
[View.layer setMasksToBounds:YES];\
[View.layer setBorderWidth:(Width)];\
[View.layer setBorderColor:[Color CGColor]]


#endif
