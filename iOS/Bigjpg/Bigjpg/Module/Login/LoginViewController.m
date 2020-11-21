//
//  LoginViewController.m
//  Bigjpg
//
//  Created by rabi on 2019/12/23.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "LoginViewController.h"
#import "LoginCustomView.h"
@interface LoginViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic,strong)UITableView *customTableView;
@property (nonatomic,strong)LoginCustomView *loginCustomView;

@end

@implementation LoginViewController
#pragma mark - 重写
-(void)backClick:(UIButton*)button{
    [self.view endEditing:YES];
    [self dismissViewControllerAnimated:YES completion:nil];

}
//右滑返回功能，默认开启（YES）NO
- (BOOL)gestureRecognizerShouldBegin{
    return NO;
}
#pragma mark - private
#pragma mark -  生命周期
- (void)viewDidLoad {
    [super viewDidLoad];
    [self configUI];
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}
- (void)dealloc{
    NSLog(@"dealloc -- %@",NSStringFromClass([self class]));
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


#pragma mark -  ui
- (void)configUI{
    [self.view addSubview:self.customTableView];
    __weak __typeof(self) weakSelf = self;
    [self.customTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.bottom.right.mas_equalTo(weakSelf.view);
        make.top.mas_equalTo(NavMaxY);
    }];
    UIView *tableHeaderView = [[UIView alloc] init];
    [tableHeaderView addSubview:self.loginCustomView];
    [self.loginCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(tableHeaderView);
    }];
    CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
    tableHeaderView.lq_height = H;
    self.customTableView.tableHeaderView = tableHeaderView;
    self.customTableView.tableHeaderView.lq_height = H;
    
    [self loginCustomViewAct];
    
    //导航栏
    [self addNavigationView];
    self.navigationBackButton.hidden = NO;

    
}
#pragma mark - act
- (void)loginCustomViewAct{
    __weak __typeof(self) weakSelf = self;

    
    //忘记密码
    self.loginCustomView.loginCustomViewForgetBtnClickBlock = ^(NSDictionary * _Nonnull dict) {


    };
    
    //登录
    self.loginCustomView.loginCustomViewConfirmBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender) {
//        [LSVProgressHUD showInfoWithStatus:@"登录"];
        NSString*mobile = [dict safeObjectForKey:@"mobile"];
        NSString*pwd = [dict safeObjectForKey:@"pwd"];
        
        //上个
        UIViewController *lastlVC = (UIViewController *)[weakSelf.navigationController.childViewControllers safeObjectAtIndex:(self.navigationController.childViewControllers.count - 1 - 1  )];

        [weakSelf loginWithMobile:mobile pwd:pwd sender:sender];


    };
}

- (void)lorginSuccess{
   [self.view endEditing:YES];
   [self dismissViewControllerAnimated:NO completion:^{
       
   }];


}
#pragma mark - net
- (void)loginWithMobile:(NSString *)mobile pwd:(NSString *)pwd sender:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    sender.userInteractionEnabled = NO;
//    [LSVProgressHUD show];

}


#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView
 numberOfRowsInSection:(NSInteger)section
{
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"cell"];
    }
    return cell;
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 0.01;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}
#pragma mark - setter

#pragma mark - lazy
- (UITableView *)customTableView{
    if (!_customTableView) {
        _customTableView = [[UITableView alloc] init];
        _customTableView.backgroundColor = BackGroundColor;
        _customTableView.dataSource = self;
        _customTableView.delegate = self;
        _customTableView.separatorStyle = UITableViewCellSelectionStyleNone;
        _customTableView.showsVerticalScrollIndicator = NO;
        _customTableView.showsHorizontalScrollIndicator = NO;
        
    }
    return _customTableView;
}

- (LoginCustomView *)loginCustomView{
    if (!_loginCustomView) {
        _loginCustomView = [[LoginCustomView alloc] init];
    }
    return _loginCustomView;
}



@end
