//
//  LoginCustomView.m
//  Encropy
//
//  Created by Lqq on 2019/4/25.
//  Copyright © 2019年 Lq. All rights reserved.
//

#import "LoginCustomView.h"

@interface LoginCustomView()
//头部view
@property (nonatomic,strong)UIView *header;
@property (nonatomic,strong)UILabel *tipLabel;
@property (nonatomic,strong)UIView *textFBackView;

@property (nonatomic,strong)UITextField *emailTextF;
@property (nonatomic,strong)UITextField *pwdTF;

@property (nonatomic,strong)UIButton *forgetBtn;
@property (nonatomic,strong)UIButton *confirmBtn;

@property (nonatomic,strong)UIButton *zhuceChooseBtn;
@property (nonatomic,strong)UILabel *zhuceTipLabel;

@property (nonatomic,strong)UIImageView *erweimaImageV;
@property (nonatomic,strong)UILabel *erweimaTipLable;

@end
@implementation LoginCustomView


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

- (void)layoutSubviews{
    //添加圆角
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:self.textFBackView.bounds byRoundingCorners:UIRectCornerTopLeft | UIRectCornerBottomLeft cornerRadii:CGSizeMake(20, 20)];
    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.textFBackView.bounds;
    maskLayer.path = maskPath.CGPath;
    self.textFBackView.layer.mask = maskLayer;

}

#pragma mark - 刷新ui
- (void)configUIWithItem:(NSObject *)item finishi:(void(^)())finishBlock{
    
    
    
    finishBlock();
}
#pragma mark - act
- (void)zhuceChooseBtnClick:(UIButton *)sender{



}

- (void)fogetBtnClick:(UIButton *)sender{
    if (self.loginCustomViewForgetBtnClickBlock) {
        self.loginCustomViewForgetBtnClickBlock(nil);
    }
}

- (void)confirmBtnClick:(UIButton *)sender{
    if (self.loginCustomViewConfirmBtnClickBlock) {
        self.loginCustomViewConfirmBtnClickBlock(@{@"mobile":SAFE_NIL_STRING(self.emailTextF.text),@"pwd":SAFE_NIL_STRING(self.pwdTF.text)}, sender);
    }
}

- (void)zhuceTap:(UITapGestureRecognizer *)gest{
    if (self.loginCustomViewZhuceClickBlock) {
        self.loginCustomViewZhuceClickBlock(nil);
    }
}


#pragma  mark - UITextField delegate
- (void)textFDidChange:(UITextField *)textf{
    
    //限制输入字数
    if (self.emailTextF.text.length >= 11) {
        self.emailTextF.text = [self.emailTextF.text substringToIndex:11];
    }
    
    if (self.emailTextF.text.length > 0 && self.pwdTF.text.length > 0 ) {
        self.confirmBtn.enabled = YES;
    }else{
        self.confirmBtn.enabled = NO;
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
        
        _tipLabel = [UILabel lableWithText:lqStrings(@"登录") textColor:[UIColor whiteColor] fontSize:AdaptedBoldFontSize(30) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_tipLabel];
        [_tipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(Adaptor_Value(20));
            make.left.mas_equalTo(Adaptor_Value(35));
        }];
        
        _textFBackView = [UIView new];
        _textFBackView.backgroundColor = BackGroundColor;
        [contentV addSubview:_textFBackView];
        [_textFBackView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.tipLabel.mas_bottom).offset(Adaptor_Value(25));
            make.left.mas_equalTo(weakSelf.tipLabel);
            make.right.mas_equalTo(contentV);
        }];
      
        _emailTextF = [[UITextField alloc] init];
        [_textFBackView addSubview:_emailTextF];
        [_emailTextF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(Adaptor_Value(5));
        }];
        _emailTextF.keyboardType = UIKeyboardTypeNumberPad;
        _emailTextF.textColor = [UIColor whiteColor];
        [_emailTextF addTarget:self action:@selector(textFDidChange:) forControlEvents:UIControlEventEditingChanged];
        _emailTextF.placeholder = lqStrings(@"邮箱");

        // "通过KVC修改placeholder的颜色"
        [_emailTextF setPlaceholderColor:TitleGrayColor font:nil];


        
        UIView *rowLine1 =  [UIView new];
        rowLine1.backgroundColor = BackGroundColor;
        [_textFBackView addSubview:rowLine1];
        [rowLine1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *2);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.emailTextF.mas_bottom).offset(5);
        }];
        

        
        _pwdTF = [[UITextField alloc] init];
        [_textFBackView addSubview:_pwdTF];
        [_pwdTF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.emailTextF);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(weakSelf.emailTextF.mas_bottom).offset(Adaptor_Value(10));
            make.right.mas_equalTo(Adaptor_Value(200));
            make.bottom.mas_equalTo(weakSelf.textFBackView).offset(-Adaptor_Value(5));
        }];
        _pwdTF.textColor = [UIColor whiteColor];
        _pwdTF.secureTextEntry = YES;
        [_pwdTF addTarget:self action:@selector(textFDidChange:) forControlEvents:UIControlEventEditingChanged];
        _pwdTF.placeholder = lqStrings(@"密码");

        // "通过KVC修改placeholder的颜色"
        [_pwdTF setPlaceholderColor:TitleGrayColor font:nil];

        UIView *zhuceView = [UIView new];
        [contentV addSubview:zhuceView];
        [zhuceView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.textFBackView.mas_bottom).offset(Adaptor_Value(20));
            make.centerX.mas_equalTo(contentV);
            make.bottom.mas_equalTo(contentV);
            
        }];

        
        
        _zhuceChooseBtn = [[UIButton alloc] init];
        [_zhuceChooseBtn setImage:[UIImage imageNamed:@"seeoff"] forState:UIControlStateNormal];
        [_zhuceChooseBtn setImage:[UIImage imageNamed:@"see"] forState:UIControlStateSelected];
        [_zhuceChooseBtn addTarget:self action:@selector(zhuceChooseBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_textFBackView addSubview:_zhuceChooseBtn];
        [zhuceView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.bottom.mas_equalTo(zhuceView);
            make.height.width.mas_equalTo(Adaptor_Value(20));
        }];
        
        
        
        _zhuceTipLabel = [UILabel lableWithText:lqStrings(@"注册一个新用户") textColor:[UIColor lq_colorWithHexString:@"#616161"] fontSize:AdaptedFontSize(13) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [zhuceView addSubview:_zhuceTipLabel];
        [_zhuceTipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.right.mas_equalTo(zhuceView);
            make.left.mas_equalTo(weakSelf.zhuceChooseBtn.mas_right).offset(Adaptor_Value(5));
        }];


        
        _confirmBtn = [[UIButton alloc] init];
        [_confirmBtn addTarget:self action:@selector(confirmBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_confirmBtn setTitle:lqStrings(@"登录") forState:UIControlStateNormal];
        [_confirmBtn setTitleColor:BackGroundColor forState:UIControlStateNormal];
        [contentV addSubview:_confirmBtn];
        [_confirmBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(50));
            make.top.mas_equalTo(zhuceView.mas_bottom).offset(Adaptor_Value(25));
            
        }];
        ViewRadius(_confirmBtn, 4);
        _confirmBtn.enabled = NO;
        
        

        _forgetBtn = [[UIButton alloc] init];
        [_forgetBtn setTitle:lqStrings(@"忘记密码?") forState:UIControlStateNormal];
        [_forgetBtn setTitleColor:[UIColor lq_colorWithHexString:@"#f94d4d"] forState:UIControlStateNormal];
        [_forgetBtn addTarget:self action:@selector(fogetBtnClick:) forControlEvents:UIControlEventTouchDown];
        _forgetBtn.titleLabel.font = AdaptedFontSize(12);
        [contentV addSubview:_forgetBtn];
        [_forgetBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(20));
            make.top.mas_equalTo(weakSelf.confirmBtn.mas_bottom).offset(Adaptor_Value(10));
            
        }];
        
        UIView *rowLine3 =  [UIView new];
        rowLine3.backgroundColor = LineGrayColor;
        [contentV addSubview:rowLine3];
        [rowLine3 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.forgetBtn.mas_bottom).offset(30);
        }];
        
        _erweimaImageV = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@""]];
        [contentV addSubview:_erweimaImageV];
        [_erweimaImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.height.mas_equalTo(Adaptor_Value(80));
            make.top.mas_equalTo(weakSelf.forgetBtn.mas_bottom).offset(Adaptor_Value(50));
            make.left.mas_equalTo(weakSelf.textFBackView);
            make.bottom.mas_equalTo(contentV).offset(-Adaptor_Value(50));
        }];
        
        UIView *rowLine4 =  [UIView new];
        rowLine4.backgroundColor = LineGrayColor;
        [contentV addSubview:rowLine4];
        [rowLine4 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.mas_equalTo(kOnePX);
            make.left.mas_equalTo(weakSelf.erweimaImageV.mas_right).offset(Adaptor_Value(10));
            
            make.top.mas_equalTo(weakSelf.erweimaImageV).offset(-Adaptor_Value(5));
            make.bottom.mas_equalTo(weakSelf.erweimaImageV).offset(Adaptor_Value(5));

        }];
        
        _erweimaTipLable = [UILabel lableWithText:lqStrings(@"") textColor:TitleBlackColor fontSize:AdaptedFontSize(15) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_erweimaTipLable];
        [_erweimaTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.erweimaImageV);
            make.left.mas_equalTo(rowLine4.mas_right).offset(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(25));
            make.bottom.mas_lessThanOrEqualTo(contentV).offset(-Adaptor_Value(50));
        }];
    }
    return _header;
}

@end
