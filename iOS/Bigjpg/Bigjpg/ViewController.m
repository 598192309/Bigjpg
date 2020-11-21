//
//  ViewController.m
//  Bigjpg
//
//  Created by lqq on 2019/12/20.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "ViewController.h"
#import "I_Account.h"
#import "I_Enlarge.h"
#import <ZLPhotoBrowser/ZLPhotoBrowser.h>


@interface M_EnlageConfDetail : M_EnlargeConf
@property (nonatomic, strong) NSData *imageData;

@end

@implementation M_EnlageConfDetail

@end


@interface ViewController ()
@property (weak, nonatomic) IBOutlet UITextView *fidsTV;
@property (weak, nonatomic) IBOutlet UITextView *imageInfoTV;
@property (weak, nonatomic) IBOutlet UITextField *createFinishInfoTF;
@property (weak, nonatomic) IBOutlet UIImageView *choiceImageView;

@property (nonatomic, strong) NSMutableArray *enlargeList;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
}
- (IBAction)login:(id)sender {
        [I_Account loginOrRegistWithUserName:@"454140866@qq.com" pwd:@"123456" notReg:NO success:^(M_User * _Nonnull account) {
            [self showMessage:account];
        } failure:^(NSError *error) {
            [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
        }];
}
- (IBAction)getConf:(id)sender {
    [I_Account requestConfOnSuccess:^(NSDictionary * _Nonnull confDic) {
         [self showMessage:confDic];
        NSLog(@"%@",[LqSandBox docPath]);
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
}
- (IBAction)getUserInfo:(id)sender {
    [I_Account getUserInfoOnSuccess:^(M_User * _Nonnull userInfo) {
         [self showMessage:userInfo];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
    
}
- (IBAction)updatePwd:(id)sender {
    [I_Account updatePassword:@"123456" success:^{
         [self showMessage:@"更新密码成功"];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
}
- (IBAction)checkFidStatus:(id)sender {
    NSArray *list = [_fidsTV.text componentsSeparatedByString:@","];
    [I_Enlarge getEnlargeTasksStatus:list success:^(NSMutableArray<M_Enlarge *> * _Nonnull taskList) {
         [self showMessage:taskList];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
}
- (IBAction)retryFid:(id)sender {
    NSArray *list = [_fidsTV.text componentsSeparatedByString:@","];
    [I_Enlarge retryEnlargeTasks:list success:^{
         [self showMessage:@"重试成功"];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
}

- (IBAction)deleteFild:(id)sender {
    NSArray *list = [_fidsTV.text componentsSeparatedByString:@","];
    [I_Enlarge deleteEnlargeTasks:list success:^{
         [self showMessage:@"删除成功"];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
    }];
}

- (IBAction)choiceImage:(id)sender {
           ZLPhotoActionSheet *actionSheet = [[ZLPhotoActionSheet alloc] init];
            actionSheet.configuration.allowSelectGif = NO;
            actionSheet.configuration.allowSelectVideo = NO;
            actionSheet.configuration.allowSelectLivePhoto = NO;
            actionSheet.configuration.allowEditImage = NO;
            actionSheet.configuration.allowTakePhotoInLibrary = NO;
            actionSheet.configuration.allowSelectOriginal = NO;
        actionSheet.configuration.shouldAnialysisAsset = NO;
            actionSheet.configuration.maxSelectCount = 10;
    //        actionSheet.configuration.navBarColor= HEXColor(@"#3AA7FF");
            actionSheet.configuration.navTitleColor = [UIColor whiteColor];
    //        actionSheet.configuration.bottomBtnsNormalTitleColor = HEXColor(@"3AA7FF");
            actionSheet.configuration.bottomBtnsDisableBgColor = RGBA(58, 167, 255, 0.5);
            actionSheet.sender = self;
            
            __weak typeof(self) weakSelf = self;
            [actionSheet setSelectImageBlock:^(NSArray<UIImage *> * _Nullable images, NSArray<PHAsset *> * _Nonnull assets, BOOL isOriginal) {
                weakSelf.enlargeList = [NSMutableArray array];
                for (PHAsset *ass in assets) {
                    [[PHImageManager defaultManager] requestImageDataForAsset:ass options:nil resultHandler:^(NSData * _Nullable imageData, NSString * _Nullable dataUTI, UIImageOrientation orientation, NSDictionary * _Nullable info) {
                        UIImage *image = [UIImage imageWithData:imageData];
                        NSURL *fileUrl = [info safeObjectForKey:@"PHImageFileURLKey"];
                        NSString *name = [fileUrl.absoluteString componentsSeparatedByString:@"/"].lastObject;
                        M_EnlageConfDetail *conf = [[M_EnlageConfDetail alloc] init];
                        conf.files_size = imageData.length;
                        conf.file_width = image.size.width;
                        conf.file_height = image.size.height;
                        conf.file_name = [NSString stringWithFormat:@"ios/%@/%@",[LqToolKit stringFromDate:[NSDate date] formatterString:@"yyyy-MM-dd/hh-mm-ss"],name];
                        conf.x2 = 3;
                        conf.noise =3;
                        conf.style = @"art";
                        [weakSelf.enlargeList addObject:conf];
                        
                        weakSelf.choiceImageView.image = image;
                        weakSelf.imageInfoTV.text = [conf mj_JSONString];
                        
                        conf.imageData = imageData;
                    }];
                }

            }];
            [actionSheet showPhotoLibrary];
}

- (IBAction)createTask:(id)sender {
    M_EnlageConfDetail *detail = _enlargeList.lastObject;
    [OSSManager asyncUploadData:detail.imageData objectKey:detail.file_name progress:^(int64_t bytesSent, int64_t totalByteSent, int64_t totalBytesExpectedToSend) {
        
    } success:^(OSSTask * _Nonnull task) {
        if (task.error) {
             [SVProgressHUD showErrorWithStatus:@"OSS图片上传发送错误"];
        } else {
            NSString *input = [NSString stringWithFormat:@"%@/%@",[OSSManager  getOSSUrl],detail.file_name];
            detail.input = input;
            [I_Enlarge createEnlargeTaskWith:detail success:^(NSString * _Nonnull fid, NSInteger time) {
                [self showMessage:[NSString stringWithFormat:@"fid:%@,预计时间:%ld",fid,(long)time]];
                _createFinishInfoTF.text = [NSString stringWithFormat:@"%@",fid];
            } failure:^(NSError *error) {
                 [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
            }];
        }
    } failure:^(NSError * _Nonnull error) {
        [SVProgressHUD showErrorWithStatus:@"OSS图片上传失败"];
    }];
}

- (IBAction)allUpload:(id)sender {
    for (M_EnlageConfDetail *detail in self.enlargeList) {
        [OSSManager asyncUploadData:detail.imageData objectKey:detail.file_name progress:^(int64_t bytesSent, int64_t totalByteSent, int64_t totalBytesExpectedToSend) {
            
        } success:^(OSSTask * _Nonnull task) {
            if (task.error) {
                 [SVProgressHUD showErrorWithStatus:@"OSS图片上传发送错误"];
            } else {
                NSString *input = [NSString stringWithFormat:@"%@/%@",[OSSManager  getOSSUrl],detail.file_name];
                detail.input = input;
                [I_Enlarge createEnlargeTaskWith:detail success:^(NSString * _Nonnull fid, NSInteger time) {
                    [self showMessage:[NSString stringWithFormat:@"fid:%@,预计时间:%ld",fid,(long)time]];
                } failure:^(NSError *error) {
                     [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
                }];
            }
        } failure:^(NSError * _Nonnull error) {
            [SVProgressHUD showErrorWithStatus:@"OSS图片上传失败"];
        }];
    }
}


- (void)showMessage:(id)obj
{
    NSString *message = [obj mj_JSONString];
        //1.创建UIAlertControler
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"标题" message:message preferredStyle:UIAlertControllerStyleAlert];
        /*
         参数说明：
         Title:弹框的标题
         message:弹框的消息内容
         preferredStyle:弹框样式：UIAlertControllerStyleAlert
         */
        
        //2.添加按钮动作
        //2.1 确认按钮
        UIAlertAction *conform = [UIAlertAction actionWithTitle:@"确认" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            NSLog(@"点击了确认按钮");
        }];
        //2.2 取消按钮
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            NSLog(@"点击了取消按钮");
        }];
//        //2.3 还可以添加文本框 通过 alert.textFields.firstObject 获得该文本框
//        [alert addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
//            textField.placeholder = @"请填写您的反馈信息";
//        }];
     
        //3.将动作按钮 添加到控制器中
        [alert addAction:conform];
        [alert addAction:cancel];
        
        //4.显示弹框
        [self presentViewController:alert animated:YES completion:nil];
}


@end


