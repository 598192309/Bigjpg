//
//  BuyCell.h
//  Bigjpg
//
//  Created by lqq on 2020/1/14.
//  Copyright © 2020 lqq. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BuyCell : UITableViewCell
- (void)configUIWithArr:(NSArray *)arr color:(UIColor *)color;
@property (nonatomic, copy) NSString *productId;

/**点击购买*/
@property(nonatomic,copy)void(^buyCellConfirmBtnClickBlock)(NSString *productId,UIButton *sender);
@end

NS_ASSUME_NONNULL_END
