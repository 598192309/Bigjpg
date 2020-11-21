//
//  NewFeatureScrollView.h
//  Encropy
//
//  Created by Lqq on 2019/6/24.
//  Copyright © 2019 Lq. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface NewFeatureScrollView : UIView
/**
 初始化方法
 @param imageNames 滚动显示的图片名称数组
 */
+ (void)leadPageViewWithImageNames:(NSArray *)imageNames;
@end

NS_ASSUME_NONNULL_END
