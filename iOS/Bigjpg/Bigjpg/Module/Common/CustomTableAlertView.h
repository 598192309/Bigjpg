//
//  CustomTableAlertView.h
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CustomTableAlertView : UIView
- (void)configUIWithArr:(NSArray *)arr;
@property(nonatomic,copy)void(^CustomTableAlertChooseBlock)(NSInteger index,NSString *str);
@property(nonatomic,copy)void(^CustomTableAlertRemoveBlock)();

@end

@interface CustomTableAlertCell : UITableViewCell
- (void)refreshUIWithTitle:(NSString *)title;
@end
NS_ASSUME_NONNULL_END
