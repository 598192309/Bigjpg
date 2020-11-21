# bigjpg.com api

*host: https://bigjpg.com*

- 登陆/创建用户

```
#!python

url: host/login
method: post
data:
	- username 用户名
	- password 密码
	- not_reg 是否注册新用户, 如果有这个参数表示只判断用户是否存在，不注册新的用户；反之如果用户不存在就创建新用户

return, json:
	- {'status': 'error'} 参数错误
	- {'status': 'not_exist'} 用户不存在
	- {'status': 'password_error'} 密码错误
	- {
          'status': 'ok',
          'username': 用户名,
          'is_expire': 是否过期或者免费版用户
       }, 同时返回的还有Http Header里面的 Set-Cookie，需要把这个cookie存起来,以后的请求都要带上
```


- 用户信息查询


```
#!python

url: host/user
method: get

return, json:
	- {'status': 'no_login'} 没有登陆
	- {'status': 'not_exist'} 用户不存在
    - {
          'status': 'ok',
          'user': {
              'username': 用户名,
              'version': 用户等级(免费版 None, 基础版basic, 标准版std, 高级版pro),
              'expire': 过期时间,
              'is_expire': 是否过期或者免费用户
           },
          'logs': [{conf放大参数, output放大后地址, 创建时间, 状态, fid}, ], 用户放大记录
      }
```




- 用户登陆密码更新

```
#!python

url: host/user
method: post
data:
	- new 新密码

return, json:
	- {'status': 'no_login'} 没有登陆
    - {'status': 'ok'} 更新成功
```



- 重试放大任务

```
#!python

url: host/retry
method: get
data:
	- fids 任务id列表, json格式

return, json:
    - {'status': 'ok'} 重试成功
```


- 查询放大任务状态


```
#!python

url: host/free
method: get
data:
	- fids 任务id列表, json格式

return, json:
    - {fid: [状态, 放大后文件], } fid为放大任务id, 状态有 new, process, success, error ；放大后文件是阿里云的oss 地址
```



- 删除放大任务

```
#!python

url: host/free
method: delete
data:
	- fids 任务id列表, json格式

return, json:
    - {'status': 'ok'} 删除成功
```


- 创建放大任务状态（先通过阿里云 oss 上传文件得到url后再创建任务方式）

```
#!python

url: host/task
method: post
data:
	- conf  {
        'x2': 放大倍数 1 两倍, 2 四倍, 3 八倍, 4 十六倍,
        'style': art 卡通图片, photo 照片,
        'noise': 噪点 -1 无, 0 低, 1 中, 2 高, 3 最高,
        'file_name': 图片名,
        'files_size': 图片字节大小,
        'file_height': 图片高度,
        'file_width': 图片宽度
        'input': 图片oss地址
    } (json 格式)

return, json:
    - {'status': 'parallel_limit'} 免费版同时只能放大两张
	- {'status': 'month_limit'} 达到当月放大数量
	- {'status': 'param_error'} 参数错误
	- {'status': 'type_error'} 图片类型无法识别
	- {'status': 'size_limit'} 图片尺寸超过限制
	- {'status': 'ok', 'info': fid, 'time': 预估时间min}
```



- 获取语言配置、oss配置


```
#!python

url: host/conf
method: get

return, json:
    - {'lng_dict': json dict, 'app_oss_conf': json dict}
```





- **【已废弃】**创建订单(youzan支付), 用户查询接口来查询支付状态


```
#!python

url: host/youzan_order
method: get
data:
	- goods 商品类型 basic 基础版, std 标准版, pro 高级版；调用这个接口需要带上cookie

return, json:
    - {'status': 'ok', 'qr': base64 编码的youzan支付链接QR, qr_url: qr里面的内容有赞支付url}
```


- **【已废弃】**创建放大任务状态（直接上传文件方式）

```
#!python

url: host/free
method: post
data:
	- conf  {
        'x2': 放大倍数 1 两倍, 2 四倍, 3 八倍, 4 十六倍,
        'style': art 卡通图片, photo 照片,
        'noise': 噪点 -1 无, 0 低, 1 中, 2 高, 3 最高,
        'file_name': 图片名,
        'files_size': 图片字节大小,
        'file_height': 图片高度,
        'file_width': 图片宽度
    } (json 格式)
	- file 图片文件

return, json:
    - {'status': 'parallel_limit'} 免费版同时只能放大两张
	- {'status': 'month_limit'} 达到当月放大数量
	- {'status': 'param_error'} 参数错误
	- {'status': 'type_error'} 图片类型无法识别
	- {'status': 'size_limit'} 图片尺寸超过限制
	- {'status': 'ok', 'info': fid, 'time': 预估时间min}
```
