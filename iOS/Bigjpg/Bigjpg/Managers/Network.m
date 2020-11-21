//
//  Network.m
//  fullsharetop
//
//  Created by lqq on 16/9/13.
//  Copyright © 2016年 lqq. All rights reserved.
//

#import "Network.h"
#import "AFHTTPSessionManager.h"
#import "Network.h"
#import<CommonCrypto/CommonDigest.h>




#define SignAPPID   @"123456"
#define SignAppSecurityKey   @"d67266586dffe8085126f3383afe8e3c"


@implementation NetworkTask{
    NSURLSessionDataTask *_sessionDatatask;
}

+ (instancetype)netWorkWithSessionDataTask:(NSURLSessionDataTask*)task{
    NetworkTask *atask=[[NetworkTask alloc] init];
    atask->_sessionDatatask=task;
    return atask;
}

- (void)cancel{
    [_sessionDatatask cancel];
}

- (void)suspend{
    [_sessionDatatask suspend];
}

- (void)resume{
    [_sessionDatatask resume];
}
@end







@implementation Network{
    AFHTTPSessionManager *_manager;
    NSString *_appVersion;
    NSString *_clientType;
    BOOL      _isTest;
}

+(instancetype)shareInstance{
    static Network *network=nil;
    static dispatch_once_t singlet;
    dispatch_once(&singlet, ^{
        network=[[Network alloc] init];
    });
    return network;
}

-(instancetype)init{
    self=[super init];
    if(self){
        [self instannceManagerWithHost:KHOST];
    }
    return self;
}

-(void)instannceManagerWithHost:(NSString*)host{
    _manager=[[AFHTTPSessionManager alloc] initWithBaseURL:[NSURL URLWithString:host]];
    
    _manager.securityPolicy = [AFSecurityPolicy policyWithPinningMode:AFSSLPinningModeNone];
    _manager.securityPolicy.allowInvalidCertificates = YES;
    [_manager.securityPolicy setValidatesDomainName:NO];
    
    // 二进制格式
    _manager.requestSerializer=[AFHTTPRequestSerializer serializer];
    // 设置接受文本类型
//    [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    // 设置接受文本类型Content-Type
    [_manager.requestSerializer setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    
    // 二进制格式
    _manager.responseSerializer = [AFJSONResponseSerializer serializer];
    // 设置接受文本类型
    _manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json", @"text/json", @"text/javascript",@"text/html",@"application/x-www-form-urlencoded", nil];
    //超时
    _manager.requestSerializer.timeoutInterval = 15;

}

- (void)changeHost:(NSNotification *)noti
{
    NSString *host = [noti.object safeObjectForKey:@"host"];
    if (host) {
        [self instannceManagerWithHost:host];
    }
    
}


- (nullable NetworkTask *)POST:(NSString *)URLString
                    parameters:(nullable id)parameters
                 criticalValue:(nullable NSDictionary*)criticalValue
                       success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                       failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure{

    URLString=[URLString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    NSURLSessionDataTask *t = [_manager POST:URLString parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        [self _handdleSuccessWithTask:task responseObject:responseObject success:success failure:failure];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [self _handdleFailureWithTask:task error:error failure:failure];
    }];
    
    return [NetworkTask netWorkWithSessionDataTask:t];
}


- (nullable NetworkTask *)GET:(NSString *)URLString
                   parameters:(nullable id)parameters
                criticalValue:(nullable NSDictionary*)criticalValue
                      success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                      failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure
{

    URLString=[URLString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    
    NSURLSessionDataTask *t = [_manager GET:URLString parameters:parameters progress:^(NSProgress * _Nonnull uploadProgress) {
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
         [self _handdleSuccessWithTask:task responseObject:responseObject success:success failure:failure];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [self _handdleFailureWithTask:task error:error failure:failure];
    }];
    return [NetworkTask netWorkWithSessionDataTask:t];
}

- (nullable NetworkTask *)DELETE:(NSString *)URLString
                   parameters:(nullable id)parameters
                criticalValue:(nullable NSDictionary*)criticalValue
                      success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                      failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure
{

    URLString=[URLString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    
    NSURLSessionDataTask *t =[_manager DELETE:URLString parameters:parameters success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        [self _handdleSuccessWithTask:task responseObject:responseObject success:success failure:failure];
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        [self _handdleFailureWithTask:task error:error failure:failure];
    }];
    return [NetworkTask netWorkWithSessionDataTask:t];
}


#pragma mark -Pravite
/**
 *  刷新请求头
 *
 *  @param criticalValue 放到加密的内容（防止篡改）
 */
-(void)_refreshHeaderWithCriticalValue:(NSDictionary*)criticalValue
{
   
    
}


- (void)signHeaderWithParams:(id)parameters
{
}

/**
 *  处理成功请求
 */
-(void)_handdleSuccessWithTask:(NSURLSessionDataTask*)task
                responseObject:(id)responseObject
                       success:(nullable void (^)(NSURLSessionDataTask *task, id resultObject))success
                       failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    NSDictionary *obj;
    if (![responseObject isKindOfClass:[NSDictionary class]] ) {
        NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableLeaves error:nil];
        if (jsonDict) {
            obj = jsonDict;
        } else {
            failure(task,[NSError lq_errorWithMsg:@"数据格式错误" domain:@"Response Error" code:10000]);
            return;
        }
    }else{
        obj = responseObject;
    }
    NSLog(@"%@",obj);
    success(task,obj);
}

/**
 *  处理失败请求
 */
-(void)_handdleFailureWithTask:(NSURLSessionDataTask*)task
                         error:(NSError*)error
                       failure:(nullable void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    failure(task,[NSError lq_errorWithMsg:@"error" domain:error.domain code:-99999]);
}

-(void)setCookie:(NSString*)key
           value:(NSString*)value{

    NSString *cookiestring=nil;
    for (NSHTTPCookie *cookie in [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies]) {
        if ([cookie.name isEqualToString:key]) {
            continue;
        }
        if (cookiestring) {
            cookiestring=[NSString stringWithFormat:@"%@;%@=%@",cookiestring,cookie.name,cookie.value];
        }else{
            cookiestring=[NSString stringWithFormat:@"%@=%@",cookie.name,cookie.value];
        }
    }
    if (cookiestring) {
        cookiestring=[NSString stringWithFormat:@"%@;%@=%@",cookiestring,key,value];
    }else{
        cookiestring=[NSString stringWithFormat:@"%@=%@",key,value];
    }
    [_manager.requestSerializer setValue:cookiestring forHTTPHeaderField:@"Cookie"];
}




@end
