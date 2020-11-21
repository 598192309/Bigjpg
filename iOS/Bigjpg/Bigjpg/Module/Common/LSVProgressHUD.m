//
//  LSVProgressHUD.m
//  xskj
//
//  Created by 黎芹 on 16/5/9.
//  Copyright © 2016年 lq. All rights reserved.
//

#import "LSVProgressHUD.h"
#import "SVProgressHUD.h"
@implementation LSVProgressHUD

+ (void)load{
////    [self setMinimumDismissTimeInterval:1.0];
//    [self setMinimumDismissTimeInterval:MAXFLOAT];
//    
//    [self setDefaultStyle:SVProgressHUDStyleCustom];
//    
////    [self setBackgroundLayerColor:[UIColor colorWithHexString:viewBackgroundColor alpha:1.0]];
//    [self setForegroundColor:[UIColor whiteColor]];
//    //设置不可交换 
////    [self setDefaultMaskType:SVProgressHUDMaskTypeClear];
//    //设置可交互  #121719 (60%)
//    [self setDefaultMaskType:SVProgressHUDMaskTypeNone];
//    self.imageViewSize = CGSizeMake(AdaptedWidth(45), AdaptedWidth(45));
////    self.backgroundColor = [UIColor colorWithHexString:@"#14181a"];
//    self.backgroundColor = [UIColor lq_colorWithHexString:@"#14181A" alpha:0.85];
//
//    [self setInfoImage:nil];
//    
    


}

+ (void)show {
    [SVProgressHUD show];
}


+ (void)showError:(NSError *)error{
    NSString *errStr = error.lq_errorMsg;
    [self showInfoWithStatus:LanguageStrings(errStr)];
    [self dismissTime];
}

+(void)showInfoWithStatus:(NSString *)status{
    [super showInfoWithStatus:status];
    [self dismissTime];
}
+(void)showWithStatus:(NSString *)status{
    [super showWithStatus:status];
    [self dismissTime];
}
+(void)showErrorWithStatus:(NSString *)status{
    [super showErrorWithStatus:status];
    [self dismissTime];
}
+(void)dismissTime{
    [self dismissWithDelay:2.f];
}
@end
