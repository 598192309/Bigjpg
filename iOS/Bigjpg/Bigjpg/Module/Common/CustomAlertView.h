//
//  CustomAlertView.h
//  RabiBird
//
//  Created by 拉比鸟 on 17/1/4.
//  Copyright © 2017年 Lq. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CustomAlertView : UIView
@property(nonatomic,copy)void(^CustomAlertViewBlock)(NSInteger index,NSString *str);

-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor;

-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor removeBtnHidden:(BOOL)removeBtnHidden;

-(void)refreshUIWithAttributeTitle:(NSAttributedString *)attributeTitle titleColor:(UIColor *)titleColor titleFont:(UIFont*)titleFont  titleAliment:(NSTextAlignment)titleAliment attributeSubTitle:(NSAttributedString *)attributeSubTitle  subTitleColor:(UIColor *)subTitleColor subTitleFont:(UIFont*)subTitleFont  subTitleAliment:(NSTextAlignment)subTitleAliment firstBtnTitle:(NSString *)firstBtnTitle firstBtnTitleColor:(UIColor *)firstBtnTitleColor secBtnTitle:(NSString *)secBtnTitle secBtnTitleColor:(UIColor *)secBtnTitleColor singleBtnHidden:(BOOL)hidden singleBtnTitle:(NSString*)singleBtnTitle singleBtnTitleColor:(UIColor *)singleBtnTitleColor removeBtnHidden:(BOOL)removeBtnHidden autoHeight:(BOOL)autoHeight;


@property (nonatomic, strong)NSString *titleStr;

@end
