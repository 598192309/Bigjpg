//
//  HistoryCustomView.m
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "HistoryCustomView.h"

@interface HistoryCustomView()
//头部view
@property (nonatomic,strong)UIView *header;
@property (nonatomic,strong)UIButton *cancleBtn;
@property (nonatomic,strong)UIButton *confrimBtn;

@property (nonatomic,strong)UILabel *wenjianTipLable;
@property (nonatomic,strong)UILabel *canshuTipLable;
@property (nonatomic,strong)UILabel *dowloadTipLable;



@end
@implementation HistoryCustomView


#pragma mark - 生命周期
#pragma mark - 生命周期
-(instancetype)init{
    if (self = [super init]) {
        [self configUI];
        
    }
    return self;
}
#pragma mark - ui
-(void)configUI{
    [self addSubview:self.header];
    __weak __typeof(self) weakSelf = self;
    [self.header mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf);
    }];
}


#pragma mark - 刷新ui

- (void)reset{
    self.downloadBtn.hidden = NO;
    self.cancleBtn.hidden = YES;
    self.confrimBtn.hidden = YES;
}
#pragma mark - act
- (void)downloadBtnClick:(UIButton *)sender{
    if (self.historyCustomViewDownAllBtnClickBlock) {
        self.historyCustomViewDownAllBtnClickBlock(@{},sender);
    }
    self.downloadBtn.hidden = YES;
    self.cancleBtn.hidden = NO;
    self.confrimBtn.hidden = NO;
}
- (void)cancleBtnClick:(UIButton *)sender{
    if (self.historyCustomViewCancleBtnClickBlock) {
        self.historyCustomViewCancleBtnClickBlock(@{},sender);
    }
    self.downloadBtn.hidden = NO;
    self.cancleBtn.hidden = YES;
    self.confrimBtn.hidden = YES;
}
- (void)confirmBtnClick:(UIButton *)sender{
    if (self.historyCustomViewConfirmBtnClickBlock) {
        self.historyCustomViewConfirmBtnClickBlock(@{},sender);
    }
}


#pragma mark - lazy
-(UIView *)header{
    if (!_header) {
        _header = [UIView new];
        UIView *contentV = [UIView new];
        contentV.backgroundColor = BackGroundColor;
        [_header addSubview:contentV];
        __weak __typeof(self) weakSelf = self;
        
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.header);
        }];
        
       
        
        _downloadBtn = [[UIButton alloc] init];
        [_downloadBtn setTitle:LanguageStrings(@"batch_download") forState:UIControlStateNormal];
        [_downloadBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_downloadBtn addTarget:self action:@selector(downloadBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [contentV addSubview:_downloadBtn];
        _downloadBtn.titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightMedium];
        _downloadBtn.contentEdgeInsets = UIEdgeInsetsMake(0, 10, 0, 10);

        [_downloadBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.mas_equalTo(contentV);
//            make.top.mas_equalTo(Adaptor_Value(60));
//            make.height.equalTo(@(40));
            make.top.mas_equalTo(TopAdaptor_Value(30));
            make.height.mas_equalTo(0);
            make.width.mas_greaterThanOrEqualTo(100);
        }];
        _downloadBtn.titleLabel.font = AdaptedFontSize(15);
        _downloadBtn.backgroundColor = LihgtGreenColor;
        ViewRadius(_downloadBtn,4);

        
        _cancleBtn = [[UIButton alloc] init];
        [_cancleBtn setTitle:LanguageStrings(@"cancel") forState:UIControlStateNormal];
        [_cancleBtn addTarget:self action:@selector(cancleBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [_cancleBtn setTitleColor:TitleBlackColor forState:UIControlStateNormal];
        [contentV addSubview:_cancleBtn];
        _cancleBtn.titleLabel.font = _downloadBtn.titleLabel.font;
        _cancleBtn.contentEdgeInsets = _downloadBtn.contentEdgeInsets;
        [_cancleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.downloadBtn);
            make.left.mas_equalTo(contentV.mas_centerX).offset(Adaptor_Value(20));
            make.height.equalTo(weakSelf.downloadBtn);
            make.width.mas_greaterThanOrEqualTo(100);
        }];
        
        ViewBorderRadius(_cancleBtn, 4, kOnePX, LineGrayColor);
        _cancleBtn.backgroundColor = RI.isNight ? RGB(20,20,20) :RGB(247,247,247);
        _cancleBtn.hidden = YES;
        
        _confrimBtn = [[UIButton alloc] init];
        [_confrimBtn setTitle:LanguageStrings(@"ok") forState:UIControlStateNormal];
        [_confrimBtn addTarget:self action:@selector(confirmBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [_confrimBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [contentV addSubview:_confrimBtn];
        _confrimBtn.titleLabel.font = _downloadBtn.titleLabel.font;
        _confrimBtn.contentEdgeInsets = _downloadBtn.contentEdgeInsets;

        [_confrimBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.downloadBtn);
            make.right.mas_equalTo(contentV.mas_centerX).offset(-Adaptor_Value(20));
            make.height.equalTo(weakSelf.downloadBtn);
            make.width.mas_greaterThanOrEqualTo(100);
        }];

        _confrimBtn.backgroundColor = LihgtGreenColor;
        ViewRadius(_confrimBtn,4);
        _confrimBtn.hidden = YES;
      
        NSArray *arr = [ConfManager.shared contentWith:@"log_head"];
        _wenjianTipLable = [UILabel lableWithText:[arr safeObjectAtIndex:0] textColor:TitleBlackColor fontSize:[UIFont systemFontOfSize:16 weight:UIFontWeightMedium] lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_wenjianTipLable];
        [_wenjianTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.downloadBtn.mas_bottom).offset(Adaptor_Value(20));
            make.centerX.mas_equalTo(contentV.mas_left).offset(Adaptor_Value(55));
            
        }];

        _canshuTipLable = [UILabel lableWithText:[arr safeObjectAtIndex:1] textColor:TitleBlackColor fontSize:_wenjianTipLable.font lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_canshuTipLable];
        [_canshuTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.mas_equalTo(contentV);
            make.centerY.mas_equalTo(weakSelf.wenjianTipLable);
        }];
        

        _dowloadTipLable = [UILabel lableWithText:[arr safeObjectAtIndex:3] textColor:TitleBlackColor fontSize:_wenjianTipLable.font lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_dowloadTipLable];
        [_dowloadTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(15));
            make.centerY.mas_equalTo(weakSelf.wenjianTipLable);
            make.width.mas_equalTo(Adaptor_Value(100));
            make.bottom.mas_equalTo(contentV).offset(-Adaptor_Value(10));
        }];
    }
    return _header;
}

@end
