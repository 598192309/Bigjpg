//
//  CustomTableAlertView.m
//  Bigjpg
//
//  Created by rabi on 2019/12/25.
//  Copyright © 2019 lqq. All rights reserved.
//

#import "CustomTableAlertView.h"


@interface CustomTableAlertView() <UITableViewDelegate, UITableViewDataSource>

@property(nonatomic, strong) UITableView *customTableView;

@property (nonatomic, strong)UIView *customCoverView;

@property (nonatomic,strong)NSArray *dataArr;
@end

@implementation CustomTableAlertView
#pragma  mark - 生命周期
-(instancetype)init{
    if (self = [super init]) {
        [self configUI];
        
    }
    return self;
}


#pragma  mark - smzq

- (void)configUI{

    [self addSubview:self.customCoverView];
    __weak __typeof(self) weakSelf = self;
    [self.customCoverView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf);
    }];
    
    [self addSubview:self.customTableView];
    [self.customTableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.mas_equalTo(weakSelf);
        make.height.mas_equalTo(0);
        make.width.mas_equalTo(Adaptor_Value(250));
    }];
     
    ViewRadius(self.customTableView, Adaptor_Value(10));
}
- (void)configUIWithArr:(NSArray *)arr{
    self.dataArr = arr;
    [self.customTableView reloadData];
    [self.customTableView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(Adaptor_Value(50) * arr.count);
    }];
}

#pragma mark - act
- (void)removeTap:(UITapGestureRecognizer *)gest{
    if (self.CustomTableAlertRemoveBlock) {
        self.CustomTableAlertRemoveBlock();
    }
}

#pragma mark - uitableview delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArr.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return Adaptor_Value(50);
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CustomTableAlertCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([CustomTableAlertCell class])];
    [cell refreshUIWithTitle:[self.dataArr safeObjectAtIndex:indexPath.row]];
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.CustomTableAlertChooseBlock) {
        self.CustomTableAlertChooseBlock(indexPath.row, [self.dataArr safeObjectAtIndex:indexPath.row ]);
    }
}

#pragma mark - lazy
- (UITableView *)customTableView
{
    if (_customTableView == nil) {
        _customTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0,LQScreemW, LQScreemH) style:UITableViewStylePlain];
        _customTableView.delegate = self;
        _customTableView.dataSource = self;
        _customTableView.showsVerticalScrollIndicator = NO;
        _customTableView.showsHorizontalScrollIndicator = NO;
        _customTableView.backgroundColor = BackGroundColor;

        
        [_customTableView registerClass:[CustomTableAlertCell class] forCellReuseIdentifier:NSStringFromClass([CustomTableAlertCell class])];
        
        _customTableView.separatorStyle = UITableViewCellSeparatorStyleNone;

        
        
    }
    return _customTableView;
}
- (UIView *)customCoverView{
    if (!_customCoverView) {
        _customCoverView = [UIView new];
        _customCoverView.backgroundColor = [UIColor lq_colorWithHexString:@"#14181A" alpha:0.5];
        UITapGestureRecognizer *removeTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(removeTap:)];
        [_customCoverView addGestureRecognizer:removeTap];
    }
    return _customCoverView;
}
@end


@interface CustomTableAlertCell()
@property (nonatomic,strong) UIView * cellBackgroundView;
@property (strong, nonatomic)  UILabel *titleLabel;
@property (nonatomic,strong)UIView *lineView;

@end
@implementation CustomTableAlertCell

#pragma mark - 生命周期
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        [self configUI];
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.backgroundColor = BackGrayColor;

    }
    return self;
}


- (void)configUI{
    [self.contentView addSubview:self.cellBackgroundView];
    __weak __typeof(self) weakSelf = self;
    [self.cellBackgroundView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(weakSelf.contentView);
    }];

}
#pragma mark - ui
- (void)refreshUIWithTitle:(NSString *)title{
    self.titleLabel.text = title;
}

#pragma mark - act

#pragma mark - lazy
- (UIView *)cellBackgroundView{
    if (!_cellBackgroundView) {
        _cellBackgroundView = [UIView new];
        _cellBackgroundView.backgroundColor = BackGroundColor;
        UIView *contentV = [UIView new];
        __weak __typeof(self) weakSelf = self;
        [_cellBackgroundView addSubview:contentV];
        [contentV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(weakSelf.cellBackgroundView);
            make.height.mas_equalTo(Adaptor_Value(50));
        }];

        
        _titleLabel = [UILabel lableWithText:lqLocalized(@"",nil) textColor:TitleBlackColor fontSize:AdaptedFontSize(17) lableSize:CGRectZero textAliment:NSTextAlignmentCenter numberofLines:0];
        [contentV addSubview:_titleLabel];
        [_titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(20));
            make.centerY.mas_equalTo(contentV);
        }];
   
        _lineView = [UIView new];
        _lineView.backgroundColor = LineGrayColor;
        [contentV addSubview:_lineView];
        [_lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(Adaptor_Value(10));
            make.right.mas_equalTo(contentV).offset(-Adaptor_Value(10));
            make.bottom.mas_equalTo(contentV);
            make.height.mas_equalTo(kOnePX);
        }];

    }
    return _cellBackgroundView;
}



@end
