# Life helper

LifeHelper的需求是从身边的朋友或自己收集而来，以B/S结构部署在云服务上作为“生活助手”。目标人群为身边的朋友，所以暂未考虑高并发的实现。

该项目一期最终实现的功能如下：
* 上传图片至数据库存储；
* 分页预览已上传的图片；
* 每次下载相同的图片，其MD5都不一样。

## 相关技术
* 使用Spring boot搭建项目；
* 集成Mybaits；
* [图片压缩](https://github.com/coobird/thumbnailator);

## 模块
### ImageSerive - 已实现功能
* 批量上传图片至服务器的指定路径；
* 批量上传图片至MySQL；
* 预览图片；
* 下载图片；
* 压缩图片质量；
* 修改图片MD5值。

### ImageService - 待优化功能

* 分页预览

  使用[Mybaits插件](https://github.com/pagehelper/Mybatis-PageHelper)完成服务端的分页功能
* 多图下载
  
  可勾选多张图片，同时下载
* 优化上传图片
  
  优化上传图片的页面，并提供该界面的快捷键至图片预览页面
* 优化预览图片
  
  优化为分页预览原图的缩略图，提高浏览速度
    
## 杂
LifeHelper目前只有关于图片的一个模块，若后续有需要会在此基础上追加模块。
