//
//  I_Enlarge.h
//  Bigjpg
//
//  Created by lqq on 2019/12/26.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "I_Base.h"
#import "M_Enlarge.h"
NS_ASSUME_NONNULL_BEGIN

@interface I_Enlarge : I_Base

/// 重试放大任务
/// @param fids 任务id列表
/// @param successBlock 成功回调
/// @param failureBlock 失败回调
+ (NetworkTask *)retryEnlargeTasks:(NSArray<NSString *>*)fids success:(void(^)(void))successBlock failure:(ErrorBlock)failureBlock;



/// 查询放大任务状态
/// @param fids 任务id列表
/// @param successBlock 成功回调
/// @param failureBlock 失败回调
+ (NetworkTask *)getEnlargeTasksStatus:(NSArray<NSString *> *)fids success:(void(^)(NSMutableArray<M_Enlarge *> *taskList))successBlock failure:(ErrorBlock)failureBlock;



/// 删除放大任务
/// @param fids 任务id列表
/// @param successBlock 成功回调
/// @param failureBlock 失败回调
+ (NetworkTask *)deleteEnlargeTasks:(NSArray *)fids success:(void(^)(void))successBlock failure:(ErrorBlock)failureBlock;



/// 创建放大任务状态（先通过阿里云 oss 上传文件得到url后再创建任务方式）
/// @param x2 放大倍数 1 两倍, 2 四倍, 3 八倍, 4 十六倍,
/// @param style art 卡通图片, photo 照片
/// @param noise 噪点 -1 无, 0 低, 1 中, 2 高, 3 最高
/// @param fileName 图片名
/// @param fileSize 图片字节大小
/// @param fileHeight 图片高度
/// @param filetWidth 图片宽度
/// @param input 图片oss地址
/// @param successBlock 成功回调fid : 任务id ,time:预估时间
/// @param failureBlock 失败回调
+ (NetworkTask *)createEnlargeTask:(int )x2
                             style:(NSString *)style
                             noise:(int)noise
                          fileName:(NSString *)fileName
                          fileSize:(long)fileSize
                        fileHeight:(long)fileHeight
                         fileWidth:(long)filetWidth
                             input:(NSString *)input
                           success:(void(^)(NSString *fid, NSInteger time))successBlock
                           failure:(ErrorBlock)failureBlock;



/// 创建放大任务状态（先通过阿里云 oss 上传文件得到url后再创建任务方式）
/// @param conf 放大信息配置
/// @param successBlock 成功回调 fid : 任务id ,time:预估时间
/// @param failureBlock 失败回调
+ (NetworkTask *)createEnlargeTaskWith:(M_EnlargeConf *)conf
                               success:(void(^)(NSString *fid, NSInteger time))successBlock
                               failure:(ErrorBlock)failureBlock;


/// 批量下载
+ (void)downloadPictureWithUrls:(NSArray *)urlList isAutoDown:(BOOL)autoDownLoad;
@end

NS_ASSUME_NONNULL_END
