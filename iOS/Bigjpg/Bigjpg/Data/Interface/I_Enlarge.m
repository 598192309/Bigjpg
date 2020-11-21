//
//  I_Enlarge.m
//  Bigjpg
//
//  Created by lqq on 2019/12/26.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "I_Enlarge.h"
#import "AFHTTPSessionManager.h"

@implementation I_Enlarge
+ (NetworkTask *)retryEnlargeTasks:(NSArray*)fids success:(void(^)(void))successBlock failure:(ErrorBlock)failureBlock
{
    NSString *value = [fids mj_JSONString];
    NSDictionary *params = @{@"fids":value};
    return [NET GET:@"retry" parameters:params criticalValue:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nonnull resultObject) {
           NSString *status = SAFE_VALUE_FOR_KEY(resultObject, @"status");//ok代表成功
           if([status isEqualToString:@"ok"]){
               successBlock();
           }else{
               failureBlock([NSError lq_errorWithMsg:status domain:@"Response Error" code:10000]);
           }
    } failure:^(NSURLSessionDataTask * _Nonnull task, NSError * _Nonnull error) {
           failureBlock(error);
    }];
}

+ (NetworkTask *)getEnlargeTasksStatus:(NSArray<NSString *> *)fids success:(void(^)(NSMutableArray<M_Enlarge *> *taskList))successBlock failure:(ErrorBlock)failureBlock
{
    NSString *value = [fids mj_JSONString];
    NSDictionary *params = @{@"fids":value};
   return [NET GET:@"free" parameters:params criticalValue:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nonnull resultObject) {
       NSDictionary *result = resultObject;
       NSArray *fids = result.allKeys;
       NSMutableArray<M_Enlarge *>* list = [NSMutableArray<M_Enlarge *> array];
       for (NSString *key in fids) {
           M_Enlarge *task = [[M_Enlarge alloc] init];
           task.fid = key;
           NSArray *value = [result safeObjectForKey:key];
           task.status = [value safeObjectAtIndex:0];
           task.output = [value safeObjectAtIndex:1];
           [list addObject:task];
       }
       successBlock(list);
   } failure:^(NSURLSessionDataTask * _Nonnull task, NSError * _Nonnull error) {
          failureBlock(error);
   }];
}

+ (NetworkTask *)deleteEnlargeTasks:(NSArray *)fids success:(void(^)(void))successBlock failure:(ErrorBlock)failureBlock
{
    NSString *value = [fids mj_JSONString];
     NSDictionary *params = @{@"fids":value};
    return [NET DELETE:@"free" parameters:params criticalValue:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nonnull resultObject) {
        NSString *status = SAFE_VALUE_FOR_KEY(resultObject, @"status");//ok代表成功
           if([status isEqualToString:@"ok"]){
               successBlock();
           }else{
               failureBlock([NSError lq_errorWithMsg:status domain:@"Response Error" code:10000]);
           }
    } failure:^(NSURLSessionDataTask * _Nonnull task, NSError * _Nonnull error) {
           failureBlock(error);
    }];
}


+ (NetworkTask *)createEnlargeTask:(int )x2
     style:(NSString *)style
     noise:(int)noise
  fileName:(NSString *)fileName
  fileSize:(long)fileSize
fileHeight:(long)fileHeight
 fileWidth:(long)filetWidth
     input:(NSString *)input
   success:(void(^)(NSString *fid, NSInteger time))successBlock
   failure:(ErrorBlock)failureBlock {
    
    NSDictionary *jsonDic = @{@"x2":[NSString stringWithFormat:@"%d",x2],@"style":SAFE_NIL_STRING(style),@"noise":[NSString stringWithFormat:@"%d",noise],@"file_name":SAFE_NIL_STRING(fileName),@"files_size":@(fileSize),@"file_height":@(fileHeight),@"file_width":@(filetWidth),@"input":SAFE_NIL_STRING(input)};
    
    NSString *value = [jsonDic mj_JSONString];
    NSDictionary *params = @{@"conf":SAFE_NIL_STRING(value)};
    return [NET POST:@"task" parameters:params criticalValue:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nonnull resultObject) {
        NSString *status = SAFE_VALUE_FOR_KEY(resultObject, @"status");//ok代表成功
        if([status isEqualToString:@"ok"]){
            NSString *fid = SAFE_VALUE_FOR_KEY(resultObject, @"info");
            NSInteger time = [SAFE_VALUE_FOR_KEY(resultObject, @"time") integerValue];
            successBlock(fid,time);
        }else{
            failureBlock([NSError lq_errorWithMsg:status domain:@"Response Error" code:10000]);
        }
    } failure:^(NSURLSessionDataTask * _Nonnull task, NSError * _Nonnull error) {
        failureBlock(error);
    }];
}


+ (NetworkTask *)createEnlargeTaskWith:(M_EnlargeConf *)conf
success:(void(^)(NSString *fid, NSInteger time))successBlock
failure:(ErrorBlock)failureBlock
{
    return [self createEnlargeTask:conf.x2 style:conf.style noise:conf.noise fileName:conf.file_name fileSize:conf.files_size fileHeight:conf.file_height fileWidth:conf.file_width input:conf.input success:successBlock failure:failureBlock];
}

/// 批量下载
+ (void)downloadPictureWithUrls:(NSArray *)urlList isAutoDown:(BOOL)autoDownLoad
{
//    urlList = @[@"https://bigjpg-server.oss-cn-shenzhen.aliyuncs.com/0c07e8fd33302caeddee545285eb1002_4_-1_art.jpg"];
    if (urlList.count == 0) {
        return;
    }
    if (!autoDownLoad) {
        [LSVProgressHUD show];
    }
    __block int count = 0;
    for (NSString *output in urlList) {
        
       /* 创建网络下载对象 */
       AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
        /* 下载地址 */
        NSURL *url = [NSURL URLWithString:output];
        NSURLRequest *request = [NSURLRequest requestWithURL:url];
        /* 下载路径 */
        NSString *path = [[LqSandBox docPath] lq_subDirectory:@"pic"];
        NSString *filePath = [path stringByAppendingPathComponent:url.lastPathComponent];
        
        /* 开始请求下载 */
        NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {
            NSLog(@"下载进度：%.0f％", downloadProgress.fractionCompleted * 100);
        } destination:^NSURL * _Nonnull(NSURL * _Nonnull targetPath, NSURLResponse * _Nonnull response) {
            dispatch_async(dispatch_get_main_queue(), ^{
                //如果需要进行UI操作，需要获取主线程进行操作
            });
            /* 设定下载到的位置 */
            return [NSURL fileURLWithPath:filePath];
                    
        } completionHandler:^(NSURLResponse * _Nonnull response, NSURL * _Nullable filePath1, NSError * _Nullable error) {
             NSLog(@"下载完成");
            
             if (error) {
                 NSLog(@"下载失败%@",output);
                 if (!autoDownLoad && urlList.count == 1) {
                     [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"no_succ")];
                 }
             } else {
                 QMUISaveImageAtPathToSavedPhotosAlbumWithAlbumAssetsGroup(filePath, nil, ^(QMUIAsset *asset, NSError *error) {
                     if (!autoDownLoad) {
                         count ++;
                         if (count >= urlList.count) {
                             if (error) {
                                 [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"save_fail")];
                             } else {
                                [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"save_succ")];

                             }
                         }
                     }
                 });

             }
        }];
         [downloadTask resume];
    }
    

    
    
}
@end
