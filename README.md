# JavaLearning
Java/Spring/SpringBoot/SpringCloud Knowledge
## 1. 准备工作：
### 1.1 下载安装配置JDK
official:
https://www.oracle.com/ua/java/technologies/downloads/

### 1.2 下载安装idea
official:
https://www.jetbrains.com/zh-cn/idea/

### 1.3 下载安装配置GIT
official:
https://git-scm.com/book/zh/v2/%E8%B5%B7%E6%AD%A5-%E5%AE%89%E8%A3%85-Git
https://git-scm.com/downloads/win

introduction:
https://blog.csdn.net/mukes/article/details/115693833

### 1.4 配置git与github
1.SSH
1) 生成SSH文件
ssh-keygen -t rsa -C "邮箱"
2) 配置本地公钥到github
点击github个人头像    ->  点击settings  ->  选择SSH and CPG keys  ->  输入复制的pubkey
3) 本地clone
略
2.Https
略

### 1.5 配置git 与 idea
打开idea  ->  设置  ->  版本控制    ->  选择git   ->  选择git安装目录瞎的bin/git.exe

### 1.6 配置github 与 idea
1) 打开idea  ->  设置  ->  版本控制    ->  选择github   ->  添加自己的账号
2) 在idea中用git进行提交本地仓，推送远端仓，管理远端仓等等。。。

### 1.7 配置setting文件
1)  阿里云镜像：
    https://maven.aliyun.com/nexus/content/groups/public