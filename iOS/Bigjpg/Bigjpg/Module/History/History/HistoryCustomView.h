//
//  HistoryCustomView.h
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HistoryCustomView : UIView
/**点击确定*/
@property(nonatomic,copy)void(^historyCustomViewConfirmBtnClickBlock)(NSDictionary *dict,UIButton *sender);
/**点击down all */
@property(nonatomic,copy)void(^historyCustomViewDownAllBtnClickBlock)(NSDictionary *dict,UIButton *sender);
/**点击cancell */
@property(nonatomic,copy)void(^historyCustomViewCancleBtnClickBlock)(NSDictionary *dict,UIButton *sender);
- (void)reset;
@property (nonatomic,strong)UIButton *downloadBtn;

@end

NS_ASSUME_NONNULL_END
