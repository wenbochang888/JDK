# 安装MySql的奔溃史 2017年8月1日19:49:36

最近闲来无聊，买了一台阿里云的服务器。学生价9.9（一个月 嘻嘻嘻）。

因为学了一段时间的javaweb了，就想在服务器上面部署一下自己的网页。其中用到了MySql
数据库，于是乎搞着搞着一下午就没了。趁现在有空记录一下，以免以后踩更多的坑。

说一下遇到的问题。阿里云有自带的MySql 5.7.16版本。因为没有密码所以进不去MySql里面，
尝试过mysql -u -p 无密码直接进去，无奈是进不去滴（你也甭想了）
![Image text](https://github.com/wenbochang888/DiaryRecord/blob/master/img/MySqlVersion.jpg)

# reset MySql 密码如下步骤

1：首先打开（vi /etc/my.cnf）MySql的配置文件 如下。加上这一行 skip-grant-tables。
这个作用是不用输入用户名和密码就可以进去。
![Image text](https://github.com/wenbochang888/DiaryRecord/blob/master/img/SettingSql.jpg)


2：service mysqld restart(重要，负责配置文件不生效)。然后命令 mysql直接进去MySQl,
此时已经不需要用户名和密码了。

3：use mysql(进入mysql数据库) 可以select * from user where users = 'root';看一下

4：注意看下面的图片。官方文档的解释。

MySQL 5.7.6 and later:
ALTER USER 'root'@'localhost' IDENTIFIED BY 'MyNewPass';

MySQL 5.7.5 and earlier:
SET PASSWORD FOR 'root'@'localhost' = PASSWORD('MyNewPass');

5：加入第四步出错，刷新一下权限。flush privileges; 在继续执行第四部。
![Image text](https://github.com/wenbochang888/DiaryRecord/blob/master/img/Document.jpg)

6：第一步的配置文件skip-grant-tables注释掉。service mysqld restart（重要）

7：与愉快快的解决啦啦啦啦啦

# 总结

我搞这点辣鸡破事，搞了一下午。说出来我自己都不信。但的的确确是真的。
对linux系统还不熟悉，要多练习一下才行啊。

还是就是百度真的有点坑，有些东西的确百度很方便。但有些东西百度就是辣鸡。
我一下午都在百度解决问题，这个文章那里抄一点，那个文章这里抄一点，找来找去还是那些。
真的伤神。

最后看了官方文档才解决的。还是官方文档碾压一切
附链接 https://dev.mysql.com/doc/refman/5.7/en/resetting-permissions.html

接下来就是用数据库拉


ps：以上步骤在2017年8月1日20:10:21在Linux version 3.10.0-327.36.3.el7.x86_64测试成功
