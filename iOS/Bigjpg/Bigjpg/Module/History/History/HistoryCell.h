//
//  HistoryCell.h
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "M_Enlarge.h"
NS_ASSUME_NONNULL_BEGIN

@interface HistoryCell : UITableViewCell
- (void)configUIWithItem:(M_EnlargeHistory *)item downAll:(BOOL)downAll backColor:(UIColor *)backColor;
@end

NS_ASSUME_NONNULL_END
