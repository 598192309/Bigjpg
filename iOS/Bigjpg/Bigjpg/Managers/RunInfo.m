//
//  RunInfo.m
//  stock
//
//  Created by Lqq on 14-2-9.
//  Copyright (c) 2014年 Maxicn. All rights reserved.
//


#define ACCOUNT_KEY  @"ACCOUNT_KEY"

#import "RunInfo.h"



@implementation RunInfo{
    
}

+ (RunInfo *)sharedInstance
{
    static dispatch_once_t t;
    static RunInfo *sharedInstance = nil;
    dispatch_once(&t, ^{
        sharedInstance = [[RunInfo alloc] init];
    });
    return sharedInstance;
}

- (id)init
{
    self = [super init];
    if (self){
        _is_logined = [[NSUserDefaults standardUserDefaults ] objectForKey:kUserIsLogin] ? [[[NSUserDefaults standardUserDefaults ] objectForKey:kUserIsLogin] boolValue] : false;
        _isNight = [[NSUserDefaults standardUserDefaults ] objectForKey:kIsNight] ? [[[NSUserDefaults standardUserDefaults ] objectForKey:kIsNight] boolValue] : false;
        _isTouristLogin = [[NSUserDefaults standardUserDefaults ] objectForKey:kTouristLogin] ? [[[NSUserDefaults standardUserDefaults ] objectForKey:kTouristLogin] boolValue] : false;

        [SVProgressHUD setDefaultStyle:_isNight?SVProgressHUDStyleLight:SVProgressHUDStyleDark];

        _autoDownImage = [[NSUserDefaults standardUserDefaults ] objectForKey:kAutoDownImage] ? [[[NSUserDefaults standardUserDefaults ] objectForKey:kAutoDownImage] boolValue] : false;


        NSString *userInfoStr = [[NSUserDefaults standardUserDefaults] objectForKey:@"UserInfoStr"];
        if (userInfoStr.length > 0) {
            _userInfo = [M_User mj_objectWithKeyValues:userInfoStr];
        }
    }
    return self;
}



-(void)setIs_logined:(BOOL)is_logined{
    _is_logined = is_logined;
    [[NSUserDefaults standardUserDefaults] setObject:@(is_logined) forKey:kUserIsLogin];
    [[NSUserDefaults standardUserDefaults] synchronize];
}
- (void)setIsNight:(BOOL)isNight{
    _isNight = isNight;
    [[NSUserDefaults standardUserDefaults] setBool:isNight forKey:kIsNight];
    [[NSUserDefaults standardUserDefaults] synchronize];

}
- (void)setAutoDownImage:(BOOL)autoDownImage{
    _autoDownImage = autoDownImage;
    [[NSUserDefaults standardUserDefaults] setBool:autoDownImage forKey:kAutoDownImage];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (void)setUserInfo:(M_User *)userInfo
{
    _userInfo = userInfo;
    NSString *userInfoStr = @"";
    if (userInfo != nil) {
        userInfoStr = [userInfo mj_JSONString];
    }
    
    [[NSUserDefaults standardUserDefaults] setObject:userInfoStr forKey:@"UserInfoStr"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

-(void)setIsTouristLogin:(BOOL)isTouristLogin
{
    _isTouristLogin = isTouristLogin;
    [[NSUserDefaults standardUserDefaults] setBool:isTouristLogin forKey:kTouristLogin];
    [[NSUserDefaults standardUserDefaults] synchronize];
}


@end
