//
//  HomeTableViewCell.h
//  Bigjpg
//
//  Created by lqq on 2019/12/27.
//  Copyright © 2019 lqq. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "M_Enlarge.h"
NS_ASSUME_NONNULL_BEGIN

@class HomeTableViewCell;

@protocol HomeTableViewCellDelegate <NSObject>

- (void)uploadEvent:(HomeTableViewCell *)cell enlarge:(M_EnlargeUpload *)upload;

@end

@interface HomeTableViewCell : UITableViewCell
@property (nonatomic, strong) M_EnlargeUpload *upload;
@property (nonatomic, weak) id<HomeTableViewCellDelegate> delegate;
@end

NS_ASSUME_NONNULL_END
