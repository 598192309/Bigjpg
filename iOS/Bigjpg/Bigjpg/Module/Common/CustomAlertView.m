//
//  CustomAlertView.m
//  RabiBird
//
//  Created by 拉比鸟 on 17/1/4.
//  Copyright © 2017年 Lq. All rights reserved.
//

#import "CustomAlertView.h"
@interface CustomAlertView()
@property (strong, nonatomic)  UIView *alertView;
@property (strong, nonatomic)  UILabel *titlelabel;
@property (strong, nonatomic)  UILabel *subLable;
@property (strong, nonatomic)  UIButton *removeBtn;

@property (strong, nonatomic)  UIButton *firstBtn;
@property (strong, nonatomic)  UIButton *secBtn;
@property (strong, nonatomic)  UIButton *singleBtn;
@property (nonatomic, strong)UIView *customCoverView;
@property (strong, nonatomic)  UIView *contentView;
@property (nonatomic,strong)UIView *spreteView;
@end


@implementation CustomAlertView
- (void)firstBtnClick:(UIButton *)sender {
    if (self.CustomAlertViewBlock) {
        self.CustomAlertViewBlock(1,[sender titleForState:UIControlStateNormal]);
    }
}
- (void)secBtnClick:(UIButton *)sender {
    if (self.CustomAlertViewBlock) {
        self.CustomAlertViewBlock(2,[sender titleForState:UIControlStateNormal]);
    }
}
- (void)singleBtnClick:(UIButton *)sender {
    if (self.CustomAlertViewBlock) {
        self.CustomAlertViewBlock(3,[sender titleForState:UIControlStateNormal]);
    }
}
- (void)removeBtnClick:(UIButton *)sender{
    if (self.CustomAlertViewBlock) {
        self.CustomAlertViewBlock(4,nil);
    }

}

#pragma  mark - 拖拽
#pragma  mark - 类方法
-(instancetype)init{
    if (self = [super init]) {
        [self configUI];
        
    }
    return self;
}


#pragma  mark - smzq

- (void)configUI{

    [self addSubview:self.customCoverView];
    __weak __typeof(self) weakSelf = self;
    [self.customCoverView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf);
    }];
    
    [self addSubview:self.alertView];
    [self.alertView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.mas_equalTo(weakSelf);
    }];


    ViewRadius(self.alertView, Adaptor_Value(20));
}


-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor{
    self.titlelabel.attributedText = attributeTitle;
    self.titlelabel.textColor = titleColor;
    self.titlelabel.font = titleFont;
    self.titlelabel.textAlignment = titleAliment;
    self.subLable.attributedText = attributeSubTitle;
    self.subLable.textColor = subTitleColor;
    self.subLable.font = subTitleFont;
    self.subLable.textAlignment = subTitleAliment;
    [self.firstBtn setTitle:firstBtnTitle forState:UIControlStateNormal];
    [self.firstBtn setTitleColor:firstBtnTitleColor forState:UIControlStateNormal];
    [self.secBtn setTitle:secBtnTitle forState:UIControlStateNormal];
    [self.secBtn setTitleColor:secBtnTitleColor forState:UIControlStateNormal];
    
    [self.singleBtn setTitle:singleBtnTitle forState:UIControlStateNormal];
    [self.singleBtn setTitleColor:singleBtnTitleColor forState:UIControlStateNormal];
    
    
    self.singleBtn.hidden = hidden;
    
    //文本混合时 有中文数字 这样设置 不然排列会有问题
    self.titlelabel.lineBreakMode = NSLineBreakByCharWrapping;
    self.subLable.lineBreakMode = NSLineBreakByCharWrapping;

    
}

-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor removeBtnHidden:(BOOL)removeBtnHidden{
    self.titlelabel.textColor = titleColor;
    self.titlelabel.font = titleFont;
    self.titlelabel.textAlignment = titleAliment;
    self.titlelabel.attributedText = attributeTitle;
    self.subLable.textColor = subTitleColor;
    self.subLable.font = subTitleFont;
    self.subLable.attributedText = attributeSubTitle;
    self.subLable.textAlignment = subTitleAliment;
    [self.firstBtn setTitle:firstBtnTitle forState:UIControlStateNormal];
    [self.firstBtn setTitleColor:firstBtnTitleColor forState:UIControlStateNormal];
    [self.secBtn setTitle:secBtnTitle forState:UIControlStateNormal];
    [self.secBtn setTitleColor:secBtnTitleColor forState:UIControlStateNormal];
    
    [self.singleBtn setTitle:singleBtnTitle forState:UIControlStateNormal];
    [self.singleBtn setTitleColor:singleBtnTitleColor forState:UIControlStateNormal];
    
    
    self.singleBtn.hidden = hidden;
    self.spreteView.hidden = ! self.singleBtn.hidden;

    
    //文本混合时 有中文数字 这样设置 不然排列会有问题
    self.titlelabel.lineBreakMode = NSLineBreakByCharWrapping;
    self.subLable.lineBreakMode = NSLineBreakByCharWrapping;
    
    self.removeBtn.hidden = removeBtnHidden;
    
    
}
-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor removeBtnHidden:(BOOL)removeBtnHidden autoHeight:(BOOL)autoHeight{
    self.titlelabel.attributedText = attributeTitle;
    self.titlelabel.textColor = titleColor;
    self.titlelabel.font = titleFont;
    self.titlelabel.textAlignment = titleAliment;
    self.subLable.attributedText = attributeSubTitle;
    self.subLable.textColor = subTitleColor;
    self.subLable.font = subTitleFont;
    self.subLable.textAlignment = subTitleAliment;
    [self.firstBtn setTitle:firstBtnTitle forState:UIControlStateNormal];
    [self.firstBtn setTitleColor:firstBtnTitleColor forState:UIControlStateNormal];
    [self.secBtn setTitle:secBtnTitle forState:UIControlStateNormal];
    [self.secBtn setTitleColor:secBtnTitleColor forState:UIControlStateNormal];
    
    [self.singleBtn setTitle:singleBtnTitle forState:UIControlStateNormal];
    [self.singleBtn setTitleColor:singleBtnTitleColor forState:UIControlStateNormal];
    
    
    self.singleBtn.hidden = hidden;
    self.spreteView.hidden = ! self.singleBtn.hidden;

    
    //文本混合时 有中文数字 这样设置 不然排列会有问题
    self.titlelabel.lineBreakMode = NSLineBreakByCharWrapping;
    self.subLable.lineBreakMode = NSLineBreakByCharWrapping;
    
    self.removeBtn.hidden = removeBtnHidden;
    
    __weak __typeof(self) weakSelf = self;
    if (autoHeight) {
        [_contentView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.alertView);
            make.width.mas_greaterThanOrEqualTo(Adaptor_Value(225));
        }];

    }else{
        [_contentView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.alertView);
            make.width.mas_greaterThanOrEqualTo(Adaptor_Value(225));
            make.height.mas_greaterThanOrEqualTo(Adaptor_Value(120));
        }];

    }
}
#pragma mark - lazy
- (UIView *)alertView{
    if (!_alertView) {
        _alertView = [UIView new];
        UIView *contentV = [UIView new];
        contentV.backgroundColor = TabbarGrayColor;
        [_alertView addSubview:contentV];
        __weak __typeof(self) weakSelf = self;
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.alertView);
            make.width.mas_greaterThanOrEqualTo(Adaptor_Value(225));
            make.width.mas_lessThanOrEqualTo(Adaptor_Value(300));
            make.height.mas_greaterThanOrEqualTo(Adaptor_Value(120));
        }];
        _contentView = contentV;
        _titlelabel = [UILabel lableWithText:@"" textColor:[UIColor lq_colorWithHexString:@"ffffff"] fontSize:AdaptedFontSize(15) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_titlelabel];
        [_titlelabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(20));
            make.top.mas_equalTo(Adaptor_Value(15));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));

        }];
        
        _removeBtn = [[UIButton alloc] init];
        [_removeBtn addTarget:self action:@selector(downloadOrRetryBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_removeBtn setImage:[UIImage imageNamed:@"alertClose"] forState:UIControlStateNormal];
        [contentV addSubview:_removeBtn];
        [_removeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.centerY.mas_equalTo(weakSelf.titlelabel);
            make.height.width.mas_equalTo(Adaptor_Value(20));
        }];
        ViewRadius(_removeBtn, Adaptor_Value(10));

        
        _subLable = [UILabel lableWithText:@"" textColor:[UIColor lq_colorWithHexString:@"ffffff"] fontSize:AdaptedFontSize(12) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_subLable];
        [_subLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.titlelabel);
            make.top.mas_equalTo(weakSelf.removeBtn.mas_bottom).offset(Adaptor_Value(15));
            make.right.mas_equalTo(weakSelf.removeBtn.mas_left);
            
        }];
        
        UIView *lineView = [UIView new];
        lineView.backgroundColor = BackGroundColor;
        [contentV addSubview:lineView];
        [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_equalTo(contentV);
            make.height.mas_equalTo(kOnePX);
            make.top.mas_equalTo(weakSelf.subLable.mas_bottom).offset(Adaptor_Value(20));
        }];

        
        _firstBtn = [[UIButton alloc] init];
        [_firstBtn addTarget:self action:@selector(firstBtnClick:) forControlEvents:UIControlEventTouchDown];
        [contentV addSubview:_firstBtn];
        [_firstBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(contentV);
            make.right.mas_equalTo(contentV.mas_centerX).offset(-kOnePX);
            make.height.mas_equalTo(Adaptor_Value(44));
            make.top.mas_equalTo(lineView);
            make.bottom.mas_equalTo(contentV);
        }];
        
        UIView *spreteView = [UIView new];
        _spreteView = spreteView;
        spreteView.backgroundColor = BackGroundColor;
        _spreteView = spreteView;
        [contentV addSubview:spreteView];
        [spreteView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.firstBtn.mas_right);
            make.top.bottom.mas_equalTo(weakSelf.firstBtn);
            make.width.mas_equalTo(kOnePX);
        }];

        
        _secBtn = [[UIButton alloc] init];
        [_secBtn addTarget:self action:@selector(secBtnClick:) forControlEvents:UIControlEventTouchDown];
        [contentV addSubview:_secBtn];
        [_secBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(contentV);
            make.left.mas_equalTo(spreteView.mas_right);
            make.top.height.mas_equalTo(weakSelf.firstBtn);
        }];
        
        
        
        _singleBtn = [[UIButton alloc] init];
        [_singleBtn addTarget:self action:@selector(singleBtnClick:) forControlEvents:UIControlEventTouchDown];
        [contentV addSubview:_singleBtn];
        [_singleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.left.mas_equalTo(contentV);
            make.top.height.mas_equalTo(weakSelf.firstBtn);
        }];
        _singleBtn.hidden = YES;


    }
    return _alertView;
}

- (UIView *)customCoverView{
    if (!_customCoverView) {
        _customCoverView = [UIView new];
        _customCoverView.backgroundColor = [UIColor lq_colorWithHexString:@"#14181A" alpha:0.5];

    }
    return _customCoverView;
}

- (NSString *)titleStr{
//    return self.titlelabel.text.length > 0 ? self.titlelabel.text : self.subLable.text;
    NSString * str ;
    if (self.titlelabel.text.length > 0) {
        str = self.titlelabel.text;
    }else if (self.subLable.text.length > 0){
        str = self.subLable.text;
    }else if (self.titlelabel.attributedText.string.length > 0 ){
        str = self.titlelabel.attributedText.string;
    }else if (self.subLable.attributedText.string.length > 0 ){
        str = self.subLable.attributedText.string;
    }
    return str;
}
@end
