该工程基于 tkmapper 的 example封装 ，与 基于jpa的 nacativeQuery方法进行封装

里面有部分代码可能在迁移的过程中有部分冗余，在后续的修正中我会逐步的改正完善

本人联系方式 ： qq 1159856928  如果一时半会码云没有回复，可以QQ联系我

工程描述
```
一：在 am-orm-spi-springboot-start 工程中有这么几个模块 
1 日志 log  ： 使用了spi实现 ，自动探测项目中已经存在的 日志实现，这样做不会带来 代码侵入

2 json ： spi实现同上

3 pager 分页条件的接口封装


您在项目中引入的话，可以自行修改


三 ： 具体的使用方法，参见 zhangjun-jpa-demo 工程 中的 Test 部分 更多测试用例正在完善中


```
