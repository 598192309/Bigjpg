//
//  ForgetPwdViewController.m
//  Bigjpg
//
//  Created by 黎芹 on 2020/1/5.
//  Copyright © 2020 lqq. All rights reserved.
//

#import "ForgetPwdViewController.h"
#import "I_Account.h"
@interface ForgetPwdViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (strong, nonatomic) UITableView  *customTableView;

@property (nonatomic,strong)UIView *header;
@property (nonatomic,strong)UIView *textFBackView;
@property (nonatomic,strong)UITextField *emailTextF;
@property (nonatomic,strong)UIView *greenlineview;
@property (nonatomic,strong)UIButton *confirmBtn;


@end

@implementation ForgetPwdViewController
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
    self.navigationTextLabel.text = LanguageStrings(@"reset");
}

#pragma mark - act
- (void)confirmBtnClick:(UIButton *)sender{
    [self sendEmailWithEmail:self.emailTextF.text sender:sender];
    
}
#pragma mark - net
- (void)sendEmailWithEmail:(NSString *)email sender:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    [SVProgressHUD show];
    [I_Account resetPwd:email success:^{
        [SVProgressHUD showSuccessWithStatus:LanguageStrings(@"check_email")];
        [SVProgressHUD dismissWithDelay:2];
        [weakSelf.navigationController popViewControllerAnimated:YES];
    } failure:^(NSError *error) {
        [SVProgressHUD showErrorWithStatus:LanguageStrings(error.lq_errorMsg)];
        [SVProgressHUD dismissWithDelay:2];
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
            make.height.mas_equalTo(Adaptor_Value(45));
        }];
      
        _emailTextF = [[UITextField alloc] init];
        [_textFBackView addSubview:_emailTextF];
        [_emailTextF mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(weakSelf.textFBackView);
            make.height.mas_equalTo(Adaptor_Value(35));
            make.top.mas_equalTo(Adaptor_Value(5));
        }];
        NSArray *user_pass = [ConfManager.shared contentWith:@"user_pass"];
        _emailTextF.keyboardType = UIKeyboardTypeEmailAddress;
        _emailTextF.textColor = TitleBlackColor;
        _emailTextF.placeholder = [user_pass safeObjectAtIndex:0];
        _emailTextF.text = self.email;

        // "通过KVC修改placeholder的颜色"
        [_emailTextF setPlaceholderColor:TitleGrayColor font:nil];
//        _emailTextF.text = [[NSUserDefaults standardUserDefaults] objectForKey:kUserName];

        
        _greenlineview = [UIView new];
        _greenlineview.backgroundColor = [UIColor greenColor];
        [_textFBackView addSubview:_greenlineview];
        [_greenlineview mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(kOnePX *2);
            make.left.right.mas_equalTo(weakSelf.textFBackView);
            make.top.mas_equalTo(weakSelf.emailTextF.mas_bottom).offset(5);
        }];
        
        _confirmBtn = [[UIButton alloc] init];
        [_confirmBtn addTarget:self action:@selector(confirmBtnClick:) forControlEvents:UIControlEventTouchDown];
        [_confirmBtn setTitle:LanguageStrings(@"send_email") forState:UIControlStateNormal];
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
