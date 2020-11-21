//
//  LaunchingViewController.m
//  IUang
//
//  Created by jayden on 2018/4/24.
//  Copyright © 2018年 jayden. All rights reserved.
//

#import "LaunchingViewController.h"
#import "NewFeatureScrollView.h"

@interface LaunchingViewController ()<CLLocationManagerDelegate>
@property(nonatomic,strong)UIImageView *imageView;

@end

@implementation LaunchingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    UIImageView *backView = [[UIImageView alloc] initWithFrame:self.view.frame];
    [self.view addSubview:backView];
    backView.center = self.view.center;
    _imageView = backView;
    backView.userInteractionEnabled = YES;

    backView.contentMode = UIViewContentModeScaleAspectFit;
    self.view.backgroundColor = [UIColor whiteColor];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    NSArray *imageNames = @[@"ic_launch_screen"];
    [NewFeatureScrollView leadPageViewWithImageNames:imageNames];

}
- (void)dealloc{
    NSLog(@"%@", [NSString stringWithFormat:@"dealloc ---- %@",NSStringFromClass([self class])]);
    
}
#pragma mark - act

#pragma  mark - 自定义

#pragma mark - lazy

@end
