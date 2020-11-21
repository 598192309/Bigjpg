//
//  SettingViewController.m
//  Bigjpg
//
//  Created by rabi on 2019/12/23.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "SettingViewController.h"
#import "SettingCustomView.h"
#import "SettingCell.h"
#import "CustomActivity.h"
#import "SetConfigViewController.h"
#import "I_Account.h"
#import "ForgetPwdViewController.h"
#import "ModifyPwdViewController.h"
#import "BuyViewController.h"
#import "BigjpgWebViewController.h"

@interface SettingViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (strong, nonatomic) UITableView  *customTableView;
@property (nonatomic,strong)SettingCustomView *settingCustomView;
@property (nonatomic,strong)UIView *footerView;
@property (nonatomic,strong)UIButton *versionBtn;
@property (nonatomic,strong)CustomAlertView *loginOutAlertView;

@end

@implementation SettingViewController
#pragma mark - 重写

#pragma mark - 生命周期
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (RI.is_logined) {
        [self requestUserInfo];
    }
    
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

    //监听用户登录成功
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshUI) name:kUserSignIn object:nil];
    //监听用户退出登录
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshUI) name:kUserSignOut object:nil];
    //监听用户切换语言
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(changLanguage) name:kChangeLanguageNotification object:nil];
    //切换夜间模式
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(changLanguage) name:kChangeNightNotification object:nil];
    [self refreshUI];
  

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
        make.left.bottom.right.top.mas_equalTo(weakSelf.view);
    }];
    self.customTableView.contentInset = UIEdgeInsetsMake(0, 0, TabbarH, 0);
    [self setUpHeader];
    if (RI.is_logined) {
        [self.settingCustomView configUIWithItem:RI.userInfo finishi:^{
            UIView *tableHeaderView = [[UIView alloc] init];
            [tableHeaderView addSubview:weakSelf.settingCustomView];
            [weakSelf.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.equalTo(tableHeaderView);
            }];
            CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
            tableHeaderView.lq_height = H;
            weakSelf.customTableView.tableHeaderView = tableHeaderView;
            weakSelf.customTableView.tableHeaderView.lq_height = H;
        }];
    }

    
    //footer
    UIView *tableFooterView = [[UIView alloc] init];
    [tableFooterView addSubview:self.footerView];
    [self.footerView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(tableFooterView);
    }];

    CGFloat fH = [tableFooterView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
    tableFooterView.lq_height = fH;
    self.customTableView.tableFooterView = tableFooterView;
    self.customTableView.tableFooterView.lq_height = fH;
    
}
- (void)setUpHeader{
    UIView *tableHeaderView = [[UIView alloc] init];
    [tableHeaderView addSubview:self.settingCustomView];
    [self.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(tableHeaderView);
    }];
    
    CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
    tableHeaderView.lq_height = H;
    self.customTableView.tableHeaderView = tableHeaderView;
    self.customTableView.tableHeaderView.lq_height = H;

    [self settingCustomViewAct];
}

- (void)refreshUI{
    __weak __typeof(self) weakSelf = self;
    [self.settingCustomView configUIWithItem:RI.userInfo finishi:^{
       UIView *tableHeaderView = [[UIView alloc] init];
       [tableHeaderView addSubview:weakSelf.settingCustomView];
       [weakSelf.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
           make.edges.equalTo(tableHeaderView);
       }];
       CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
       tableHeaderView.lq_height = H;
       weakSelf.customTableView.tableHeaderView = tableHeaderView;
       weakSelf.customTableView.tableHeaderView.lq_height = H;
   }];
}
- (void)changLanguage{
    self.view.backgroundColor = BackGroundColor;
    self.customTableView.backgroundColor = BackGroundColor;
    [self.customTableView reloadData];
    [self.settingCustomView removeFromSuperview];
    self.settingCustomView = nil;
    [self setUpHeader];
    [self refreshUI];
    
}


#pragma mark - act

- (void)settingCustomViewAct{
    __weak __typeof(self) weakSelf = self;
    //登录 退出
    self.settingCustomView.settingCustomViewConfirmBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender){
        NSString *email = [dict safeObjectForKey:@"email"];
        NSString *pwd = [dict safeObjectForKey:@"pwd"];
        BOOL zhuce = [[dict safeObjectForKey:@"zhuce"] boolValue];
        if ([[sender titleForState:UIControlStateNormal] isEqualToString:LanguageStrings(@"login_reg")]) {//登录 或者注册
            [weakSelf loginWithUserName:email pwd:pwd notReg:zhuce sender:sender];
        }else{//退出
            [SystemAlertViewController alertViewControllerWithTitle:nil message:LanguageStrings(@"logout") cancleButtonTitle:LanguageStrings(@"cancel") commitButtonTitle:LanguageStrings(@"ok") cancleBlock:^{
              
            } commitBlock:^{
                [weakSelf logOut:nil];
            }];

        }
    };
    //升级
    self.settingCustomView.settingCustomViewUpdateBtnClickBlock = ^(NSDictionary * _Nonnull dict) {
        BuyViewController *vc = [[BuyViewController alloc] init];
        [weakSelf.navigationController pushViewController:vc animated:YES];
    };
    //忘记密码 修改密码
    self.settingCustomView.settingCustomViewForgetBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender) {
        if ([[sender titleForState:UIControlStateNormal] isEqualToString:LanguageStrings(@"change_password")]) {
            //修改密码
            ModifyPwdViewController *vc = [[ModifyPwdViewController alloc] init];
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }else{//忘记密码
            ForgetPwdViewController *vc = [[ForgetPwdViewController alloc] init];
            vc.email = [weakSelf.settingCustomView getemail];
            [weakSelf.navigationController pushViewController:vc animated:YES];
        }
    };
    
    //保存图片
    self.settingCustomView.settingCustomViewSavePictureBlock = ^(UIImage * _Nonnull image) {
        [weakSelf saveErweima:image];
    };
}
//系统分享
- (void)activityShare{
    // 1、设置分享的内容，并将内容添加到数组中
   // 1、设置分享的内容，并将内容添加到数组中
   NSString *shareText = LanguageStrings(@"title");
   //图片应是加载完成后的image或本地的image，否则可能会出错
    UIImage *shareImage = [UIImage imageNamed:@"ic_launch_screen"];
    NSURL *shareUrl = [NSURL URLWithString:@"https://bigjpg.com"];
    NSArray *activityItemsArray = @[shareText,shareImage,shareUrl];
   // 2、初始化控制器，添加分享内容至控制器
   UIActivityViewController *activityVC = [[UIActivityViewController alloc]initWithActivityItems:activityItemsArray applicationActivities:nil];
   activityVC.modalInPopover = YES;
   // 3、设置回调
   if ([UIDevice currentDevice].systemVersion.floatValue >= 8.0) {
       // ios8.0 之后用此方法回调
       UIActivityViewControllerCompletionWithItemsHandler itemsBlock = ^(UIActivityType __nullable activityType, BOOL completed, NSArray * __nullable returnedItems, NSError * __nullable activityError){
           NSLog(@"activityType == %@",activityType);
           if (completed == YES) {
               NSLog(@"completed");
           }else{
               NSLog(@"cancel");
           }
       };
       activityVC.completionWithItemsHandler = itemsBlock;
   }else{
       // ios8.0 之前用此方法回调
       UIActivityViewControllerCompletionHandler handlerBlock = ^(UIActivityType __nullable activityType, BOOL completed){
           NSLog(@"activityType == %@",activityType);
           if (completed == YES) {
               NSLog(@"completed");
           }else{
               NSLog(@"cancel");
           }
       };
       activityVC.completionHandler = handlerBlock;
   }
   // 4、调用控制器
   [self presentViewController:activityVC animated:YES completion:nil];
  
    
}

- (void)remindShow:(NSString *)msg msgColor:(UIColor *)msgColor msgFont:(UIFont *)msgFont subMsg:(NSString *)subMsg submsgColor:(UIColor *)submsgColor submsgFont:(UIFont *)submsgFont firstBtnTitle:(NSString *)firstBtnTitle secBtnTitle:(NSString *)secBtnTitle singeBtnTitle:(NSString *)singeBtnTitle removeBtnHidden:(BOOL)removeBtnHidden{
    NSAttributedString *attr = [msg lq_getAttributedStringWithLineSpace:Adaptor_Value(5) kern:Adaptor_Value(2)  ];

    [self.loginOutAlertView refreshUIWithAttributeTitle:attr titleColor:msgColor titleFont:msgFont titleAliment:NSTextAlignmentCenter attributeSubTitle:[[NSAttributedString alloc]initWithString:SAFE_NIL_STRING(subMsg) ] subTitleColor:submsgColor subTitleFont:submsgFont subTitleAliment:NSTextAlignmentCenter firstBtnTitle:firstBtnTitle firstBtnTitleColor:TitleGrayColor secBtnTitle:secBtnTitle secBtnTitleColor:TitleBlackColor singleBtnHidden:singeBtnTitle.length == 0 singleBtnTitle:singeBtnTitle singleBtnTitleColor:nil removeBtnHidden:removeBtnHidden];
    [[UIApplication sharedApplication].keyWindow addSubview:self.loginOutAlertView];
    
    [self.loginOutAlertView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo([UIApplication sharedApplication].keyWindow);
    }];
}
#pragma mark - net
//登录 注册
- (void)loginWithUserName:(NSString *)usename pwd:(NSString *)pwd notReg:(BOOL)notReg sender:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    [LSVProgressHUD show];
    sender.userInteractionEnabled = NO;
    [I_Account loginOrRegistWithUserName:usename pwd:pwd notReg:notReg success:^(M_User * _Nonnull userInfo) {
        RI.isTouristLogin = NO;
         [LSVProgressHUD dismiss];
        sender.userInteractionEnabled = YES;
        [weakSelf.settingCustomView configUIWithItem:userInfo finishi:^{
            UIView *tableHeaderView = [[UIView alloc] init];
            [tableHeaderView addSubview:weakSelf.settingCustomView];
            [weakSelf.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.equalTo(tableHeaderView);
            }];
            CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
            tableHeaderView.lq_height = H;
            weakSelf.customTableView.tableHeaderView = tableHeaderView;
            weakSelf.customTableView.tableHeaderView.lq_height = H;
        }];
        [weakSelf requestUserInfo];
    } failure:^(NSError *error) {
        [LSVProgressHUD showError:error];
        sender.userInteractionEnabled = YES;

    }];
}


- (void)requestUserInfo {
    __weak __typeof(self) weakSelf = self;
    [I_Account getUserInfoOnSuccess:^(M_User * _Nonnull userInfo) {
              [weakSelf.settingCustomView configUIWithItem:userInfo finishi:^{
                  UIView *tableHeaderView = [[UIView alloc] init];
                  [tableHeaderView addSubview:weakSelf.settingCustomView];
                  [weakSelf.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
                      make.edges.equalTo(tableHeaderView);
                  }];
                  CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
                  tableHeaderView.lq_height = H;
                  weakSelf.customTableView.tableHeaderView = tableHeaderView;
                  weakSelf.customTableView.tableHeaderView.lq_height = H;
              }];
              
              
          } failure:^(NSError *error) {
             
          }];
}
//退出登录
- (void)logOut:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    sender.userInteractionEnabled = NO;
    [I_Account loginOutOnSuccessOnSuccess:^{
        sender.userInteractionEnabled = YES;
        [weakSelf.settingCustomView configUIWithItem:RI.userInfo finishi:^{
            UIView *tableHeaderView = [[UIView alloc] init];
            [tableHeaderView addSubview:weakSelf.settingCustomView];
            [weakSelf.settingCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.equalTo(tableHeaderView);
            }];
            CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
            tableHeaderView.lq_height = H;
            weakSelf.customTableView.tableHeaderView = tableHeaderView;
            weakSelf.customTableView.tableHeaderView.lq_height = H;
        }];
    }];
}
#pragma mark - save image
- (void)saveErweima:(UIImage *)image{
    UIImageWriteToSavedPhotosAlbum(image, self, @selector(image:didFinishSavingWithError:contextInfo:), NULL);
}
- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo
{
    if(!error){
        [LSVProgressHUD showSuccessWithStatus:NSLocalizedString(@"二维码图片已保存至相册", nil)];
    }else{
        [LSVProgressHUD showInfoWithStatus:NSLocalizedString(@"二维码图片保存至相册失败", nil)];
        
    }
}
#pragma mark -  UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return 7;

}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SettingCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([SettingCell class]) forIndexPath:indexPath];

    
    if (indexPath.row == 0) {
        [cell refreshUIWithTitle:LanguageStrings(@"conf")];
        
    }else if (indexPath.row == 1) {
        [cell refreshUIWithTitle:LanguageStrings(@"share")];

    }else if (indexPath.row == 2) {
        [cell refreshUIWithTitle:LanguageStrings(@"visit")];

    }else if (indexPath.row == 3) {
        [cell refreshUIWithTitle:LanguageStrings(@"feedback")];

    }else if (indexPath.row == 4) {
        [cell refreshUIWithTitle:@"User Agreement"];

    } else if (indexPath.row == 5) {
        [cell refreshUIWithTitle:@"Privacy Policy"];
    } else if (indexPath.row == 6) {
        [cell refreshUIWithTitle:LanguageStrings(@"clear_cache")];
    }
  
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    switch (indexPath.row) {
        case 0:{
            SetConfigViewController *vc = [[SetConfigViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
            
            break;
        case 1:{//系统分享
            [self activityShare];
        }
            
            break;
        case 2:{
            //打开官网
            if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"https://"]]) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"https://bigjpg.com"]];
            }
        }
            
            break;
        case 3:{
            
        }
            
            break;
        case 4:{
            //用户协议
            BigjpgWebViewController *vc = [[BigjpgWebViewController alloc] init];
            vc.urlStr = @"http://bigjpg.com/htdocs/term.html";
            vc.webTitle = @"User Agreement";
            [self.navigationController pushViewController:vc animated:YES];
        }
            
            break;
        case 5:{
            //隐私协议
            BigjpgWebViewController *vc = [[BigjpgWebViewController alloc] init];
            vc.urlStr = @"https://bigjpg.com/htdocs/privacy_policy.html";
            vc.webTitle = @"Privacy Policy";
            [self.navigationController pushViewController:vc animated:YES];
        }
            
            break;
        case 6: {
            NSString *path = [[LqSandBox docPath] lq_subDirectory:@"pic"];
            NSFileManager *fileManager = [NSFileManager defaultManager];
            [fileManager removeItemAtPath:path error:nil];
            
            [[SDWebImageManager sharedManager].imageCache clearWithCacheType:SDImageCacheTypeAll completion:^{
                [LSVProgressHUD showSuccessWithStatus:LanguageStrings(@"succ")];
            }];
        }
            break;
            
        default:
            break;
    }
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return Adaptor_Value(60);
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

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    [self.view endEditing:YES];
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
        //高度自适应
        _customTableView.estimatedRowHeight=60;
        _customTableView.rowHeight=UITableViewAutomaticDimension;
        
        [_customTableView registerClass:[SettingCell class] forCellReuseIdentifier:NSStringFromClass([SettingCell class])];
        
        _customTableView.separatorStyle = UITableViewCellSeparatorStyleNone;

//        [_customTableView addHeaderWithRefreshingTarget:self refreshingAction:@selector(requestData)];
//        [_customTableView beginHeaderRefreshing];
        
        
    }
    return _customTableView;
}
- (SettingCustomView *)settingCustomView{
    if (!_settingCustomView) {
        _settingCustomView = [SettingCustomView new];
    }
    return _settingCustomView;
}

- (UIView *)footerView{
    if (!_footerView) {
        _footerView = [UIView new];
        __weak __typeof(self) weakSelf = self;
        _versionBtn = [[UIButton alloc] init];
        NSString *version = [LqToolKit appVersionNo];
        [_versionBtn setTitle:[NSString stringWithFormat:@"V%@",version] forState:UIControlStateNormal];
        [_versionBtn setTitleColor:TitleGrayColor forState:UIControlStateNormal];
        _versionBtn.titleLabel.font = AdaptedFontSize(15);
        [_footerView addSubview:_versionBtn];
        [_versionBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.footerView);
            make.height.mas_equalTo(Adaptor_Value(40));
            make.width.mas_equalTo(LQScreemW);
        }];
    }
    return _footerView;
}

- (CustomAlertView *)loginOutAlertView{
    if (!_loginOutAlertView) {
        _loginOutAlertView = [[CustomAlertView alloc] init];

        __weak __typeof(self) weakSelf = self;
        _loginOutAlertView.CustomAlertViewBlock = ^(NSInteger index,NSString *str){
            if (index == 1) {//取消
                
            }else if(index == 2){//确定
                [weakSelf logOut:nil];
            }
            [weakSelf.loginOutAlertView removeFromSuperview];
            weakSelf.loginOutAlertView = nil;
            
         };
    }
    return _loginOutAlertView;
}
@end
