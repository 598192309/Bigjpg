//
//  AppDelegate.m
//  Bigjpg
//
//  Created by lqq on 2019/12/20.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "LaunchingViewController.h"
#import "I_Account.h"
#import "RMStore.h"
#import "RMStoreAppReceiptVerifier.h"
#import "RMStoreKeychainPersistence.h"
#import "KKUUID.h"
@interface AppDelegate () <RMStoreReceiptVerifier>

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSArray *products = @[@"basic_dy",
                  @"std_dy",
                  @"pro_dy"];
    [[RMStore defaultStore] requestProducts:[NSSet setWithArray:products] success:^(NSArray *products, NSArray *invalidProductIdentifiers) {
        NSLog(@"请求商品列表成功");
    } failure:^(NSError *error) {
        NSLog(@"请求商品列表失败");
    }];
    
    
    [RMStore defaultStore].receiptVerifier = self;
    
    
    self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    self.window.rootViewController = [MainTabBarController new];
//    self.window.rootViewController = [LaunchingViewController new];
    if (RI.is_logined) {
        [self requestUserInfo];
    }
    
    [self.window makeKeyAndVisible];
    
    NSLog(@"%@",LanguageStrings(@"donate"));
    
    return YES;
}
- (void)verifyTransaction:(SKPaymentTransaction*)transaction success:(void (^)(void))successBlock failure:(void (^)(NSError *error))failureBlock
{
 
    if (RI.userInfo.username.length <= 0) {
        failureBlock(nil);
    } else {
        //生成购买凭证
        NSURL *receiptUrl = [[NSBundle mainBundle] appStoreReceiptURL];
        NSData *receipt;
        receipt = [NSData dataWithContentsOfURL:receiptUrl];
        NSString* receipt_data = [receipt base64EncodedStringWithOptions:0];
        NSString *productId = transaction.payment.productIdentifier;
        NSString *transaction_id = transaction.transactionIdentifier;

        [I_Account authWithUserName:RI.userInfo.username product_id:productId transaction_id:transaction_id receipt_data:receipt_data success:^(NSDictionary * _Nonnull resultObject) {
            NSString *status = SAFE_VALUE_FOR_KEY(resultObject, @"status");//ok代表成功
            if ([status isEqualToString:@"ok"]) {
//            if (YES) {
                NSLog(@"服务器验证成功");
                successBlock();
                if (RI.isTouristLogin) {
                    QMUIAlertController *vc = [QMUIAlertController alertControllerWithTitle:LanguageStrings(@"restore_remind") message:[NSString stringWithFormat:@"%@",RI.userInfo.username] preferredStyle:QMUIAlertControllerStyleAlert];
                    QMUIAlertAction *action = [QMUIAlertAction actionWithTitle:LanguageStrings(@"Copy") style:QMUIAlertActionStyleCancel handler:^(__kindof QMUIAlertController * _Nonnull aAlertController, QMUIAlertAction * _Nonnull action) {
                            UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
                            pasteboard.string = RI.userInfo.username;
                            [LSVProgressHUD showSuccessWithStatus:@"Copy Success !"];
                    }];
                    [vc addAction:action];
                    [vc showWithAnimated:YES];
                }
            } else {
                NSLog(@"服务器验证失败");
                [LSVProgressHUD dismiss];
                long info = [SAFE_VALUE_FOR_KEY(resultObject, @"info")longValue];
                if (info == 21005 || (info>= 21100 && info <= 21199)) {
                    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                        [self verifyTransaction:transaction success:successBlock failure:failureBlock];
                    });
                }
            }
        } failure:^(NSError *error) {
            NSLog(@"服务器验证请求失败");
            failureBlock([NSError errorWithDomain:@"Net Error" code:RMStoreErrorCodeUnableToCompleteVerification userInfo:nil]);
        }];

    }
}


#pragma mark - 自定义

+ (void)presentLoginViewContrller
{
    AppDelegate *del = (AppDelegate *)[UIApplication sharedApplication].delegate;
    BaseNavgationController*nav = [[BaseNavgationController alloc] initWithRootViewController:[[LoginViewController alloc] init]];
    [del.window.rootViewController presentViewController:nav animated:YES completion:nil];
}

//获取用户信息
- (void)requestUserInfo{
    [I_Account getUserInfoOnSuccess:^(M_User * _Nonnull userInfo) {
    } failure:^(NSError *error) {
        
    }];
}
@end
