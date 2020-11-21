//
//  BaseTableViewController.h
//  FullShareTop
//
//  Created by lqq on 17/3/21.
//  Copyright © 2017年 FSB. All rights reserved.
//

#import <UIKit/UIKit.h>


/**
 TableViewController基类
 */
@interface BaseTableViewController : UITableViewController


@property(assign,nonatomic)BOOL isFirstViewDidAppear;// 是否是第一次出现


@property (nonatomic, assign) BOOL isVisible;//是否在显示


@end
