//
//  HistoryViewController.m
//  Bigjpg
//
//  Created by rabi on 2019/12/23.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "HistoryViewController.h"
#import "HistoryCustomView.h"
#import "HistoryCell.h"
#import "I_Account.h"
#import "I_Enlarge.h"

@interface HistoryViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (strong, nonatomic) UITableView  *customTableView;
@property (nonatomic,strong)HistoryCustomView *historyCustomView;

@property (nonatomic,strong)UIView *unloginFooter;
@property (nonatomic,strong)QMUILabel *unloginCheckLable;

@property (nonatomic,strong)CustomAlertView *infoAlert;

@property (nonatomic, strong) M_User *userInfo;
@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, assign) BOOL downAll;

@property (nonatomic,strong)NSMutableArray *dataArr;

@end

@implementation HistoryViewController
#pragma mark - 重写

#pragma mark - 生命周期

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
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notifyRequestData) name:kUserSignIn object:nil];
    //监听用户退出登录
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notifyRequestData) name:kUserSignOut object:nil];
    //监听用户切换语言
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(changLanguage) name:kChangeLanguageNotification object:nil];
    //切换夜间模式
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(changLanguage) name:kChangeNightNotification object:nil];

    //监听重试放大任务成功
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(retrySuccess) name:kRetrySuccessNoti object:nil];

    [self requestData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (_timer == nil) {
        self.timer = [NSTimer timerWithTimeInterval:10 target:self selector:@selector(requestData) userInfo:nil repeats:YES];
        [[NSRunLoop currentRunLoop] addTimer:self.timer forMode:NSDefaultRunLoopMode];
        [self.timer setFireDate:[NSDate date]];
    }
    if (!self.isFirstViewDidAppear) {
        [self requestData];
    }
}
- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self.timer invalidate];
    self.timer = nil;
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
    [self changeUIWithLoginStatus];
    
}
- (void)setUpHeader{
    if (_historyCustomView) {
        if (self.dataArr.count == 0 || !RI.is_logined) {//没有历史记录 不显示批量下载按钮
            [self.historyCustomView.downloadBtn mas_updateConstraints:^(MASConstraintMaker *make) {
                make.height.mas_equalTo(0);
                make.top.mas_equalTo(TopAdaptor_Value(30));

            }];

        }else{
            [self.historyCustomView.downloadBtn mas_updateConstraints:^(MASConstraintMaker *make) {
                make.height.mas_equalTo(40);
                make.top.mas_equalTo(Adaptor_Value(60));

            }];
        }
        CGFloat H = [self.historyCustomView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
        self.customTableView.tableHeaderView.lq_height = H;
    }else{
        UIView *tableHeaderView = [[UIView alloc] init];
        [tableHeaderView addSubview:self.historyCustomView];
        if (self.userInfo.historyList.count == 0 || !RI.is_logined) {//没有历史记录 不显示批量下载按钮
            [self.historyCustomView.downloadBtn mas_updateConstraints:^(MASConstraintMaker *make) {
                make.height.mas_equalTo(0);
                make.top.mas_equalTo(TopAdaptor_Value(30));

            }];

        }else{
            [self.historyCustomView.downloadBtn mas_updateConstraints:^(MASConstraintMaker *make) {
                make.height.mas_equalTo(40);
                make.top.mas_equalTo(Adaptor_Value(60));

            }];
        }
        [self.historyCustomView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(tableHeaderView);
        }];
        CGFloat H = [tableHeaderView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
        tableHeaderView.lq_height = H;
        self.customTableView.tableHeaderView = tableHeaderView;
        self.customTableView.tableHeaderView.lq_height = H;
        [self historyCustomViewAct];
    }




}
#pragma mark - refresh ui
- (void)changeUIWithLoginStatus{
    if (RI.is_logined) {
        if (_unloginFooter) {
            [_unloginFooter removeFromSuperview];
            _unloginFooter = nil;
            self.customTableView.tableFooterView = [UIView new];

        }
    }else{
        //footer
        if (!_unloginFooter) {
            UIView *tableFooterView = [[UIView alloc] init];
            [tableFooterView addSubview:self.unloginFooter];
            [self.unloginFooter mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.equalTo(tableFooterView);
            }];
            CGFloat H = [self.historyCustomView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
            tableFooterView.lq_height = LQScreemH - H -TabbarH;
            self.customTableView.tableFooterView = tableFooterView;
            self.customTableView.tableFooterView.lq_height =LQScreemH - H -TabbarH*2;
        }

    }
    [self.customTableView reloadData];
}
- (void)changLanguage{
    self.view.backgroundColor = BackGroundColor;
    self.customTableView.backgroundColor = BackGroundColor;
    [self.customTableView reloadData];
    [self.historyCustomView removeFromSuperview];
    self.historyCustomView = nil;
    [self setUpHeader];
    self.unloginCheckLable.text = LanguageStrings(@"no_upgrade");
    self.unloginFooter.backgroundColor = BackGroundColor;
    self.unloginCheckLable.textColor = RI.isNight ? TitleGrayColor : TitleBlackColor;
    self.unloginCheckLable.backgroundColor = RI.isNight ? BackGroundColor : TabbarGrayColor;

}

- (void)retrySuccess
{
    [self requestData];
}
#pragma mark - act

- (void)historyCustomViewAct{
    __weak __typeof(self) weakSelf = self;
    self.historyCustomView.historyCustomViewConfirmBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender) {
        if (RI.is_logined) {//批量下载
            //for循环 拿到被选中的items
            NSMutableArray *selectedArr = [NSMutableArray array];
            for (M_EnlargeHistory *item in weakSelf.userInfo.historyList) {
                if (item.customSlected) {
                    [selectedArr addObject:item];
                }
            }
            [weakSelf downAllWithArr:selectedArr sender:sender];
            
        }else{
            [weakSelf remindShow:nil msgColor:TitleBlackColor msgFont:AdaptedFontSize(15) subMsg:LanguageStrings(@"no_upgrade") submsgColor:nil submsgFont:AdaptedFontSize(17) firstBtnTitle:LanguageStrings(@"cancel") secBtnTitle:LanguageStrings(@"ok") singeBtnTitle:@"" removeBtnHidden:YES];
        }
        
    };
    
    //点击down ALL
    self.historyCustomView.historyCustomViewDownAllBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender) {
        weakSelf.downAll = YES;
        [weakSelf.customTableView reloadData];
    };
    
    //cancle
    self.historyCustomView.historyCustomViewCancleBtnClickBlock = ^(NSDictionary * _Nonnull dict, UIButton * _Nonnull sender) {
        weakSelf.downAll = NO;
        [weakSelf.customTableView reloadData];

    };
    
}

- (void)unloginTap:(UITapGestureRecognizer *)gest{
    self.tabBarController.selectedIndex = 2;
}

- (void)remindShow:(NSString *)msg msgColor:(UIColor *)msgColor msgFont:(UIFont *)msgFont subMsg:(NSString *)subMsg submsgColor:(UIColor *)submsgColor submsgFont:(UIFont *)submsgFont firstBtnTitle:(NSString *)firstBtnTitle secBtnTitle:(NSString *)secBtnTitle singeBtnTitle:(NSString *)singeBtnTitle removeBtnHidden:(BOOL)removeBtnHidden{
    NSAttributedString *attr = [msg lq_getAttributedStringWithLineSpace:Adaptor_Value(5) kern:Adaptor_Value(2)  ];

    [self.infoAlert refreshUIWithAttributeTitle:attr titleColor:msgColor titleFont:msgFont titleAliment:NSTextAlignmentCenter attributeSubTitle:[[NSAttributedString alloc]initWithString:SAFE_NIL_STRING(subMsg) ] subTitleColor:submsgColor subTitleFont:submsgFont subTitleAliment:NSTextAlignmentCenter firstBtnTitle:firstBtnTitle firstBtnTitleColor:TitleGrayColor secBtnTitle:secBtnTitle secBtnTitleColor:TitleBlackColor singleBtnHidden:singeBtnTitle.length == 0 singleBtnTitle:singeBtnTitle singleBtnTitleColor:nil removeBtnHidden:removeBtnHidden];
    [[UIApplication sharedApplication].keyWindow addSubview:self.infoAlert];
    
    [self.infoAlert mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo([UIApplication sharedApplication].keyWindow);
    }];
}
#pragma mark - net
- (void)requestData{
    if (RI.is_logined) {
        __weak __typeof(self) weakSelf = self;
        [I_Account getUserInfoOnSuccess:^(M_User * _Nonnull userInfo) {
            if (!weakSelf.downAll) {
                weakSelf.userInfo = userInfo;
                weakSelf.dataArr = [NSMutableArray arrayWithArray:userInfo.historyList];
                 [weakSelf.customTableView reloadData];
            }
            [self setUpHeader];

        } failure:^(NSError *error) {
            if (weakSelf.userInfo == nil) {
                [LSVProgressHUD showError:error];
            }
        }];
    }else{
        [self setUpHeader];
    }
   
}
//接收通知 请求数据
- (void)notifyRequestData{
    [self changeUIWithLoginStatus];
    if (RI.is_logined) {
        [self requestData];
    }else{
        [self.customTableView reloadData];
    }
}
//删除放大任务
- (void)removeTaskWithIdsArr:(NSArray*)ids withIndex:(NSInteger)index{
    [LSVProgressHUD show];
    __weak __typeof(self) weakSelf = self;
    [I_Enlarge deleteEnlargeTasks:ids success:^{
        [LSVProgressHUD showInfoWithStatus:LanguageStrings(@"succ")];
        [weakSelf.userInfo.historyList removeObjectAtIndex:index];
        [weakSelf.customTableView reloadData];
    } failure:^(NSError *error) {
        [LSVProgressHUD showError:error];
    }];
}

//批量下载
- (void)downAllWithArr:(NSMutableArray *)arr sender:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    sender.userInteractionEnabled = NO;
    weakSelf.downAll = NO;
    [weakSelf.customTableView reloadData];
    [weakSelf.historyCustomView reset];
    
    NSMutableArray *urlList = [NSMutableArray array];
    for (M_EnlargeHistory *item in arr) {
        [urlList safeAddObject:item.output];
    }
    
    [I_Enlarge downloadPictureWithUrls:urlList isAutoDown:NO];
}

#pragma mark -  UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return RI.is_logined ? self.dataArr.count : 0;

}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HistoryCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([HistoryCell class]) forIndexPath:indexPath];
    UIColor *color = BackGroundColor;
    if (indexPath.row % 2 == 0) {
        color = BackGroundColor;
    }else{
        color = TabbarGrayColor;
    }
    [cell configUIWithItem:[self.dataArr safeObjectAtIndex:indexPath.row] downAll:self.downAll backColor:color];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.downAll) {
        M_EnlargeHistory *item = [self.userInfo.historyList safeObjectAtIndex:indexPath.row];
        item.customSlected = !item.customSlected;
        [self.customTableView reloadData];

    }
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 110;
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
-(NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return LanguageStrings(@"del");
}
-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.downAll) {
        return UITableViewCellEditingStyleNone;
    }
    return UITableViewCellEditingStyleDelete;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    __weak __typeof(self) weakSelf = self;
    [SystemAlertViewController alertViewControllerWithTitle:nil message:LanguageStrings(@"sure") cancleButtonTitle:LanguageStrings(@"cancel") commitButtonTitle:LanguageStrings(@"ok") cancleBlock:^{
        
      } commitBlock:^{
          M_EnlargeHistory *item = [self.userInfo.historyList safeObjectAtIndex:indexPath.row];
          [weakSelf removeTaskWithIdsArr:@[item.fid] withIndex:indexPath.row];
          [weakSelf.dataArr removeObject:item];
          [weakSelf setUpHeader];
      }];
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
        
        [_customTableView registerClass:[HistoryCell class] forCellReuseIdentifier:NSStringFromClass([HistoryCell class])];
        
        _customTableView.separatorStyle = UITableViewCellSeparatorStyleNone;

//        [_customTableView addHeaderWithRefreshingTarget:self refreshingAction:@selector(requestData)];
//        [_customTableView beginHeaderRefreshing];
        
        
    }
    return _customTableView;
}
- (HistoryCustomView *)historyCustomView{
    if (!_historyCustomView) {
        _historyCustomView = [HistoryCustomView new];
    }
    return _historyCustomView;
}


- (UIView *)unloginFooter{
    if (!_unloginFooter) {
        _unloginFooter = [UIView new];
        _unloginFooter.backgroundColor = BackGroundColor;

        UIView *contentV = [UIView new];
        contentV.backgroundColor = [UIColor clearColor];
        [_unloginFooter addSubview:contentV];
        __weak __typeof(self) weakSelf = self;
        
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.unloginFooter);
        }];
        
      
    
        _unloginCheckLable = [[QMUILabel alloc] init];
        _unloginCheckLable.text = LanguageStrings(@"no_upgrade");
        _unloginCheckLable.textColor = TitleBlackColor;
        _unloginCheckLable.font = AdaptedFontSize(15);
        _unloginCheckLable.textAlignment = NSTextAlignmentCenter;
        _unloginCheckLable.numberOfLines = 0;
        _unloginCheckLable.contentEdgeInsets = UIEdgeInsetsMake(10, 20, 10, 20);
        [contentV addSubview:_unloginCheckLable];
        
        [_unloginCheckLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.mas_equalTo(contentV);
            make.width.mas_lessThanOrEqualTo(LQScreemW - Adaptor_Value(50));
        }];
        ViewBorderRadius(_unloginCheckLable, 4, kOnePX*2, LineGrayColor);
        _unloginCheckLable.backgroundColor =  RI.isNight ? BackGroundColor : TabbarGrayColor;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(unloginTap:)];
        _unloginCheckLable.userInteractionEnabled = YES;
        [_unloginCheckLable addGestureRecognizer:tap];
        
        
    }
    return _unloginFooter;
}

- (CustomAlertView *)infoAlert{
    if (_infoAlert == nil) {
        _infoAlert = [[CustomAlertView alloc] init];

        __weak __typeof(self) weakSelf = self;
        UITabBarController *rootVC  = (UITabBarController *)[UIApplication sharedApplication].keyWindow.rootViewController;
        NSInteger a = rootVC.selectedIndex;
        UINavigationController *mineVC = [rootVC.childViewControllers safeObjectAtIndex:rootVC.selectedIndex];
        UIViewController *currentVc = mineVC.viewControllers.lastObject;
    
        _infoAlert.CustomAlertViewBlock = ^(NSInteger index,NSString *str){
            if (index == 1) {//取消
                
            }else if(index == 2){//确定
                weakSelf.tabBarController.selectedIndex = 2;
                [weakSelf.historyCustomView reset];

            }else if(index == 4){
                
            }
            [weakSelf.infoAlert removeFromSuperview];
            weakSelf.infoAlert = nil;
            
        };
    }
    
    return _infoAlert;
    
}
@end
