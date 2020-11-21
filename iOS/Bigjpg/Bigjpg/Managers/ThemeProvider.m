//
//  ThemeProvider.m
//  stock
//
//  Created by Lqq on 14-4-3.
//  Copyright (c) 2014年 Maxicn. All rights reserved.
//




#import "ThemeProvider.h"

@implementation ThemeProvider{
}
+ (ThemeProvider *)sharedInstance
{
    static dispatch_once_t t;
    static ThemeProvider *sharedInstance = nil;
    dispatch_once(&t, ^{
        sharedInstance = [[ThemeProvider alloc] init];
    });
    return sharedInstance;
}

-(id)init
{
    self=[super init];
    if(self){
        
    }
    return self;
}

-(UIColor *)greenColor
{
    return [UIColor lq_colorWithHexString:@"1AAC19"];
}


-(UIColor *)yellowColor
{
    return [UIColor lq_colorWithHexString:@"F9C713"];
}

-(UIColor *)redColor
{
    return [UIColor lq_colorWithHexString:@"E54340"];
}

-(UIColor *)grayColor
{
    return [UIColor lq_colorWithHexString:@"999999"];
}

-(UIColor *)backGrayColor
{
    return [UIColor lq_colorWithHexString:@"EEEEEE"];
}

-(UIColor *)lightGrayColor
{
    return [UIColor lq_colorWithHexString:@"757575"];
}

-(UIColor *)whiteColor
{
    return [UIColor whiteColor];
}

-(UIColor *)blackColor
{
    return [UIColor lq_colorWithHexString:@"494949"];
}



- (UIColor *)progressTrackColor
{
    return [UIColor lq_colorWithHexString:@"757575"];
}
@end
