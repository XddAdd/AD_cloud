# SUST@Add

# AD_cloud
web端个人云盘，前端是裸界面，纯粹是显示数据，连前端显示下载进度都是显示的byte单位的。

# 功能
1. 用户注册登录
2. 文件上传和下载(支持大文件的分块上传)
3. 文件秒传(目前是用内存的hashmap，后面会改成redis)
4. 异步创建文件夹
5. 异步目录回退上一级
6. 输入http下载连接，后台自动多线程下载到云盘指定目录(当时为了方便测试，做的单线程，后面懒得改了，可以自行百度demo看下就行)
7. 前端实时显示后台自动下载的进度

# 前端
HTML，CSS，Javascript，Jquery

# 后端
Java

# 数据库
MySQL

# 文件存储
本机

# 初始化步骤
1. 创建数据库，执行cloud.sql
2. DBUtil，修改数据库连接地址和账号密码
3. FileUtil，设置云盘的ROOT目录
4. 启动项目，执行Main的main方法开启文件任务下载线程

