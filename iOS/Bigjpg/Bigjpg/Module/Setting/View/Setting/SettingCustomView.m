//
//  SettingCustomView.m
//  Bigjpg
//
//  Created by rabi on 2019/12/24.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "SettingCustomView.h"
#import "M_User.h"

@interface SettingCustomView()<UITextFieldDelegate>
//头部view
@property (nonatomic,strong)UIView *header;
@property (nonatomic,strong)UIView *tipView;
@property (nonatomic,strong)UIButton *tipBtn;
@property (nonatomic,strong)UIButton *updateBtn;

@property (nonatomic,strong)UIView *lorginTipView;
@property (nonatomic,strong)UIButton *lorgintipBtn;
@property (nonatomic,strong)UILabel *lorgintimeLabel;
@property (nonatomic,strong)UIButton *lorginupdateBtn;
@property (nonatomic,strong)UILabel *lorgintotalTipLabel;


@property (nonatomic,strong)UIView *textFBackView;

@property (nonatomic,strong)UITextField *emailTextF;
@property (nonatomic,strong)UIView *lineview1;
@property (nonatomic,strong)UIView *greenlineview1;

@property (nonatomic,strong)UITextField *pwdTF;
@property (nonatomic,strong)UIView *lineview2;
@property (nonatomic,strong)UIView *greenlineview2;

@property (nonatomic,strong)UIButton *forgetBtn;
@property (nonatomic,strong)UIButton *confirmBtn;

@property (nonatomic,strong)UIView *zhuceView;
@property (nonatomic,strong)UIButton *zhuceChooseBtn;
@property (nonatomic,strong)UILabel *zhuceTipLabel;


@property (nonatomic,strong)UIImageView *erweimaImageV;
@property (nonatomic,strong)UILabel *erweimaTipLable;


@end
@implementation SettingCustomView


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
- (void)configUIWithItem:(M_User *)item finishi:(void(^)())finishBlock{
    __weak __typeof(self) weakSelf = self;

    if (RI.is_logined) {
        
        [_textFBackView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(Adaptor_Value(0));
        }];
        self.emailTextF.hidden = YES;
        self.pwdTF.hidden = YES;
        self.pwdTF.text = @"";
        self.lineview1.hidden = YES;
        self.lineview2.hidden = YES;
        self.greenlineview1.hidden = YES;
        self.greenlineview2.hidden = YES;
        [self.zhuceChooseBtn mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(0);
        }];
        self.zhuceTipLabel.text = nil;
        
        NSString *name = [[NSUserDefaults standardUserDefaults] objectForKey:kUserName];
        [self.confirmBtn setTitle:[NSString stringWithFormat:@"%@ %@",LanguageStrings(@"logout"),name] forState:UIControlStateNormal];
        self.confirmBtn.backgroundColor = [UIColor lq_colorWithHexString:@"E54340"];
        self.confirmBtn.enabled = YES;
         [self.confirmBtn mas_updateConstraints:^(MASConstraintMaker *make) {
             make.top.mas_equalTo(weakSelf.zhuceView.mas_bottom).offset(Adaptor_Value(-20));
         }];
      
        NSArray *arr = [ConfManager.shared contentWith:@"version"];
        NSString *typestr;

        if ([item.version isEqualToString:@"pro"]) {
            typestr = [arr safeObjectAtIndex:3];
            self.lorginTipView.backgroundColor = RGB(44, 152, 240);
        }else if ([item.version isEqualToString:@"basic"]) {
            typestr = [arr safeObjectAtIndex:1];
            self.lorginTipView.backgroundColor = RGB(155, 48, 175);

        }else if ([item.version isEqualToString:@"std"]) {
            typestr = [arr safeObjectAtIndex:2];
            self.lorginTipView.backgroundColor = RGB(31, 184, 34);

        }else{
            typestr = [arr safeObjectAtIndex:0];
            self.lorginTipView.backgroundColor = TitleGrayColor;

        }
        NSArray *typeArr;
        if ([typestr containsString:@":"]) {
            typeArr =  [typestr componentsSeparatedByString:@":"];

        }else{
            typeArr =  [typestr componentsSeparatedByString:@"："];

        }

        [self.lorgintipBtn setTitle:typeArr.lastObject forState:UIControlStateNormal];
        NSString *time = [item.expire lq_dealTimeFormarter:@"yyyy-MM-dd HH:mm:ss" changeFormater:@"yyyy-MM-dd"];
        self.lorgintimeLabel.text = time;
        self.lorgintotalTipLabel.text = [NSString stringWithFormat:@"%@%lu",LanguageStrings(@"used"),(unsigned long)item.used];
        
        [_forgetBtn setTitle:LanguageStrings(@"change_password") forState:UIControlStateNormal];
        [_forgetBtn setBackgroundColor:TabbarGrayColor];
        ViewBorderRadius(_forgetBtn, 4, kOnePX, LineGrayColor);

    }else{
 
        [_textFBackView mas_updateConstraints:^(MASConstraintMaker *make) {
              make.height.mas_equalTo(Adaptor_Value(90));
          }];
        self.emailTextF.hidden = NO;
        self.pwdTF.hidden = NO;
        self.lineview1.hidden = NO;
        self.lineview2.hidden = NO;
        
        [self.zhuceChooseBtn mas_updateConstraints:^(MASConstraintMaker *make) {
              make.height.mas_equalTo(30);
          }];
        self.zhuceTipLabel.text = LanguageStrings(@"reg_new");
        
        [self.confirmBtn setTitle:LanguageStrings(@"login_reg") forState:UIControlStateNormal];
//        self.confirmBtn.backgroundColor = LihgtGreenColor;
        if (self.emailTextF.text.length > 0 & self.pwdTF.text.length > 0) {
            self.confirmBtn.backgroundColor = LihgtGreenColor;
        }else{
            self.confirmBtn.backgroundColor = TitleGrayColor;
        }
        [self.confirmBtn mas_updateConstraints:^(MASConstraintMaker *make) {
             make.top.mas_equalTo(weakSelf.zhuceView.mas_bottom).offset(Adaptor_Value(25));
         }];
        [_forgetBtn setTitle:LanguageStrings(@"reset") forState:UIControlStateNormal];
        [_forgetBtn setBackgroundColor:[UIColor clearColor]];
        ViewBorderRadius(_forgetBtn, 4, kOnePX, [UIColor clearColor]);

    }

    self.lorginTipView.hidden = !RI.is_logined;
        
    if (RI.isTouristLogin) {//登录成功的游客模式
                [_forgetBtn mas_updateConstraints:^(MASConstraintMaker *make) {
                    make.height.mas_equalTo(0);
                }];
    }else{
        [self.forgetBtn mas_updateConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(Adaptor_Value(50));
        }];
    }
    finishBlock();
}



#pragma mark - act
- (void)updateBtnClick:(UIButton *)sender{
    if (self.settingCustomViewUpdateBtnClickBlock) {
        self.settingCustomViewUpdateBtnClickBlock(@{});
    }
}
- (void)zhuceChooseBtnClick:(UIButton *)sender{
    sender.selected = !sender.selected;
}

- (void)fogetBtnClick:(UIButton *)sender{
    if (self.settingCustomViewForgetBtnClickBlock) {
        self.settingCustomViewForgetBtnClickBlock(@{},sender);
    }
}

- (void)confirmBtnClick:(UIButton *)sender{
    if (self.settingCustomViewConfirmBtnClickBlock) {
        self.settingCustomViewConfirmBtnClickBlock(@{@"email":SAFE_NIL_STRING(self.emailTextF.text),@"pwd":SAFE_NIL_STRING(self.pwdTF.text),@"zhuce":@(self.zhuceChooseBtn.selected)}, sender);
    }
}

- (void)zhuceTap:(UITapGestureRecognizer *)gest{
    [self zhuceChooseBtnClick:self.zhuceChooseBtn];
}

- (void)savePicture:(UITapGestureRecognizer *)gest{
    if (self.settingCustomViewSavePictureBlock) {
        self.settingCustomViewSavePictureBlock(self.erweimaImageV.image);
    }
}
#pragma  mark - UITextField delegate
- (void)textFDidChange:(UITextField *)textf{
    
    if (self.emailTextF.text.length > 0 && self.pwdTF.text.length > 0 ) {
        self.confirmBtn.enabled = YES;
        _confirmBtn.backgroundColor = LihgtGreenColor;

    }else{
        self.confirmBtn.enabled = NO;
        _confirmBtn.backgroundColor = TitleGrayColor;
    }
    
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    if ([textField isEqual:self.emailTextF]) {
        self.greenlineview1.hidden = NO;
        self.greenlineview2.hidden = YES;

    }else{
        self.greenlineview1.hidden = YES;
        self.greenlineview2.hidden = NO;
    }
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    if ([textField isEqual:self.emailTextF]) {
        [self.pwdTF becomeFirstResponder];
    }else{
        if (self.settingCustomViewConfirmBtnClickBlock) {
            self.settingCustomViewConfirmBtnClickBlock(@{@"email":SAFE_NIL_STRING(self.emailTextF.text),@"pwd":SAFE_NIL_STRING(self.pwdTF.text),@"zhuce":@(self.zhuceChooseBtn.selected)}, self.confirmBtn);
        }
    }
    return YES;
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
        
        _tipView = [UIView new];
        _tipView.backgroundColor = RI.isNight ? RGB(31, 31, 31) : RGB(238, 238, 238);
        [contentV addSubview:_tipView];
        [_tipView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.height.mas_equalTo(Adaptor_Value(120));
            make.top.mas_equalTo(TopAdaptor_Value(30));
        }];
        ViewRadius(_tipView, 4);
                
        
        _tipBtn = [[UIButton alloc] init];
        NSArray *arr = [ConfManager.shared contentWith:@"version"];
        NSString *typestr = [arr safeObjectAtIndex:0];
        NSArray *typeArr;
        if ([typestr containsString:@":"]) {
            typeArr =  [typestr componentsSeparatedByString:@":"];

        }else{
            typeArr =  [typestr componentsSeparatedByString:@"："];

        }
        [_tipBtn setTitle:typeArr.lastObject forState:UIControlStateNormal];
        [_tipBtn setTitleColor:TitleBlackColor forState:UIControlStateNormal];
        [_tipView addSubview:_tipBtn];
        [_tipBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.tipView);
            make.right.mas_equalTo(weakSelf.tipView.mas_centerX).offset(-Adaptor_Value(10));
       }];
        _tipBtn.enabled = NO;
        
        _updateBtn = [[UIButton alloc] init];
        [_updateBtn setTitle:LanguageStrings(@"upgrade") forState:UIControlStateNormal];
        [_updateBtn addTarget:self action:@selector(updateBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [_updateBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _updateBtn.titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightMedium];
        [_tipView addSubview:_updateBtn];
        _updateBtn.contentEdgeInsets = UIEdgeInsetsMake(7, 15, 7, 15);

//        CGFloat w = [LanguageStrings(@"upgrade") boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:_updateBtn.titleLabel.font} context:nil].size.width + Adaptor_Value(20);

        [_updateBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.tipView);
            make.left.mas_equalTo(weakSelf.tipView.mas_centerX).offset(Adaptor_Value(10));
//            make.width.mas_equalTo(w);
            
        }];
        _updateBtn.backgroundColor = LihgtGreenColor;
        ViewRadius(_updateBtn, 4);
        
        [contentV addSubview:self.lorginTipView];
        [self.lorginTipView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.tipView);
        }];
        self.lorginTipView.hidden = !RI.is_logined;
        

        
        _textFBackView = [UIView new];
        _textFBackView.backgroundColor = BackGroundColor;
        [contentV addSubview:_textFBackView];
        [_textFBackView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.tipView.mas_bottom).offset(Adaptor_Value(25));
            make.left.mas_equalTo(weakSelf.tipView);
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.height.mas_equalTo(Adaptor_Value(90));
        }];
      
        _emailTextF = [[UITextField alloc] init];
        [_textFBackView addSubview:_emailTextF];
        _emailTextF.returnKeyType = UIReturnKeyNext;
        [_emailTextF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(Adaptor_Value(5));
        }];
        NSArray *user_pass = [ConfManager.shared contentWith:@"user_pass"];

        _emailTextF.keyboardType = UIKeyboardTypeEmailAddress;
        _emailTextF.textColor = TitleBlackColor;
        [_emailTextF addTarget:self action:@selector(textFDidChange:) forControlEvents:UIControlEventEditingChanged];
        _emailTextF.placeholder = [user_pass safeObjectAtIndex:0];
        _emailTextF.delegate = self;

        // "通过KVC修改placeholder的颜色"
        [_emailTextF setPlaceholderColor:TitleGrayColor font:nil];
        _emailTextF.text = [[NSUserDefaults standardUserDefaults] objectForKey:kUserName];

        
        UIView *rowLine1 =  [UIView new];
        _lineview1 = rowLine1;
        rowLine1.backgroundColor = LihgtGreenColor;
        [_textFBackView addSubview:rowLine1];
        [rowLine1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *2);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.emailTextF.mas_bottom).offset(5);
        }];
        
        _greenlineview1 = [UIView new];
        _greenlineview1.backgroundColor = [UIColor greenColor];
        [_textFBackView addSubview:_greenlineview1];
        [_greenlineview1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *3);
            make.left.right.mas_equalTo(weakSelf.lineview1);
            make.centerY.mas_equalTo(weakSelf.lineview1);
        }];
        _greenlineview1.hidden = YES;

        
        _pwdTF = [[UITextField alloc] init];
        [_textFBackView addSubview:_pwdTF];
        _pwdTF.returnKeyType = UIReturnKeyDone;
        [_pwdTF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.emailTextF);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(weakSelf.emailTextF.mas_bottom).offset(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
//            make.bottom.mas_equalTo(weakSelf.textFBackView).offset(-Adaptor_Value(5));
        }];
        _pwdTF.textColor = TitleBlackColor;
        _pwdTF.secureTextEntry = YES;
        [_pwdTF addTarget:self action:@selector(textFDidChange:) forControlEvents:UIControlEventEditingChanged];
        _pwdTF.placeholder = [user_pass safeObjectAtIndex:1];
        _pwdTF.delegate = self;

        // "通过KVC修改placeholder的颜色"
        [_pwdTF setPlaceholderColor:TitleGrayColor font:nil];

        UIView *rowLine2 =  [UIView new];
        _lineview2 = rowLine2;
        rowLine2.backgroundColor = LihgtGreenColor;
        [_textFBackView addSubview:rowLine2];
        [rowLine2 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *2);
            make.bottom.mas_equalTo(weakSelf.textFBackView);
            make.left.right.mas_equalTo(rowLine1);

        }];
        
        _greenlineview2 = [UIView new];
        _greenlineview2.backgroundColor = [UIColor greenColor];
        [_textFBackView addSubview:_greenlineview2];
        [_greenlineview2 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *3);
            make.left.right.mas_equalTo(weakSelf.lineview2);
            make.centerY.mas_equalTo(weakSelf.lineview2);
        }];
        _greenlineview2.hidden = YES;
        
        UIView *zhuceView = [UIView new];
        _zhuceView = zhuceView;
        [contentV addSubview:zhuceView];
        [zhuceView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.textFBackView.mas_bottom).offset(Adaptor_Value(20));
            make.left.mas_equalTo(weakSelf.textFBackView);
            
        }];

        
        
        _zhuceChooseBtn = [[UIButton alloc] init];
        [_zhuceChooseBtn setImage:[[UIImage imageNamed:@"ic_uncheck"] qmui_imageWithTintColor:DeepGreenColor] forState:UIControlStateNormal];
        [_zhuceChooseBtn setImage:[UIImage imageNamed:@"ic_check"]  forState:UIControlStateSelected];

        [_zhuceChooseBtn addTarget:self action:@selector(zhuceChooseBtnClick:) forControlEvents:UIControlEventTouchDown];
        [zhuceView addSubview:_zhuceChooseBtn];
        [_zhuceChooseBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.left.bottom.mas_equalTo(zhuceView);
            make.height.width.mas_equalTo(Adaptor_Value(30));
        }];
                
        _zhuceTipLabel = [UILabel lableWithText:LanguageStrings(@"reg_new") textColor:[UIColor lq_colorWithHexString:@"#616161"] fontSize:AdaptedFontSize(15) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [zhuceView addSubview:_zhuceTipLabel];
        [_zhuceTipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.right.mas_equalTo(zhuceView);
            make.left.mas_equalTo(weakSelf.zhuceChooseBtn.mas_right).offset(Adaptor_Value(0));
        }];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(zhuceTap:)];
        _zhuceTipLabel.userInteractionEnabled = YES;
        [_zhuceTipLabel addGestureRecognizer:tap];


        
        _confirmBtn = [[UIButton alloc] init];
        [_confirmBtn addTarget:self action:@selector(confirmBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_confirmBtn setTitle:lqStrings(@"登录") forState:UIControlStateNormal];
        [_confirmBtn setTitleColor:[UIColor lq_colorWithHexString:@"ffffff"] forState:UIControlStateNormal];
        _confirmBtn.titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightMedium];

        _confirmBtn.backgroundColor = TitleGrayColor;
        _confirmBtn.enabled = NO;
        
        [contentV addSubview:_confirmBtn];
        [_confirmBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(50));
            make.top.mas_equalTo(zhuceView.mas_bottom).offset(Adaptor_Value(25));
            
        }];
        ViewRadius(_confirmBtn, 4);
        
        

        _forgetBtn = [[UIButton alloc] init];
        [_forgetBtn setTitle:LanguageStrings(@"reset") forState:UIControlStateNormal];
        [_forgetBtn setTitleColor:TitleBlackColor forState:UIControlStateNormal];
        [_forgetBtn addTarget:self action:@selector(fogetBtnClick:) forControlEvents:UIControlEventTouchDown];
        _forgetBtn.titleLabel.font = [UIFont systemFontOfSize:16 weight:UIFontWeightMedium];
        [contentV addSubview:_forgetBtn];

        [_forgetBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(weakSelf.confirmBtn.mas_bottom).offset(10);
            make.left.right.mas_equalTo(weakSelf.confirmBtn);
            make.height.mas_equalTo(Adaptor_Value(50));
        }];
        ViewBorderRadius(_forgetBtn, 4, kOnePX, LineGrayColor);
        
        BOOL iszh = [ConfManager.shared.localLanguage isEqualToString:@"zh"];
        
        UIView *rowLine3 =  [UIView new];
        rowLine3.backgroundColor = LineGrayColor;
        [contentV addSubview:rowLine3];
        [rowLine3 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.forgetBtn.mas_bottom).offset(20);
            if (!iszh) {
                make.bottom.mas_equalTo(contentV);
            }
        }];
        
        _erweimaImageV = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"wechat_qr"]];
        [contentV addSubview:_erweimaImageV];
        [_erweimaImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.height.mas_equalTo(Adaptor_Value(80));
            make.top.mas_equalTo(weakSelf.forgetBtn.mas_bottom).offset(Adaptor_Value(40));
            make.left.mas_equalTo(weakSelf.textFBackView);
            if (iszh) {
                make.bottom.mas_equalTo(contentV).offset(-Adaptor_Value(20));
            }
        }];
        _erweimaImageV.hidden = !iszh;
        

        
        UIView *rowLine4 =  [UIView new];
        rowLine4.backgroundColor = LineGrayColor;
        [contentV addSubview:rowLine4];
        [rowLine4 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.mas_equalTo(kOnePX);
            make.left.mas_equalTo(weakSelf.erweimaImageV.mas_right).offset(Adaptor_Value(10));
            
            make.top.mas_equalTo(weakSelf.erweimaImageV).offset(-Adaptor_Value(5));
            make.bottom.mas_equalTo(weakSelf.erweimaImageV).offset(Adaptor_Value(5));

        }];
        rowLine4.hidden = !iszh;

        
        _erweimaTipLable = [UILabel lableWithText:lqStrings(@"[bigjpg 人工智能] 微信公众号会定期推送超超超清美图壁纸插画和黑科技新功能") textColor:TitleBlackColor fontSize:AdaptedFontSize(15) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_erweimaTipLable];
        [_erweimaTipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.erweimaImageV);
            make.left.mas_equalTo(rowLine4.mas_right).offset(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(25));
        }];
        _erweimaTipLable.hidden = !iszh;

        UIView *tapView = [UIView new];
        [contentV addSubview:tapView];
        [tapView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.erweimaImageV);
            make.top.right.mas_equalTo(weakSelf.erweimaTipLable);
            make.centerY.mas_equalTo(weakSelf.erweimaTipLable);
            make.height.mas_greaterThanOrEqualTo(weakSelf.erweimaImageV);
        }];
        UITapGestureRecognizer *erweimatap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(savePicture:)];
        [tapView addGestureRecognizer:erweimatap];
        tapView.userInteractionEnabled = YES;
    }
    return _header;
}
- (UIView *)lorginTipView{
    if (!_lorginTipView) {
        _lorginTipView = [UIView new];
        _lorginTipView.backgroundColor = BlueBackColor;

        UIView *contentV = [UIView new];
        [_lorginTipView addSubview:contentV];
        __weak __typeof(self) weakSelf = self;
        
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.lorginTipView);
        }];
        
        UIView *topBackView = [UIView new];
//        topBackView.backgroundColor = [UIColor redColor];
        [contentV addSubview:topBackView];
        [topBackView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(contentV.mas_centerY);
            make.centerX.mas_equalTo(contentV);
        }];
        _lorgintimeLabel = [[UILabel alloc] init];
        _lorgintimeLabel.textColor = [UIColor whiteColor];
        _lorgintimeLabel.font = AdaptedFontSize(16);
        [topBackView addSubview:_lorgintimeLabel];
        [_lorgintimeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.mas_equalTo(topBackView);
            make.height.mas_equalTo(Adaptor_Value(20));
        }];

        _lorgintipBtn = [[UIButton alloc] init];
        NSArray *arr = [ConfManager.shared contentWith:@"version"];
        _lorgintipBtn.contentEdgeInsets = UIEdgeInsetsZero;
        [_lorgintipBtn setTitle:[arr safeObjectAtIndex:0] forState:UIControlStateNormal];
        [_lorgintipBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [topBackView addSubview:_lorgintipBtn];
        _lorgintipBtn.titleLabel.font = AdaptedFontSize(18);
        [_lorgintipBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.lorgintimeLabel);
            make.right.mas_equalTo(weakSelf.lorgintimeLabel.mas_left).offset(-Adaptor_Value(10));
            make.left.equalTo(topBackView);
        }];
        

        
        _lorginupdateBtn = [[UIButton alloc] init];
        [_lorginupdateBtn setTitle:LanguageStrings(@"upgrade") forState:UIControlStateNormal];
        [_lorginupdateBtn addTarget:self action:@selector(updateBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [_lorginupdateBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _lorginupdateBtn.titleLabel.font = AdaptedFontSize(15);
        [topBackView addSubview:_lorginupdateBtn];
        _lorginupdateBtn.contentEdgeInsets = UIEdgeInsetsMake(7, 15, 7, 15);

        [_lorginupdateBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(weakSelf.lorgintimeLabel);
            make.left.mas_equalTo(weakSelf.lorgintimeLabel.mas_right).offset(Adaptor_Value(10));
            make.top.right.equalTo(topBackView);
        }];
        _lorginupdateBtn.backgroundColor = LihgtGreenColor;
        ViewRadius(_lorginupdateBtn, 4);
        
        _lorgintotalTipLabel = [UILabel lableWithText:LanguageStrings(@"used") textColor:[UIColor whiteColor] fontSize:AdaptedFontSize(14) lableSize:CGRectZero textAliment:NSTextAlignmentLeft numberofLines:0];
        [contentV addSubview:_lorgintotalTipLabel];
        [_lorgintotalTipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.mas_equalTo(contentV);
            make.top.mas_equalTo(weakSelf.lorginupdateBtn.mas_bottom).offset(Adaptor_Value(15));
        }];
        ViewRadius(_lorginTipView, 4);

    }
    return _lorginTipView;
}
- (NSString *)getemail{
    return self.emailTextF.text;
}
@end
