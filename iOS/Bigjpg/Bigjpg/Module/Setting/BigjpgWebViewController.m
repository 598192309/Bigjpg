//
//  BigjpgWebViewController.m
//  Bigjpg
//
//  Created by 黎芹 on 2020/1/20.
//  Copyright © 2020 lqq. All rights reserved.
//

#import "BigjpgWebViewController.h"
#import "SYProgressWebView.h"

@interface BigjpgWebViewController ()<SYProgressWebViewDelegate>

@property (nonatomic, strong)  SYProgressWebView *webView;

@end

@implementation BigjpgWebViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self setUI];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    if (self.webView)
    {
        [self.webView timerKill];
    }
}

- (void)loadView
{
    [super loadView];
    self.view.backgroundColor = [UIColor clearColor];
    if ([self respondsToSelector:@selector(setEdgesForExtendedLayout:)])
    {
        [self setEdgesForExtendedLayout:UIRectEdgeNone];
    }
}

- (void)dealloc
{
    self.webView = nil;
    
    NSLog(@"%@ 被释放了!!!", self);
}

#pragma mark - 创建视图

- (void)setUI
{
    [self addNavigationView];
    self.navigationTextLabel.text =_webTitle;

    
    [self webViewUIPush];

}

#pragma mark 网页视图

- (void)webViewUIPush
{
    NSString *url = self.urlStr;

    WeakWebView;
    // 方法1 实例化
//    self.webView = [[ZLCWebView alloc] initWithFrame:self.view.bounds];
    // 方法2 实例化
    self.webView = [[SYProgressWebView alloc] init];
    [self.view addSubview:self.webView];
    self.webView.frame = CGRectMake(0, NavMaxY, LQScreemW, LQScreemH - NavMaxY);
    self.webView.url = url;
    self.webView.isBackRoot = NO;
    self.webView.showActivityView = YES;
    self.webView.showActionButton = YES;
    [self.webView reloadUI];
    [self.webView loadRequest:^(SYProgressWebView *webView, NSString *title, NSURL *url) {
        NSLog(@"准备加载。title = %@, url = %@", title, url);
//        self.navigationTextLabel.text = title;
    } didStart:^(SYProgressWebView *webView) {
        NSLog(@"开始加载。");
    } didFinish:^(SYProgressWebView *webView, NSString *title, NSURL *url) {
        NSLog(@"成功加载。title = %@, url = %@", title, url);
//        self.navigationTextLabel.text = title;
    } didFail:^(SYProgressWebView *webView, NSString *title, NSURL *url, NSError *error) {
        NSLog(@"失败加载。title = %@, url = %@, error = %@", title, url, error);
//        self.navigationTextLabel.text = title;
    }];
}


#pragma mark - SYProgressWebViewDelegate

- (void)progressWebViewDidStartLoad:(SYProgressWebView *)webview
{
    NSLog(@"开始加载。");
}

- (void)progressWebView:(SYProgressWebView *)webview title:(NSString *)title shouldStartLoadWithURL:(NSURL *)url
{
    NSLog(@"准备加载。title = %@, url = %@", title, url);
//    self.navigationTextLabel.text = title;
}

- (void)progressWebView:(SYProgressWebView *)webview title:(NSString *)title didFinishLoadingURL:(NSURL *)url
{
    NSLog(@"成功加载。title = %@, url = %@", title, url);
//    self.navigationTextLabel.text = title;
}

- (void)progressWebView:(SYProgressWebView *)webview title:(NSString *)title didFailToLoadURL:(NSURL *)url error:(NSError *)error
{
    NSLog(@"失败加载。title = %@, url = %@, error = %@", title, url, error);
//    self.navigationTextLabel.text = title;
}
@end
