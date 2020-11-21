//
//  EnlargeConfViewController.m
//  Bigjpg
//
//  Created by lqq on 2019/12/30.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "EnlargeConfViewController.h"

@interface EnlargeConfViewController ()
@property (strong, nonatomic) IBOutlet UILabel *typeLabel;
@property (strong, nonatomic) IBOutlet UISegmentedControl *typeSegment;

@property (strong, nonatomic) IBOutlet UILabel *enlargeFactorLabel;
@property (strong, nonatomic) IBOutlet UISegmentedControl *ennargeFactorSegment;

@property (strong, nonatomic) IBOutlet UILabel *denoiseLabel;
@property (strong, nonatomic) IBOutlet UISegmentedControl *denoiseSegment;

@property (strong, nonatomic) IBOutlet UIButton *comfirBtn;
@property (strong, nonatomic) IBOutlet UIButton *confimAllBtn;


@property (nonatomic, strong) NSArray<M_EnlargeUpload *> *enlargeUploads;

@end

@implementation EnlargeConfViewController

+ (instancetype)controllerWithEnlargeUploads:(NSArray<M_EnlargeUpload *> *)enlargeUploads
{
    EnlargeConfViewController *vc = [UIStoryboard lq_controllerWithStoryBoardIdentify:@"EnlargeConfViewController" inStoryBoard:@"Home"];
    vc.enlargeUploads = enlargeUploads;
    
    return vc;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    [self addNavigationView];
    self.navigationTextLabel.text = LanguageStrings(@"configure");
    
    
    UIFont *font = [UIFont systemFontOfSize:16];
    UIColor *selectColor = [UIColor whiteColor];
    UIColor *normalColor = RI.isNight ? TitleGrayColor : [UIColor lq_colorWithHexString:@"4A4A4A"];
    UIColor *disableColor = RI.isNight ? RGB(85, 85, 85) : [UIColor lq_colorWithHexString:@"ABABAB"];
    UIImage *selectImage = RI.isNight ? [UIImage qmui_imageWithColor:RGB(30,30,30) size:CGSizeMake(200, 32) cornerRadius:4] : [UIImage qmui_imageWithColor:[UIColor lq_colorWithHexString:@"EEEEEF"] size:CGSizeMake(200, 32) cornerRadius: 4];
    NSArray<UISegmentedControl *> *arr = @[_typeSegment,_ennargeFactorSegment,_denoiseSegment];
       for (UISegmentedControl *seg in arr) {
           seg.tintColor = RI.isNight ? RGB(40,40,40) : RGB(220,220,220);
            [seg setTitleTextAttributes:@{NSForegroundColorAttributeName:selectColor,NSFontAttributeName:font} forState:UIControlStateSelected];
           [seg setTitleTextAttributes:@{NSForegroundColorAttributeName:normalColor,NSFontAttributeName:font} forState:UIControlStateNormal];
            [seg setTitleTextAttributes:@{NSForegroundColorAttributeName:disableColor,NSFontAttributeName:font} forState:UIControlStateDisabled];
           [seg setBackgroundImage:selectImage forState:UIControlStateNormal barMetrics:UIBarMetricsDefault];
           [seg setBackgroundImage:[UIImage qmui_imageWithColor:[UIColor lq_colorWithHexString:@"1AAC19"] size:CGSizeMake(200, 32) cornerRadius: 4] forState:UIControlStateSelected barMetrics:UIBarMetricsDefault];
       }
    
    _typeLabel.text = LanguageStrings(@"pic_type");
    _typeLabel.textColor = RI.isNight ? TitleGrayColor : [UIColor lq_colorWithHexString:@"4a4a4a"];
    [_typeSegment setTitle:LanguageStrings(@"carton") forSegmentAtIndex:0];
    [_typeSegment setTitle:LanguageStrings(@"photo") forSegmentAtIndex:1];

    
    
    _enlargeFactorLabel.text = LanguageStrings(@"upscaling");
    _enlargeFactorLabel.textColor = RI.isNight ? TitleGrayColor : [UIColor lq_colorWithHexString:@"4a4a4a"];;

    _denoiseLabel.text = LanguageStrings(@"noise");
    _denoiseLabel.textColor = RI.isNight ? TitleGrayColor : [UIColor lq_colorWithHexString:@"4a4a4a"];;

    [_denoiseSegment setTitle:LanguageStrings(@"none") forSegmentAtIndex:0];
    [_denoiseSegment setTitle:LanguageStrings(@"low") forSegmentAtIndex:1];
    [_denoiseSegment setTitle:LanguageStrings(@"mid") forSegmentAtIndex:2];
    [_denoiseSegment setTitle:LanguageStrings(@"high") forSegmentAtIndex:3];
    [_denoiseSegment setTitle:LanguageStrings(@"highest") forSegmentAtIndex:4];
    
    [_comfirBtn setTitle:LanguageStrings(@"ok") forState:UIControlStateNormal];
   
    if (_enlargeUploads.count > 1) {
        _confimAllBtn.hidden = NO;
         [_confimAllBtn setTitle:LanguageStrings(@"batch_btn") forState:UIControlStateNormal];
    } else {
        _confimAllBtn.hidden = YES;
    }
    _comfirBtn.layer.cornerRadius = 4;
    _comfirBtn.layer.masksToBounds = YES;
    
    ViewBorderRadius(_confimAllBtn, 4, kOnePX, RI.isNight ? TitleGrayColor : LineGrayColor);
    [_confimAllBtn setTitleColor:RI.isNight ? TitleGrayColor : [UIColor lq_colorWithHexString:@"4a4a4a"] forState:UIControlStateNormal];
    _confimAllBtn.backgroundColor = RI.isNight ? RGB(31, 31, 31) : [UIColor lq_colorWithHexString:@"EEEEEF"];;

    self.view.backgroundColor = BackGroundColor;
    NSArray *legelarr = [ConfManager.shared contentWith:@"version"];
    NSLog(@"RI.userInfo.version----%@",RI.userInfo.version);
    if (!RI.is_logined || [RI.userInfo.version isEqualToString:@"free"] || RI.userInfo.is_expire) {//免费 或者过期
        [self.ennargeFactorSegment setEnabled:NO forSegmentAtIndex:2];
        [self.ennargeFactorSegment setEnabled:NO forSegmentAtIndex:3];
    }else{
        [self.ennargeFactorSegment setEnabled:YES forSegmentAtIndex:2];
        [self.ennargeFactorSegment setEnabled:YES forSegmentAtIndex:3];
    }
    
}

//确定//批量放大
- (IBAction)confimEvent:(UIButton *)sender {
    M_EnlargeConf *conf = [[M_EnlargeConf alloc] init];
    if (_typeSegment.selectedSegmentIndex == 0) {
        conf.style = @"art";
    } else {
        conf.style = @"photo";
    }
    conf.x2 = (int)_ennargeFactorSegment.selectedSegmentIndex + 1;
    conf.noise = (int)_denoiseSegment.selectedSegmentIndex - 1;
    
    NSDictionary *dic = @{@"conf":conf,
                          @"enlargeBatch":@(sender.tag),
                          @"uploads":_enlargeUploads
    };
    
    [[NSNotificationCenter defaultCenter] postNotificationName:kEnlargeConfigarationFinishNoti object:dic];
    [self.navigationController popViewControllerAnimated:YES];
}



@end
