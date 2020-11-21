//
//  LSVProgressHUD.h
//  xskj
//
//  Created by 黎芹 on 16/5/9.
//  Copyright © 2016年 lq. All rights reserved.
//

#import <SVProgressHUD/SVProgressHUD.h>

@interface LSVProgressHUD : SVProgressHUD
+ (void)showError:(NSError *)error;
+(void)showGIFImage;
@end
