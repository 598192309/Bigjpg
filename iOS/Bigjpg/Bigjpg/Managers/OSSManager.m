//
//  OSSManager.m
//  Bigjpg
//
//  Created by JS on 2019/12/26.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "OSSManager.h"

@interface OSSManager ()
@property (nonatomic, strong) OSSClient *client;
@end

@implementation OSSManager
{
    
}
+ (OSSManager *)shared
{
    static dispatch_once_t t;
    static OSSManager *sharedInstance = nil;
    dispatch_once(&t, ^{
        sharedInstance = [[OSSManager alloc] init];
        [sharedInstance initOSS];
    });
    return sharedInstance;
}

- (void)initOSS{
    OSSPlainTextAKSKPairCredentialProvider *pCredential = [[OSSPlainTextAKSKPairCredentialProvider alloc] initWithPlainTextAccessKey:[ConfManager.shared oss_accesskeyid] secretKey:[ConfManager.shared oss_accesskeysecret]];
    OSSClientConfiguration *cfg = [[OSSClientConfiguration alloc] init];
    cfg.maxRetryCount = 3;
    cfg.timeoutIntervalForRequest = 60;
    self.client = [[OSSClient alloc] initWithEndpoint:[ConfManager.shared oss_endpoint] credentialProvider:pCredential clientConfiguration:cfg];

}

+ (void)asyncUploadData:(NSData *)data objectKey:(NSString *)objectKey progress:(void(^)(int64_t bytesSent, int64_t totalByteSent, int64_t totalBytesExpectedToSend))progress success:(void (^)(OSSTask *task))success
failure:(void (^)(NSError *error))failure {
    if (![objectKey oss_isNotEmpty]) {
        NSError *error = [NSError errorWithDomain:NSInvalidArgumentException code:0 userInfo:@{NSLocalizedDescriptionKey: @"objectKey should not be nil"}];
        failure(error);
        return;
    }
    
    OSSPutObjectRequest * put = [OSSPutObjectRequest new];
    put.bucketName = [ConfManager.shared oss_bucket];
    put.objectKey = objectKey;
    put.uploadingData = data;
    put.uploadProgress = ^(int64_t bytesSent, int64_t totalByteSent, int64_t totalBytesExpectedToSend) {
        progress(bytesSent,totalByteSent,totalBytesExpectedToSend);
        float pro = 1.f * totalByteSent / totalBytesExpectedToSend;
        OSSLogDebug(@"上传文件进度: %f", pro);
    };
    put.callbackParam = @{@"objectKey": objectKey};
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        OSSTask * task = [OSSManager.shared.client putObject:put];
        [task continueWithBlock:^id(OSSTask *task) {
            dispatch_async(dispatch_get_main_queue(), ^{
                if (task.error) {
                    failure(task.error);
                } else {
                    success(task);
                }
            });
            
            return nil;
        }];
    });
}

+ (NSString *)getOSSUrl{
//    https://oss-accelerate.aliyuncs.com
    NSArray *arr = [[ConfManager.shared oss_endpoint] componentsSeparatedByString:@"//"];
    return [NSString stringWithFormat:@"%@//%@.%@",[arr safeObjectAtIndex:0],[ConfManager.shared oss_bucket],[arr safeObjectAtIndex:1]];
}
@end
