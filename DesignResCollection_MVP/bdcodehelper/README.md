# BDCodeHelper 代码助手
Code Helper Model for android develper (Android Studio)  
代码为Android Studio 的 Model，不是完整App应用。需要主项目依赖导入作为辅助开发工具。

---
# 功能介绍
Model封装了应用中常用的功能，包括
####adapter包
* **LoadMoreAdapter** 加载更多适配器，用于包装普通RecyclerView.Adapter增添一个加载更多功能。适用于所有LayoutManager。  
装饰器设计模式。无需自定义控件，无需写一大堆监听代码，包装即用~   
感谢[飞飞大神](https://github.com/jeffreyhappy)的贡献，我在他代码基础上做了简单修改。

* **SettingRecyclerAdapter** 设置选项列表适配器  。
Item通用样式为：左侧图标、中间文字、右侧文字、右侧图标。  
使用数据类SettingItem，不同字段对应不同控件，图片资源为-1或者字符为null时，对应位置控件不显示。

####fragment包
* **FragmentController** fragment切换控制器。  
初始化时直接add全部fragment, 然后利用show和hide进行切换控制。

####utils包
大部分常用工具类，比较特殊的单独介绍下
* **TitleBuilder** 标题栏构造器。
使用构造器模式设置标题栏，结合layout中的include_titlebar.xml一起使用，先在布局中include引入标题栏布局，  
再于Activity或Fragment中使用TitleBuilder设置标题栏内容，标题栏格式为：标题文字、左右各自是文字/图片按钮。

####views包
封装了一些有用的自定义控件
* **BottomTabRadioButton** 底部导航栏选项卡RadioButton
利用onDraw绘制红色圆点、带数字的红色圆圈，提供各种自定义attrs属性可以对红点的颜色、数字大小、数字颜色等进行配置。
感谢[程序媛大神燕姐](https://github.com/xiaoxuyan)的贡献，我在他代码基础上做了简单修改。


