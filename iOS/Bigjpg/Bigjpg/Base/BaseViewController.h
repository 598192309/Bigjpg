//
//  BaseViewController.h
//  FullShareTop
//
//  Created by lqq on 17/3/21.
//  Copyright © 2017年 FSB. All rights reserved.
//

#import <UIKit/UIKit.h>


/**
 Controller基类
 */
@interface BaseViewController : UIViewController



@property(assign,nonatomic)BOOL isFirstViewDidAppear;// 是否是第一次出现


@property (nonatomic, assign) BOOL isVisible;//是否在显示
@property (nonatomic, assign) BOOL isHideBackItem;

///右滑返回功能，默认开启（YES）
- (BOOL)gestureRecognizerShouldBegin;



//添加导航栏
@property (nonatomic,strong) UIView * navigationView;
@property (nonatomic,strong) UIButton * navigationBackButton;
@property (nonatomic,strong) UIButton * navigationBackTitleButton;
@property (nonatomic,strong) UIButton * navigationBackSecButton;//在返回按钮后面的
@property (nonatomic,strong) UIView * navigationbackgroundLine;
@property (nonatomic,strong) UILabel * navigationTextLabel;//单个

@property (nonatomic,strong) UILabel * navigationTextTopLabel;//偏上
@property (nonatomic,strong) UILabel * navigationTextBottomLabel;//偏上
@property (nonatomic,strong) UIButton * navigationRightBtn;
@property (nonatomic,strong) UIButton * navigationRightSecBtn;
@property (nonatomic,strong) UIButton * navigationRightThirdBtn;

@property (nonatomic,strong) UIButton * navigationLastBtn;//上一个
@property (nonatomic,strong) UIButton * navigationNextBtn;//下一个


-(void)addNavigationView;
-(NSString *)backItemImageName;
@end
