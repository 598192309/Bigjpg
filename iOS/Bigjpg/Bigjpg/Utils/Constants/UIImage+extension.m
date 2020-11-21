//
//  UIImage+extension.m
//  Bigjpg
//
//  Created by 黎芹 on 2020/1/13.
//  Copyright © 2020 lqq. All rights reserved.
//

#import "UIImage+extension.h"



@implementation UIImage (extension)

// 把类加载进内存的时候调用,只会调用一次
+ (void)load
{
    // self -> UIImage
    // 获取imageNamed
    // 获取哪个类的方法
    // SEL:获取哪个方法
    Method imageNamedMethod = class_getClassMethod(self, @selector(imageNamed:));
    // 获取xmg_imageNamed
    Method lq_imageNamedMethod = class_getClassMethod(self, @selector(imageWithName:));

    // 交互方法:runtime
    method_exchangeImplementations(imageNamedMethod, lq_imageNamedMethod);
    // 调用imageNamed => xmg_imageNamedMethod
    // 调用xmg_imageNamedMethod => imageNamed
}
// 既能加载图片又能打印

+ (instancetype)imageWithName:(NSString *)name

{
    if ([name isEqualToString:@"zl_navBack"]) {
        if (RI.isNight) {
            return [[UIImage imageWithName:@"back"] qmui_imageWithTintColor:[UIColor whiteColor]];
            
        } else {
            return [[UIImage imageWithName:@"back"] qmui_imageWithTintColor:RGB(31, 31, 31)];
        }
    }
    // 这里调用imageWithName，相当于调用imageName

    UIImage *image = [self imageWithName:name];

    return image;
}
@end
