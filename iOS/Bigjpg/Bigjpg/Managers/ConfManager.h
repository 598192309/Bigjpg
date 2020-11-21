//
//  ConfManager.h
//  Bigjpg
//
//  Created by 黎芹 on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//


#define kChangeLanguageNotification   @"kChangeLanguageNotification"


#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ConfManager : NSObject

+ (ConfManager *)shared;
//整个配置信息
@property (nonatomic, copy) NSDictionary *conf;
//更新配置
- (void)updateConf:(NSDictionary *)conf;
//语言
@property (nonatomic, copy, readonly) NSString *localLanguage;

//修改语言
- (void)changeLocalLanguage:(NSString *)language;

//根据key来获取值
- (id)contentWith:(NSString *)key;
//确定是字符串时可以使用此方法
- (NSString *)strContentWith:(NSString *)key;

//OSS
- (NSString *)oss_endpoint;
- (NSString *)oss_accesskeysecret;
- (NSString *)oss_bucket;
- (NSString *)oss_accesskeyid;
@end

NS_ASSUME_NONNULL_END
