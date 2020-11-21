//
//  ThemeProvider.h
//  stock
//
//  Created by Lqq on 14-4-3.
//  Copyright (c) 2014年 Maxicn. All rights reserved.
//

#import <UIKit/UIKit.h>

#define TP ([ThemeProvider sharedInstance])


@interface ThemeProvider : NSObject

+ (ThemeProvider *)sharedInstance;

@property (nonatomic, strong) UIColor *greenColor;

@property (nonatomic, strong) UIColor *yellowColor;

@property (nonatomic, strong) UIColor *redColor;

@property (nonatomic, strong) UIColor *grayColor;

@property (nonatomic, strong) UIColor *whiteColor;

@property (nonatomic, strong) UIColor *blackColor;

@property (nonatomic, strong) UIColor *lightGrayColor;

@property (nonatomic, strong) UIColor *backGrayColor;;

@property (nonatomic, strong) UIColor *progressTrackColor;
@end
