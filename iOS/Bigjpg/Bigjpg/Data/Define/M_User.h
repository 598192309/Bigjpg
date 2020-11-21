//
//  M_User.h
//  Bigjpg
//
//  Created by lqq on 2019/12/23.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "M_Base.h"
#import "M_Enlarge.h"
NS_ASSUME_NONNULL_BEGIN

@interface M_User : M_Base
//用户名
@property (nonatomic, copy) NSString *username;
//是否过期或者免费用户
@property (nonatomic, assign) BOOL is_expire;
//过期时间 "2020-04-14 00:00:00"
@property (nonatomic, copy) NSString *expire;
//不知道是啥，应该是userid
@property (nonatomic, assign) long used;
//昵称，为null的情况下用username
@property (nonatomic, copy) NSString *nickname;
//不用管
@property (nonatomic, copy) NSString *api_key;
//free basic std pro,根据这个区ConfManager获取对应的描述
@property (nonatomic, copy) NSString *version;
//放大历史
@property (nonatomic, strong) NSMutableArray<M_EnlargeHistory *>* historyList;
@end






NS_ASSUME_NONNULL_END
