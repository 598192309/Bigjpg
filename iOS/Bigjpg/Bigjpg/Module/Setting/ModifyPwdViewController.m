//
//  ModifyPwdViewController.m
//  Bigjpg
//
//  Created by 黎芹 on 2020/1/6.
//  Copyright © 2020 lqq. All rights reserved.
//  修改密码

#import "ModifyPwdViewController.h"
#import "I_Account.h"
@interface ModifyPwdViewController ()<UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate>
@property (strong, nonatomic) UITableView  *customTableView;

@property (nonatomic,strong)UIView *header;
@property (nonatomic,strong)UIView *textFBackView;
@property (nonatomic,strong)UITextField *onePwdTextF;
@property (nonatomic,strong)UIView *lineview1;
@property (nonatomic,strong)UIView *greenlineview1;

@property (nonatomic,strong)UITextField * twoPwdTextF;
@property (nonatomic,strong)UIView *lineview2;
@property (nonatomic,strong)UIView *greenlineview2;


@property (nonatomic,strong)UIButton *confirmBtn;


@end

@implementation ModifyPwdViewController
#pragma mark - 重写

#pragma mark - 生命周期
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];

    
}
- (void)viewDidLoad {
    [super viewDidLoad];

    [self configUI];

    
    if (@available(iOS 11.0, *)) {
        _customTableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        // Fallback on earlier versions
        self.automaticallyAdjustsScrollViewInsets = NO;
    }

    [self setUpNav];
}

- (void)dealloc{
    NSLog(@"dealloc -- %@",NSStringFromClass([self class]));
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}
#pragma mark - ui
- (void)configUI{
    [self.view addSubview:self.customTableView];
    __weak __typeof(self) weakSelf = self;
    [self.customTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.bottom.right.mas_equalTo(weakSelf.view);
        make.top.mas_equalTo(NavMaxY);
    }];

    
    //footer
    UIView *tableHeaderView = [[UIView alloc] init];
    [tableHeaderView addSubview:self.header];
    [self.header mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(tableHeaderView);
    }];
    CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
    tableHeaderView.lq_height = H;
    self.customTableView.tableHeaderView = tableHeaderView;
    self.customTableView.tableHeaderView.lq_height = H;
}
- (void)setUpNav{
    [self addNavigationView];
    self.navigationView.backgroundColor = TabbarGrayColor;
    self.navigationTextLabel.text = LanguageStrings(@"change_password");
}

#pragma mark - act
- (void)confirmBtnClick:(UIButton *)sender{
    [self modifyPwdWithPwd1:self.onePwdTextF.text pwd2:self.twoPwdTextF.text sender:sender];
    
}
#pragma mark - net
- (void)modifyPwdWithPwd1:(NSString *)pwd1 pwd2:(NSString *)pwd2 sender:(UIButton *)sender{
    if (RI.isTouristLogin) {
        [LSVProgressHUD showInfoWithStatus:@"Tourists are not allowed to change their password"];
        return;
    }
    __weak __typeof(self) weakSelf = self;
    [LSVProgressHUD show];
    if (!(pwd1.length > 0 && pwd2.length > 0)) {
        [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"input_new_password")];
        return;
    }
    if (![pwd1 isEqualToString:pwd2]) {
        [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"reset_error")];
        return;
    }
    
    sender.userInteractionEnabled = NO;
    [I_Account updatePassword:pwd1 success:^{
        //修改成功之后 退出登陆
        [I_Account loginOutOnSuccessOnSuccess:^{
            sender.userInteractionEnabled = YES;
            [weakSelf.navigationController popViewControllerAnimated:NO];
            [LSVProgressHUD showSuccessWithStatus:LanguageStrings(@"reset_success")];
        }];

    } failure:^(NSError *error) {
        [LSVProgressHUD showError:error];
        sender.userInteractionEnabled = YES;
    }];

}

#pragma mark -  UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return 0;

}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
   
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 0.01;
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *view = [UIView new];
    view.backgroundColor  = [UIColor clearColor];
    return view;

}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 0.01;
}


- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *view = [UIView new];
    view.backgroundColor  = [UIColor clearColor];
    return view;
}

#pragma  mark - UITextField delegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    if ([textField isEqual:self.onePwdTextF]) {
        self.greenlineview1.hidden = NO;
        self.greenlineview2.hidden = YES;

    }else{
        self.greenlineview1.hidden = YES;
        self.greenlineview2.hidden = NO;
    }
    return YES;
}


#pragma  mark - lazy
- (UITableView *)customTableView
{
    if (_customTableView == nil) {
        _customTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0,LQScreemW, LQScreemH) style:UITableViewStylePlain];
        _customTableView.delegate = self;
        _customTableView.dataSource = self;
        _customTableView.showsVerticalScrollIndicator = NO;
        _customTableView.showsHorizontalScrollIndicator = NO;
        _customTableView.backgroundColor = BackGroundColor;
        
        _customTableView.separatorStyle = UITableViewCellSeparatorStyleNone;


        
        
    }
    return _customTableView;
}
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

        _textFBackView = [UIView new];
        _textFBackView.backgroundColor = BackGroundColor;
        [contentV addSubview:_textFBackView];
        [_textFBackView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(Adaptor_Value(10));
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.height.mas_equalTo(Adaptor_Value(90));
        }];
      
        _onePwdTextF = [[UITextField alloc] init];
        [_textFBackView addSubview:_onePwdTextF];
        [_onePwdTextF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(Adaptor_Value(5));
        }];

        _onePwdTextF.textColor = TitleBlackColor;
        _onePwdTextF.placeholder = LanguageStrings(@"input_new_password");
        _onePwdTextF.delegate = self;

        // "通过KVC修改placeholder的颜色"
        [_onePwdTextF setPlaceholderColor:TitleGrayColor font:nil];

        
        UIView *rowLine1 =  [UIView new];
        _lineview1 = rowLine1;
        rowLine1.backgroundColor = LihgtGreenColor;
        [_textFBackView addSubview:rowLine1];
        [rowLine1 mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *2);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.onePwdTextF.mas_bottom).offset(5);
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

        
        _twoPwdTextF = [[UITextField alloc] init];
        [_textFBackView addSubview:_twoPwdTextF];
        [_twoPwdTextF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.onePwdTextF);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(weakSelf.onePwdTextF.mas_bottom).offset(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
//            make.bottom.mas_equalTo(weakSelf.textFBackView).offset(-Adaptor_Value(5));
        }];
        _twoPwdTextF.textColor = TitleBlackColor;
        _twoPwdTextF.placeholder = LanguageStrings(@"again");
        _twoPwdTextF.delegate = self;

        // "通过KVC修改placeholder的颜色"
        [_twoPwdTextF setPlaceholderColor:TitleGrayColor font:nil];

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
        
        _confirmBtn = [[UIButton alloc] init];
        [_confirmBtn addTarget:self action:@selector(confirmBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_confirmBtn setTitle:LanguageStrings(@"change_password") forState:UIControlStateNormal];
        [_confirmBtn setTitleColor:BackGroundColor forState:UIControlStateNormal];
        _confirmBtn.titleLabel.font = AdaptedFontSize(16);

        _confirmBtn.backgroundColor = LihgtGreenColor;
        [contentV addSubview:_confirmBtn];
        [_confirmBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(50));
            make.top.mas_equalTo(weakSelf.textFBackView.mas_bottom).offset(Adaptor_Value(15));
            make.bottom.mas_equalTo(contentV);
            
        }];
        ViewRadius(_confirmBtn, 4);

    }
    return _header;
}
@end
