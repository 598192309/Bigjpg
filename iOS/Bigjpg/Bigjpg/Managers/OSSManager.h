//
//  OSSManager.h
//  Bigjpg
//
//  Created by JS on 2019/12/26.
//  Copyright © 2019 lqq. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AliyunOSSiOS/OSSService.h>

NS_ASSUME_NONNULL_BEGIN

@interface OSSManager : NSObject
+ (OSSManager *)shared;

+ (void)asyncUploadData:(NSData *)data objectKey:(NSString *)objectKey progress:(void(^)(int64_t bytesSent, int64_t totalByteSent, int64_t totalBytesExpectedToSend))progress success:(void (^)(OSSTask *task))success
                failure:(void (^)(NSError *error))failure;

+ (NSString *)getOSSUrl;

@end

NS_ASSUME_NONNULL_END
