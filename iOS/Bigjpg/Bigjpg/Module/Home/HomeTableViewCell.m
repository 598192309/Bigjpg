//
//  HomeTableViewCell.m
//  Bigjpg
//
//  Created by lqq on 2019/12/27.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "HomeTableViewCell.h"
#import "EnlargeConfViewController.h"
#import "I_Enlarge.h"
@interface HomeTableViewCell ()
@property (weak, nonatomic) IBOutlet UIImageView *confImageView;
@property (weak, nonatomic) IBOutlet UIProgressView *progressView;
@property (weak, nonatomic) IBOutlet UILabel *imageDetailLabel;
@property (weak, nonatomic) IBOutlet UIButton *firstBtn;
@property (weak, nonatomic) IBOutlet UIView *statusBackView;
@property (weak, nonatomic) IBOutlet UILabel *statusLabel;
@end

@implementation HomeTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    _confImageView.layer.cornerRadius = 4;
    _confImageView.layer.masksToBounds = YES;
    _statusBackView.layer.cornerRadius = 4;
    _statusBackView.layer.cornerRadius = 4;
    _firstBtn.layer.cornerRadius = 4;
    _firstBtn.layer.masksToBounds  = YES;
}
- (IBAction)firstBtnEvent:(id)sender {
    
    switch (self.upload.uploadStep) {
           case EnlargeUploadStepInitialize:
           {//开始
               if ([self.delegate respondsToSelector:@selector(uploadEvent:enlarge:)]) {
                   [self.delegate uploadEvent:self enlarge:self.upload];
               }
           }
               break;
        case EnlargeUploadStepDataUploadFail:
        case EnlargeUploadStepEnlargeError:
           {//重试
               if (_upload.fid.length > 0) {//bigjpg重试
                   _upload.uploadStep = EnlargeUploadStepEnlargeingProcess;
                   __weak __typeof(self) weakSelf = self;
                   _upload.createTime = [NSDate date];
                   [I_Enlarge retryEnlargeTasks:@[_upload.fid] success:^{
                       
                   } failure:^(NSError *error) {
                       weakSelf.upload.uploadStep = EnlargeUploadStepEnlargeError;
                       [LSVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
                   }];
               } else {//重新上传
                   NSDictionary *dic = @{@"conf":self.upload.conf,
                                         @"enlargeBatch":@(NO),
                                         @"uploads":@[self.upload]
                   };
                   [[NSNotificationCenter defaultCenter] postNotificationName:kEnlargeConfigarationFinishNoti object:dic];
               }
           }
               break;
           case EnlargeUploadStepEnlargeSuccess:
           {//下载
               [I_Enlarge downloadPictureWithUrls:@[_upload.output] isAutoDown:NO];
           }
               break;
               
           default:
               break;
       }
    
}


- (void)setUpload:(M_EnlargeUpload *)upload
{
    _upload = upload;
    _confImageView.image = [UIImage imageWithData:upload.imageData];
    self.imageDetailLabel.text = upload.imageSizeStr;
    switch (upload.uploadStep) {
        case EnlargeUploadStepInitialize:
        {//初始化
            //statusView
            self.statusBackView.hidden = YES;
            //进度条
            self.progressView.progress = 0;
            //按钮
            _firstBtn.hidden = NO;
            [_firstBtn setTitle:LanguageStrings(@"begin") forState:UIControlStateNormal];
            [_firstBtn setBackgroundColor:TP.greenColor];
        }
            break;
        case EnlargeUploadStepOverSize:
        {//超出限制
            //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"over");
            self.statusLabel.textColor = TP.redColor;
            //进度条
            self.progressView.progress = 0;
            
            //按钮
            _firstBtn.hidden = YES;
        }
            break;
        case EnlargeUploadStepDataUploading:
        {//数据上传中
            //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"process");
            self.statusLabel.textColor = TP.whiteColor;
            //进度条
            self.progressView.progress = 0.03;
            //按钮
            _firstBtn.hidden = YES;
        }
            break;
        case EnlargeUploadStepDataUploadFail:
        {//数据上传失败
             //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"fail");
            self.statusLabel.textColor = TP.redColor;
            //进度条
            self.progressView.progress = 0;
            //按钮
            _firstBtn.hidden = NO;
            [_firstBtn setTitle:LanguageStrings(@"retry") forState:UIControlStateNormal];
            [_firstBtn setBackgroundColor:TP.yellowColor];
        }
            break;
        case EnlargeUploadStepEnlargeingNew:
        case EnlargeUploadStepEnlargeingProcess:
        {//放大中
            //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"process");
            self.statusLabel.textColor = TP.whiteColor;
            
            //进度条
            NSTimeInterval nowInte = [[NSDate date] timeIntervalSince1970];
             NSTimeInterval createInte = [upload.createTime timeIntervalSince1970];
             CGFloat seconds = upload.conf.time ;
            
            CGFloat progress = (nowInte - createInte) / seconds;
            NSLog(@"%f,%f,%f",(nowInte - createInte),seconds, progress);
            if (progress < 0.03) {
                progress = 0.03;
            }
            
            if (progress > 0.9) {
                progress = 0.9;
            }
            _progressView.progress = progress;
            
            //按钮
            _firstBtn.hidden = YES;
        }
            break;
        case EnlargeUploadStepEnlargeSuccess:
        {//放大成功
            //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"succ");
            self.statusLabel.textColor = TP.whiteColor;
            //进度条
            self.progressView.progress = 1;
            //按钮
            _firstBtn.hidden = NO;
            [_firstBtn setTitle:LanguageStrings(@"download") forState:UIControlStateNormal];
            [_firstBtn setBackgroundColor:TP.greenColor];
        }
            break;
        case EnlargeUploadStepEnlargeError:
        {//放大失败
            //statusView
            self.statusBackView.hidden = NO;
            self.statusLabel.text = LanguageStrings(@"fail");
            self.statusLabel.textColor = TP.redColor;
            //进度条
            self.progressView.progress = 0;
            //按钮
            _firstBtn.hidden = NO;
            [_firstBtn setTitle:LanguageStrings(@"retry") forState:UIControlStateNormal];
            [_firstBtn setBackgroundColor:TP.yellowColor];
        }
            break;
            
        default:
            break;
    }
    
    self.backgroundColor = BackGroundColor;
    self.imageDetailLabel.textColor = TitleBlackColor;
}

@end
