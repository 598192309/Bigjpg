//
//  RunInfo.h
//  stock
//
//  Created by Lqq on 14-2-9.
//  Copyright (c) 2014年 Maxicn. All rights reserved.
//




#define RI ([RunInfo sharedInstance])


#import <UIKit/UIKit.h>
#import "M_User.h"
@interface RunInfo : NSObject

+ (RunInfo *)sharedInstance;
@property(nonatomic,assign)BOOL is_logined;//是否登录 以这个为标准判断 不以上面判断
@property (nonatomic, assign)BOOL isNight;//夜间模式
@property (nonatomic, assign)BOOL autoDownImage;//自动下载放大的图片

@property (nonatomic, strong)M_User *userInfo;

@property (nonatomic, assign) BOOL isTouristLogin;
@end
