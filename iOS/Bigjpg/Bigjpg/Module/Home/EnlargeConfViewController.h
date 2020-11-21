//
//  EnlargeConfViewController.h
//  Bigjpg
//
//  Created by lqq on 2019/12/30.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "BaseViewController.h"
#import "M_Enlarge.h"
NS_ASSUME_NONNULL_BEGIN

@interface EnlargeConfViewController : BaseViewController
+ (instancetype)controllerWithEnlargeUploads:(NSArray<M_EnlargeUpload *> *)enlargeUploads;
@end

NS_ASSUME_NONNULL_END
