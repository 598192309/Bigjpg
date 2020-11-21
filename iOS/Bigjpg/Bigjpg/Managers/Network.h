//
//  Network.h
//  fullsharetop
//
//  Created by lqq on 16/9/13.
//  Copyright © 2016年 lqq. All rights reserved.
//

#import <Foundation/Foundation.h>


#define KHOST    @"https://bigjpg.com"      //正式环境

//#define KHOST    @"http://47.56.91.245:2005"      //测试环境

#define NET [Network shareInstance]

#define NetErrorCode   -9999

#define NOT_REAL_BRACELET_DATA  YES


/**
 *  一个网络请求任务
 */
@interface NetworkTask : NSObject
+ (nullable instancetype)netWorkWithSessionDataTask:(nullable NSURLSessionDataTask*)task;
- (void)cancel;
- (void)suspend;
- (void)resume;
@end




@interface Network : NSObject
#if (defined(__IPHONE_OS_VERSION_MAX_ALLOWED) && __IPHONE_OS_VERSION_MAX_ALLOWED >= 70000) || (defined(__MAC_OS_X_VERSION_MAX_ALLOWED) && __MAC_OS_X_VERSION_MAX_ALLOWED >= 1090) || TARGET_OS_WATCH

NS_ASSUME_NONNULL_BEGIN

+(instancetype)shareInstance;


- (nullable NetworkTask *)POST:(NSString *)URLString
                    parameters:(nullable id)parameters
                 criticalValue:(nullable NSDictionary*)criticalValue
                       success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                       failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure;


- (nullable NetworkTask *)GET:(NSString *)URLString
                    parameters:(nullable id)parameters
                 criticalValue:(nullable NSDictionary*)criticalValue
                       success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                       failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure;

- (nullable NetworkTask *)DELETE:(NSString *)URLString
   parameters:(nullable id)parameters
criticalValue:(nullable NSDictionary*)criticalValue
      success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                         failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure;

-(void)instannceManagerWithHost:(NSString*)host;

@property (nonatomic, strong) NSString *servePath;

-(void)setCookie:(NSString*)key
           value:(NSString*)value;



NS_ASSUME_NONNULL_END

#endif
@end
