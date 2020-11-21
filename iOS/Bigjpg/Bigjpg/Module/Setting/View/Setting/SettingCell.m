//
//  SettingCell.m
//  Bigjpg
//
//  Created by rabi on 2019/12/24.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "SettingCell.h"

@interface SettingCell()
@property (nonatomic,strong) UIView * cellBackgroundView;
@property (strong, nonatomic)  UILabel *titleLabel;
@property (nonatomic,strong) UIView * cellBackgroundViewContentV;

@end
@implementation SettingCell

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
- (void)refreshUIWithTitle:(NSString *)title{
    self.titleLabel.text = title;
    self.titleLabel.textColor = TitleBlackColor;
    _cellBackgroundView.backgroundColor = BackGroundColor;
    _cellBackgroundViewContentV.backgroundColor = RI.isNight ? RGB(20, 20, 20) : BackGrayColor;
}

#pragma mark - act

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
            make.height.mas_equalTo(Adaptor_Value(50));
            make.top.mas_equalTo(Adaptor_Value(5));
            make.bottom.mas_equalTo(weakSelf.cellBackgroundView).offset(-Adaptor_Value(5));
        }];
        contentV.backgroundColor = RI.isNight ? RGB(20, 20, 20) : BackGrayColor;
        _cellBackgroundViewContentV = contentV;
        ViewBorderRadius(contentV, 4, kOnePX, TitleGrayColor);
        
        _titleLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(17) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_titleLabel];
        [_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.mas_equalTo(contentV);
        }];
   

    }
    return _cellBackgroundView;
}



@end
