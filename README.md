# Testing-Notifier

##  功能

此项目用于定时抓取[安徽语言文字培训测试网](http://ahywpc.ahedu.gov.cn/) 的**普通话考试**报名通知。
如果考试地点是在合肥，则发送邮件通知到事先配置的收件人（收件人邮箱配置在`Dict`中，字典的`key`为`sys_notify_to`）。
并提供了web也面用于暂停抓取调度任务，还提供了部分api用于创建、更新调度任务（参考`tech.hongjian.testingnotifier.controller.JobController`）。


## 技术栈

* Spring Boot
* Spring Data JPA
* Quartz
* Freemarker
* JQuery

## 使用方法

1. 创建表
2. 调整applicantion-dev.yml中数据源配置
3. 运行`TsttingNotifierApplication`中的`main`方法