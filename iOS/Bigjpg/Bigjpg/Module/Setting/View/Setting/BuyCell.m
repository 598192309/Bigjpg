//
//  BuyCell.m
//  Bigjpg
//
//  Created by lqq on 2020/1/14.
//  Copyright © 2020 lqq. All rights reserved.
//

#import "BuyCell.h"
@interface BuyCell()
@property (nonatomic,strong) UIView * cellBackgroundView;
@property (nonatomic,strong) UIView * cellBackgroundViewContentV;
@property (strong, nonatomic)  UILabel *titleLabel;
@property (strong, nonatomic)  UILabel *timeLabel;
@property (strong, nonatomic)  UILabel *countLabel;
@property (strong, nonatomic)  UILabel *speedLabel;

@property (strong, nonatomic)  UILabel *serviceLabel;
@property (strong, nonatomic)  UILabel *sizeLabel;
@property (strong, nonatomic)  UILabel *scaleLabel;
@property (strong, nonatomic)  UILabel *offlineLabel;
@property (strong, nonatomic)  UILabel *sameTimeLabel;
@property (strong, nonatomic)  UILabel *enlargeLabel;

@property (strong, nonatomic)  UIButton *buyBtn;


@end
@implementation BuyCell

#pragma mark - 生命周期
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        [self configUI];
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.backgroundColor = BackGrayColor;

    }
    return self;
}


- (void)configUI{
    [self.contentView addSubview:self.cellBackgroundView];
    __weak __typeof(self) weakSelf = self;
    [self.cellBackgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf.contentView);
    }];

}
#pragma mark - ui
- (void)configUIWithArr:(NSArray *)arr color:(UIColor *)color{
    self.titleLabel.text = [NSString stringWithFormat:@"  %@",[arr safeObjectAtIndex:0]];
    NSString *time = [arr safeObjectAtIndex:1];
    NSRange start = [time rangeOfString:@"\">"];
    NSRange end = [time rangeOfString:@"</"];
    if (start.length > 0 && end.length > 0) {
        time = [time substringWithRange:NSMakeRange(start.location + start.length,end.location - (start.location + start.length))];
    }
    self.timeLabel.text = time;
    
    NSString *count = [arr safeObjectAtIndex:2];
    NSRange start2 = [count rangeOfString:@"\">"];
    NSRange end2 = [count rangeOfString:@"</"];
    if (start2.length > 0 && end2.length > 0) {
        count = [count substringWithRange:NSMakeRange(start2.location + start2.length,end2.location - (start2.location + start2.length))];
    }
    __weak __typeof(self) weakSelf = self;
    self.countLabel.text = count;
    self.speedLabel.text = [arr safeObjectAtIndex:3];
    self.serviceLabel.text = [arr safeObjectAtIndex:4];
    self.sizeLabel.text = [arr safeObjectAtIndex:5];
    self.scaleLabel.text = [arr safeObjectAtIndex:6];
    self.offlineLabel.text = [arr safeObjectAtIndex:7];
    self.sameTimeLabel.text = [arr safeObjectAtIndex:8];
    self.enlargeLabel.text = [arr safeObjectAtIndex:9];
    if ([color isEqual:TitleGrayColor]) {
        self.titleLabel.textColor = RGB(50,50,50);
        self.titleLabel.backgroundColor = TitleGrayColor;
        self.timeLabel.textColor =TitleBlackColor ;
        self.countLabel.textColor =TitleBlackColor ;
        [self.buyBtn mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(0);
            make.top.mas_equalTo(weakSelf.enlargeLabel.mas_bottom).offset(-Adaptor_Value(10));

        }];
    }else{
        self.titleLabel.textColor =[UIColor whiteColor];
        self.titleLabel.backgroundColor = color;

        self.timeLabel.textColor =color ;
        self.countLabel.textColor =color ;
        [self.buyBtn mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(Adaptor_Value(50));
            make.top.mas_equalTo(weakSelf.enlargeLabel.mas_bottom).offset(Adaptor_Value(10));

        }];
    }
    [self.buyBtn setTitle:LanguageStrings(@"upgrade") forState:UIControlStateNormal];
    [self.buyBtn setBackgroundColor:color];
}

#pragma mark - act
- (void)buyBtnClick:(UIButton *)sender{
    if (self.buyCellConfirmBtnClickBlock) {
        self.buyCellConfirmBtnClickBlock(_productId,sender);
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
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.cellBackgroundView).offset(-Adaptor_Value(10));
            make.top.mas_equalTo(Adaptor_Value(5));
            make.bottom.mas_equalTo(weakSelf.cellBackgroundView).offset(-Adaptor_Value(5));
        }];
        contentV.backgroundColor = RI.isNight ? RGB(20, 20, 20) : BackGrayColor;
        _cellBackgroundViewContentV = contentV;
        ViewBorderRadius(contentV, 4, kOnePX, TitleGrayColor);
        
        _titleLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedBoldFontSize(17) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_titleLabel];
        [_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.top.mas_equalTo(contentV);
            make.height.mas_equalTo(Adaptor_Value(40));
        }];
        
        _timeLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_timeLabel];
        [_timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.top.mas_equalTo(weakSelf.titleLabel.mas_bottom).offset(Adaptor_Value(10));
            make.right.mas_equalTo(contentV);
        }];
        
        _countLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_countLabel];
        [_countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.timeLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _speedLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_speedLabel];
        [_speedLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.countLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
       
        _serviceLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_serviceLabel];
        [_serviceLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.speedLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _sizeLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_sizeLabel];
        [_sizeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.serviceLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _scaleLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_scaleLabel];
        [_scaleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.sizeLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _offlineLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_offlineLabel];
        [_offlineLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.scaleLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _sameTimeLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_sameTimeLabel];
        [_sameTimeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.offlineLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
        
        _enlargeLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(16) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_enlargeLabel];
        [_enlargeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.sameTimeLabel.mas_bottom).offset(Adaptor_Value(5));
            make.right.mas_equalTo(weakSelf.timeLabel);
        }];
   
        _buyBtn = [[UIButton alloc] init];
        _buyBtn.titleLabel.font = AdaptedFontSize(17);
        [_buyBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_buyBtn addTarget:self action:@selector(buyBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [contentV addSubview:_buyBtn];
        [_buyBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.timeLabel);
            make.top.mas_equalTo(weakSelf.enlargeLabel.mas_bottom).offset(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.height.mas_equalTo(Adaptor_Value(50));
            make.bottom.mas_equalTo(contentV).offset(-Adaptor_Value(10));
        }];
        ViewRadius(_buyBtn, 5);

    }
    return _cellBackgroundView;
}



@end
