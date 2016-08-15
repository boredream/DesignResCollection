# DesignResCollection
提供同一个App的不同架构实现，对其进行对比分析，方便大家选取使用  

仿造谷歌的App框架项目 https://github.com/googlesamples/android-architecture  
  


# 为什么要做这样一个开源项目 [持续开发中...]

Android 的框架多用MVC模型进行开发，而其中的Activity经常承担了大量的V和C的工作，既处理逻辑又处理UI。  
因此Activity中很容易聚集大量代码，造成结构复杂混乱、测试维护困难等诸多不便。  

而这个项目就是为了帮助解决这个问题的。在这个项目中将提供相同的应用程序，使用不同的框架实现之。  

您可以使用本项目中的示例代码作为参考，或者直接作为项目的架子在此之上继续开发自己的项目。  
本项目中，主要关注的重点在于代码的结构框架、测试以及可维护性。  
但是要注意，这里提供了不同的架构，各自有自己的优缺点。因此在选取时要根据自己的需要选择对应的框架结构。  
比如你只是一个简单的App，不需要单元测试，功能UI都比较少，那直接MVC结构即可。  


## 代码示例

显示设计网站中收集来的资源的一个应用DesignResCollection，不同结构对应不同的[_结构后缀]。  
比如基本的MVP结构就是 DesignResCollection_MVP。不同结构的具体介绍请查看对应文件夹中的README.md  


### 已开发完成的示例

  * [DesignResCollection_MVC/](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVC) - Model-View-Controller 结构.
  * [DesignResCollection_MVC/](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP) - Model-View-Presenter 结构.

### 待开发的示例
  * [DesignResCollection_MVC-Dagger2/](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP-Dagger2) - 基于 Model-View-Presenter 结构，添加Dagger2框架.


### 后台实现

直接使用LeanCloud作为后端服务，比较简单，无需自行开发。  
使用LeanCloud的Restful-API接口，而非直接使用LeanCloud的Android SDK，更贴近于实际开发中情景，可以使用自己的网络框架。  
网络框架部分使用Retrofit2.0 + RxJava。  


## 到底使用哪种框架使用在我自己的app中？

这里是给你的建议：每个框架示例中都有一个README，你可以先查看下每种的特点。  
最终项目里还会对比下所有框架的优缺点列出来，方便你根据自己具体情况进行全面的比较选取。  


## 使用

下载~解压~Open对应框架项目的文件夹

