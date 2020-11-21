//
//  LoginCustomView.h
//  Encropy
//
//  Created by Lqq on 2019/4/25.
//  Copyright © 2019年 Lq. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface LoginCustomView : UIView
/**点击忘记密码*/
@property(nonatomic,copy)void(^loginCustomViewForgetBtnClickBlock)(NSDictionary *dict);

/**点击登录*/
@property(nonatomic,copy)void(^loginCustomViewConfirmBtnClickBlock)(NSDictionary *dict,UIButton *sender);

/**点击注册*/
@property(nonatomic,copy)void(^loginCustomViewZhuceClickBlock)(NSDictionary *dict);

@end

NS_ASSUME_NONNULL_END
