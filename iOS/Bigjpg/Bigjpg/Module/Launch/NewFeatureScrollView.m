//
//  NewFeatureScrollView.m
//  Encropy
//
//  Created by Lqq on 2019/6/24.
//  Copyright © 2019 Lq. All rights reserved.
//

#import "NewFeatureScrollView.h"
#import "LoginViewController.h"


@interface NewFeatureScrollView () <UIScrollViewDelegate>

/** 滚动图片的`ScrollView` */
@property (nonatomic, strong) UIScrollView *mainScrollView;

/** `跳过` 按钮 */
//@property (nonatomic, strong) UIButton *skipBtn;
/** 存放图片名称是数组 */
@property (nonatomic, strong) NSMutableArray <NSString *> *imageNames;
/** 存放images数组 */
@property (nonatomic, strong) NSMutableArray <UIImage *> *images;


@property (nonatomic, assign) NSInteger currIndex;


@end
@implementation NewFeatureScrollView

- (instancetype)initWithFrame:(CGRect)frame imageNames:(NSArray *)imageNames {
    NSAssert(imageNames, @"imageNames can not be nil.");
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor lq_colorWithHexString:@"#09100f"];
        self.imageNames = [NSMutableArray arrayWithArray:imageNames];
        /** 处理传进来的imageNames */
        [self checkImageNames];
        
        [self addSubview:self.mainScrollView];
                
        [self configScrollViewImages];
      
        __weak __typeof(self) weakSelf = self;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^(void){
            [weakSelf disMissLeadPage];
        });

    }
    return self;
}

- (void)dealloc{
//    // 判断下用户有没有最新的版本
//    NSDictionary *dict =  [NSBundle mainBundle].infoDictionary;
//
//    // 获取最新的版本号
//    NSString *curVersion = dict[@"CFBundleShortVersionString"];
//
//    // 获取上一次的版本号
//    NSString *lastVersion = [[NSUserDefaults standardUserDefaults] objectForKey:VersionKey];
//
//    // 之前的最新的版本号 lastVersion
//    if (![curVersion isEqualToString:lastVersion]) {
//        [[NSUserDefaults standardUserDefaults] setObject:curVersion forKey:VersionKey];
//    }
}

/**
 检查传入imageNames, 如果通过名称找不到图片, 移除.
 */
- (void)checkImageNames {
    self.images = [NSMutableArray array];
    for (NSString *imageName in self.imageNames) {
        UIImage *image = [UIImage imageNamed:imageName];
        if (image) {
            [self.images safeAddObject:image];
        }
    }
}

/** 传入图片名称数组 */
+ (void)leadPageViewWithImageNames:(NSArray *)imageNames {
    NewFeatureScrollView *leadPage = [[self alloc] initWithFrame:[UIScreen mainScreen].bounds imageNames:imageNames];
    UIWindow *keyWindow = [UIApplication sharedApplication].keyWindow ;
    [keyWindow addSubview:leadPage];


}


- (void)configScrollViewImages {
    
    NSAssert(self.images.count, @"self.images is empty, check imageNames.");
    
    _mainScrollView.contentSize = CGSizeMake(self.images.count * self.width, self.height);
    for (int i = 0; i < self.images.count; ++i) {
        UIImage *image = self.images[i];
        UIImageView *imageV = [[UIImageView alloc] initWithImage:image];
        imageV.frame = CGRectMake(i * self.width, 0, self.width, self.height);
        imageV.contentMode = UIViewContentModeScaleAspectFit;
        [self.mainScrollView addSubview:imageV];
    }

}

- (CGFloat)height {
    return self.frame.size.height;
}

- (CGFloat)width {
    return self.frame.size.width;
}




#pragma mark - act
/** 点击跳过后执行方法 */
- (void)disMissLeadPage {
    [UIView animateWithDuration:1.3f animations:^{
        self.transform = CGAffineTransformMakeScale(1.2f, 1.2f);
        self.alpha = 0;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
        UIViewController *vc2 = [UIViewController topViewController];
        if ( ![[UIApplication sharedApplication].keyWindow.rootViewController isKindOfClass:[MainTabBarController class]] && ! [vc2 isKindOfClass:[LoginViewController class]]) {
            MainTabBarController *main = [[MainTabBarController alloc] init];
            APPDelegate.window.rootViewController = main ;
        }
        
    }];
}
//翻页
- (void)nextBtnClick:(UIButton *)sender{
    [self.mainScrollView setContentOffset:CGPointMake((self.currIndex + 1) *LQScreemW, self.mainScrollView.contentOffset.y) animated:YES];
    
}

#pragma mark - /**************** scrollView delegate ****************/
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    NSInteger currentIndex = scrollView.contentOffset.x / LQScreemW;
    BOOL isHidden = (self.images.count-1 == currentIndex);



}

- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView {
    [self scrollViewDidEndDecelerating:scrollView];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    CGFloat offsetX = scrollView.contentOffset.x;
    NSInteger currentIndex = (offsetX + LQScreemW/2) / LQScreemW;
    self.currIndex = currentIndex;
    /** scrollView 向左拖动大于100, 进入首页 */
    if (scrollView.contentOffset.x > LQScreemW * (self.images.count-1)+100) {
        [self disMissLeadPage];
    }
}


#pragma mark - /**************** lazy load ****************/
- (UIScrollView *)mainScrollView {
    if (_mainScrollView == nil) {
        _mainScrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
        _mainScrollView.contentInset = UIEdgeInsetsZero;
        _mainScrollView.delegate = self;
        _mainScrollView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        _mainScrollView.backgroundColor = BackGroundColor;
        _mainScrollView.showsHorizontalScrollIndicator = NO;
        _mainScrollView.showsVerticalScrollIndicator = NO;
        _mainScrollView.pagingEnabled = YES;
        //_mainScrollView.bounces = NO;
    }
    return _mainScrollView;
}


@end
