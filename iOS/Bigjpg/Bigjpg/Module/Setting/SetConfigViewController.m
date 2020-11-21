//
//  SetConfigViewController.m
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "SetConfigViewController.h"
#import "SetConfigChooseCell.h"
#import "CustomTableAlertView.h"

@interface SetConfigViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (strong, nonatomic) UITableView  *customTableView;

@property (nonatomic,strong)UIView *footer;
@property (nonatomic,strong)UILabel *tipLable;
@property (nonatomic,strong)UIButton *languageBtn;

@property (nonatomic,strong)CustomTableAlertView *customTableAlertView;
@end

@implementation SetConfigViewController
#pragma mark - 重写
-(UIStatusBarStyle)preferredStatusBarStyle {
    return RI.isNight ? UIStatusBarStyleLightContent : UIStatusBarStyleDefault ;
}
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
    UIView *tableFooterView = [[UIView alloc] init];
    [tableFooterView addSubview:self.footer];
    [self.footer mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(tableFooterView);
    }];
    CGFloat H = [tableFooterView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height;
    tableFooterView.lq_height = H;
    self.customTableView.tableFooterView = tableFooterView;
    self.customTableView.tableFooterView.lq_height = H;
}
- (void)setUpNav{
    [self addNavigationView];
    self.navigationTextLabel.text = LanguageStrings(@"conf");
}

#pragma mark - act
- (void)languageBtnClick:(UIButton *)sender{
    __weak __typeof(self) weakSelf = self;
    [[UIApplication sharedApplication].keyWindow addSubview:self.customTableAlertView];
    [self.customTableAlertView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo([UIApplication sharedApplication].keyWindow);
    }];
    [self.customTableAlertView configUIWithArr:@[@"简体中文",@"繁體中文",@"日本語",@"English",@"Русский",@"Türkçe",@"Deutsch"]];
    self.customTableAlertView.CustomTableAlertChooseBlock = ^(NSInteger index, NSString * _Nonnull str) {
        [weakSelf.customTableAlertView removeFromSuperview];
        weakSelf.customTableAlertView = nil;
        NSString *language;
        switch (index) {
            case 0:
                language = @"zh";
                break;
            case 1:
                language = @"tw";
                break;
            case 2:
                language = @"jp";
                break;
            case 3:
                language = @"en";
                break;
            case 4:
                language = @"ru";
                break;
            case 5:
                language = @"tr";
                break;
            case 6:
                language = @"de";
                break;
            default:
                break;
        }
        [[ConfManager shared] changeLocalLanguage:language];
        [weakSelf.languageBtn setTitle:str forState:UIControlStateNormal];
        [weakSelf.customTableView reloadData];
        weakSelf.tipLable.text = LanguageStrings(@"language");
        weakSelf.navigationTextLabel.text = LanguageStrings(@"conf");
        NSArray<UITabBarItem *> *items = weakSelf.tabBarController.tabBar.items;
        NSArray *titles = @[LanguageStrings(@"begin"),LanguageStrings(@"log"),LanguageStrings(@"conf")];
        for (int i = 0 ; i < 3; i++) {
            UITabBarItem *item = [items safeObjectAtIndex:i];
            item.title = [titles safeObjectAtIndex:i];
        }
        
    };
    
    self.customTableAlertView.CustomTableAlertRemoveBlock = ^{
        [weakSelf.customTableAlertView removeFromSuperview];
        weakSelf.customTableAlertView = nil;
    };
}
#pragma mark - net
- (void)requestData{
    
}

#pragma mark -  UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    return 2;

}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SetConfigChooseCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([SetConfigChooseCell class]) forIndexPath:indexPath];
    if (indexPath.row ==0) {
        [cell configUIWithTitle:LanguageStrings(@"save_dir") selected:RI.autoDownImage];
    }else{
        [cell configUIWithTitle:LanguageStrings(@"night_mode") selected:RI.isNight];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
   
    if (indexPath.row == 1) {//开启关闭 night模式
        RI.isNight = !RI.isNight;
        [[NSNotificationCenter defaultCenter] postNotificationName:kChangeNightNotification object:nil];
        
        [self.customTableView reloadData];
        self.customTableView.backgroundColor = BackGroundColor;
        self.footer.backgroundColor = BackGroundColor;
        [self.languageBtn setTitleColor:TitleBlackColor forState:UIControlStateNormal];
        [self setNeedsStatusBarAppearanceUpdate];
        [SVProgressHUD setDefaultStyle:RI.isNight?SVProgressHUDStyleLight:SVProgressHUDStyleDark];
    }else{
        RI.autoDownImage = !RI.autoDownImage;
    }
    
    //改变数据源
    [self.customTableView reloadData];
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
        
        [_customTableView registerClass:[SetConfigChooseCell class] forCellReuseIdentifier:NSStringFromClass([SetConfigChooseCell class])];
        
        _customTableView.separatorStyle = UITableViewCellSeparatorStyleNone;


        
        
    }
    return _customTableView;
}

- (UIView *)footer{
    if (!_footer) {
        _footer = [UIView new];
        _footer.backgroundColor = BackGroundColor;

        UIView *contentV = [UIView new];
        contentV.backgroundColor = [UIColor clearColor];
        [_footer addSubview:contentV];
        __weak __typeof(self) weakSelf = self;
        
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.footer);
        }];
        
        _tipLable = [UILabel lableWithText:LanguageStrings(@"language") textColor:TitleGrayColor fontSize:AdaptedFontSize(17) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_tipLable];
        [_tipLable mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(25));
            make.top.mas_equalTo(Adaptor_Value(10));
        }];
        
        _languageBtn = [[UIButton alloc] init];
        [_languageBtn setTitle:LanguageStrings(@"lng") forState:UIControlStateNormal];
        [_languageBtn setTitleColor:TitleBlackColor forState:UIControlStateNormal];
        [_languageBtn addTarget:self action:@selector(languageBtnClick:) forControlEvents:UIControlEventTouchUpInside];
        [contentV addSubview:_languageBtn];
        [_languageBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(weakSelf.tipLable);
            make.top.mas_equalTo(weakSelf.tipLable.mas_bottom).offset(Adaptor_Value(15));
            
            make.bottom.mas_equalTo(contentV);
        }];
        
    }
    return _footer;
}

- (CustomTableAlertView *)customTableAlertView{
    if (!_customTableAlertView) {
        _customTableAlertView = [CustomTableAlertView new];
    }
    return _customTableAlertView;
}
@end
