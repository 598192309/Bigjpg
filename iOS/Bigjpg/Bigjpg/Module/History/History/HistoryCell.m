//
//  HistoryCell.m
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "HistoryCell.h"
#import <SDWebImage/SDWebImage.h>
#import "I_Enlarge.h"
@interface HistoryCell()
@property (nonatomic,strong) UIView * cellBackgroundView;
@property (nonatomic,strong)   UIImageView *iconImageV;
@property (nonatomic,strong)   UIView *iconImageVCoverView;

@property (strong, nonatomic)  UILabel *imageTipLable;
@property (strong, nonatomic)  UILabel *titleLabel;
@property (nonatomic,strong)   UIButton *retryOrDownloadBtn;

@property (nonatomic,strong)   UIButton *chooseBtn;

@property (nonatomic, strong) M_EnlargeHistory *item;
@end
@implementation HistoryCell

#pragma mark - 生命周期
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        [self configUI];
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.contentView.backgroundColor = BackGroundColor;
        
    }
    return self;
}


- (void)configUI{
    [self.contentView addSubview:self.cellBackgroundView];
    [self.cellBackgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.mas_equalTo(self.contentView);
    }];
    
}


- (void)configUIWithItem:(M_EnlargeHistory *)item downAll:(BOOL)downAll backColor:(UIColor *)backColor{
    
    _item = item;
    
    [_retryOrDownloadBtn setTitle:LanguageStrings(@"retry") forState:UIControlStateNormal];
    if ([item.status isEqualToString:@"success"]) {
        _retryOrDownloadBtn.hidden = NO;
        [_retryOrDownloadBtn setTitle:LanguageStrings(@"download") forState:UIControlStateNormal];
        _retryOrDownloadBtn.backgroundColor = LihgtGreenColor;
        self.chooseBtn.hidden = !downAll;
        self.retryOrDownloadBtn.hidden = downAll;
        self.chooseBtn.selected = item.customSlected;
        //设置图片
        NSString *smallImagesStr = [NSString stringWithFormat:@"%@?x-oss-process=image/resize,m_fill,w_%d,h_%d",item.output,100,100];
        [_iconImageV sd_setImageWithURL:[NSURL URLWithString:smallImagesStr]];
    }else if ([item.status isEqualToString:@"error"]) {
        _retryOrDownloadBtn.hidden = NO;
        [_retryOrDownloadBtn setTitle:LanguageStrings(@"retry") forState:UIControlStateNormal];
        _retryOrDownloadBtn.backgroundColor = YellowBackColor;
        self.chooseBtn.hidden = !downAll;
        self.retryOrDownloadBtn.hidden = downAll;
        self.chooseBtn.selected = item.customSlected;
        //设置图片
        NSString *smallImagesStr = [NSString stringWithFormat:@"%@?x-oss-process=image/resize,m_fill,w_%d,h_%d",item.conf.input,100,100];
        [_iconImageV sd_setImageWithURL:[NSURL URLWithString:smallImagesStr]];
    } else {
        _retryOrDownloadBtn.hidden = YES;
        self.chooseBtn.hidden = YES;
        //设置图片
        NSString *smallImagesStr = [NSString stringWithFormat:@"%@?x-oss-process=image/resize,m_fill,w_%d,h_%d",item.conf.input,100,100];
        [_iconImageV sd_setImageWithURL:[NSURL URLWithString:smallImagesStr]];

    }

    NSString *noiseStr ;
    if (item.conf.noise == -1) {
        noiseStr = LanguageStrings(@"none");
    }else if (item.conf.noise == 0) {
        noiseStr = LanguageStrings(@"low");
    }else if (item.conf.noise == 1) {
        noiseStr = LanguageStrings(@"mid");
    }else if (item.conf.noise == 2) {
        noiseStr = LanguageStrings(@"high");
    }else if (item.conf.noise == 3) {
        noiseStr = LanguageStrings(@"highest");
    }
    
    NSString *x2Str ;
     if (item.conf.x2 == 1) {
        x2Str = @"2x";
    }else if (item.conf.x2 == 2) {
        x2Str = @"4x";
    }else if (item.conf.x2 == 3) {
        x2Str = @"8x";
    }else if (item.conf.x2 == 4) {
        x2Str = @"16x";
    }
    NSString *typeStr ;
     if ([item.conf.style isEqualToString:@"art"]) {
        typeStr = LanguageStrings(@"carton");
    }else  {
        typeStr = LanguageStrings(@"photo");
    }
    NSString *sizeStr = @"";
    if (item.conf.files_size < 1024) {
        sizeStr = [NSString stringWithFormat:@"%ldbytes",(long)item.conf.files_size];
    } else if (item.conf.files_size/ 1024.0 < 1024) {
        sizeStr = [NSString stringWithFormat:@"%.1fkb",item.conf.files_size/1024.0];
    } else {
        sizeStr = [NSString stringWithFormat:@"%.1fM",item.conf.files_size/1024.0/1024.0];
    }
    self.titleLabel.text = [NSString stringWithFormat:@"%@,%@,%@,\n%@:%@",x2Str,sizeStr,typeStr,LanguageStrings(@"noise"),noiseStr];
    if ([item.status isEqualToString:@"new"] || [item.status isEqualToString:@"process"]) {
        self.imageTipLable.text = LanguageStrings(@"process");
    } else if ([item.status isEqualToString:@"error"]) {
        self.imageTipLable.text = LanguageStrings(@"fail");
    }
    self.imageTipLable.hidden = [item.status isEqualToString:@"success"];
    self.iconImageVCoverView.hidden = [item.status isEqualToString:@"success"];
    
    _iconImageV.backgroundColor = RI.isNight ? RGB(38,38,38) : LineGrayColor;
    _cellBackgroundView.backgroundColor = backColor;

}

#pragma mark - act
- (void)downloadOrRetryBtnClick:(UIButton *)sender{
    if ([_item.status isEqualToString:@"success"]) {
        [I_Enlarge downloadPictureWithUrls:@[_item.output] isAutoDown:NO];
    } else if([_item.status isEqualToString:@"error"]) {
        [I_Enlarge retryEnlargeTasks:@[_item.fid] success:^{
            [LSVProgressHUD showSuccessWithStatus:LanguageStrings(@"succ")];
            POST_NOTIFY(kRetrySuccessNoti, nil);
        } failure:^(NSError *error) {
            [LSVProgressHUD showError:error];
        }];
    }
}

#pragma mark - lazy
- (UIView *)cellBackgroundView{
    if (!_cellBackgroundView) {
        _cellBackgroundView = [UIView new];
        _cellBackgroundView.backgroundColor = BackGroundColor;
        UIView *contentV = [UIView new];
        __weak __typeof(self) weakSelf = self;
        [_cellBackgroundView addSubview:contentV];
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.cellBackgroundView);
            make.height.mas_equalTo(110);
        }];
        contentV.backgroundColor = [UIColor clearColor];
        
        _iconImageV = [[UIImageView alloc] init];
        [contentV addSubview:_iconImageV];
        [_iconImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.width.mas_equalTo(78);
            make.left.mas_equalTo(15);
            make.centerY.mas_equalTo(contentV);
        }];
        ViewRadius(_iconImageV,4);
        _iconImageV.backgroundColor = RI.isNight ? RGB(38,38,38) : LineGrayColor;
        
        _iconImageVCoverView = [UIView new];
        _iconImageVCoverView.backgroundColor = [UIColor colorWithWhite:0.5 alpha:0.5];
        ViewRadius(_iconImageVCoverView, 4);
        [contentV addSubview:_iconImageVCoverView];
        [_iconImageVCoverView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(weakSelf.iconImageV);
        }];
        
        _imageTipLable = [UILabel lableWithText:lqLocalized(@"",nil) textColor:RedColor fontSize:AdaptedFontSize(13) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
         [_iconImageVCoverView addSubview:_imageTipLable];
         [_imageTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
             make.edges.mas_equalTo(weakSelf.iconImageV);
         }];
        
      
        _retryOrDownloadBtn = [[UIButton alloc] init];
        [_retryOrDownloadBtn setTitle:@"" forState:UIControlStateNormal];
        [_retryOrDownloadBtn addTarget:self action:@selector(downloadOrRetryBtnClick:) forControlEvents:UIControlEventTouchDown];
        [contentV addSubview:_retryOrDownloadBtn];
        _retryOrDownloadBtn.titleLabel.font = [UIFont systemFontOfSize:14 weight:UIFontWeightRegular];
        _retryOrDownloadBtn.contentEdgeInsets = UIEdgeInsetsMake(0, 10, 0, 10);
//        _retryOrDownloadBtn.titleLabel.adjustsFontSizeToFitWidth = YES;
        [_retryOrDownloadBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(Adaptor_Value(35));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(15));
            make.centerY.mas_equalTo(contentV.mas_centerY);
            make.width.mas_greaterThanOrEqualTo(70);
            make.width.mas_lessThanOrEqualTo(90);
        }];
        _retryOrDownloadBtn.backgroundColor = YellowBackColor;
        ViewRadius(_retryOrDownloadBtn, 4);
        
        
        _chooseBtn = [[UIButton alloc] init];
        [_chooseBtn setImage:[[UIImage imageNamed:@"ic_uncheck"] qmui_imageWithTintColor:DeepGreenColor] forState:UIControlStateNormal];
        [_chooseBtn setImage:[UIImage imageNamed:@"ic_check"] forState:UIControlStateSelected];
        [_chooseBtn addTarget:self action:@selector(downloadOrRetryBtnClick:) forControlEvents:UIControlEventTouchDown];
        [contentV addSubview:_chooseBtn];
        [_chooseBtn mas_makeConstraints:^(MASConstraintMaker *make) {

            make.height.width.mas_equalTo(Adaptor_Value(40));
            make.center.mas_equalTo(weakSelf.retryOrDownloadBtn);
        }];
        _chooseBtn.userInteractionEnabled = NO;
   
        _titleLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleGrayColor fontSize:AdaptedFontSize(13) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_titleLabel];
       
//        _titleLabel.backgroundColor = [UIColor redColor];
        [_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.mas_equalTo(contentV);
            make.left.mas_greaterThanOrEqualTo(weakSelf.iconImageV.mas_right).offset(Adaptor_Value(10));
            make.right.equalTo(weakSelf.retryOrDownloadBtn.mas_left).offset(-10);
        }];
        [_titleLabel setContentCompressionResistancePriority:UILayoutPriorityDefaultLow forAxis:UILayoutConstraintAxisHorizontal];
        UIView *lineView = [UIView new];
        lineView.backgroundColor = LineGrayColor;
        [contentV addSubview:lineView];
        [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.bottom.mas_equalTo(contentV);
            make.height.mas_equalTo(kOnePX);
        }];
        
    }
    return _cellBackgroundView;
}

@end
