项目启发来自谷歌的同类框架项目  android-architecture
利用一个相同的项目，使用不同的框架实现之

这里我找了个自己练手的App作为基础项目，然后尝试不同框架实现它
Github地址如下，其中有详细完全的介绍文档：
https://github.com/boredream/DesignResCollection  

#### 已开发完成的示例

  * [DesignResCollection_MVC/](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVC) - Model-View-Controller 结构。
  * [DesignResCollection_MVP/](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP) - Model-View-Presenter 结构。
  其中包含了针对P层的junit逻辑测试代码以及针对V层的Espresso页面测试代码。
  
---

## 为什么选择MVP？

不同于第一个项目传统的MVC结构，这次使用了现在比较流行的MVP。
相信大部分人都听过这个框架，或者已经使用过。

了解和简单运用的过程中大家一定会有这样几个问题或者痛点：
* ####MVP有什么好处，为什么要用它？
* ####MVP结构代码怎么写？
* ####都说MVP结构利于单元测试，但是我不会写啊！
* ####MVP多了好多类，还要写测试代码，写起来好累啊！老娘不想这么麻烦啊！

这里班门弄斧的分享下我的经验，挨个解决这几个问题。

---

## MVP有什么好处，为什么要用它？
和传统MVC相比，有很多优势。网上文章一大堆，总结下来主要有下面几点：
* 代码解耦、结构更清晰
* 更好的拓展性
* 可复用性
* 利于单元测试

首先要自己写，最好实际写个完整的项目。
有大量代码后可能才可以感受到结构更清晰、拓展性更好等这样听着玄而又玄的概念。

所以这里就不纯文字上赘述优点了，直接上完成的实战项目，一边参考代码一边介绍MVP特点。
** 单元测试属于MVP的“干货”，我也是在写单元测试的时候才更感受到了MVP应该怎么写，为什么要这样写。
所以单元测试部分会放在最后作为重点单独介绍。 **

---

## MVP结构介绍
这里以项目中的意见反馈页面为例，进行介绍。

* ####分包
传统MVC很多习惯所有页面放在一个activity或者ui名字的包中，这样按“类型”把所有页面分到一起。
MVP因为一个页面由V和P共同组成，所以推荐在ui页面包中再按照具体界面单独建包区分。（M单独放在一个包中。当然，还可以所有Contact放在一个包里，所有Presenter、View都各自放在一个包里）

![MVP的ui分包](http://upload-images.jianshu.io/upload_images/1513977-d3736c0f15632567.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

* ####Contact协议类
可以看到这里多了一个Contact协议类，这个是参考了[谷歌官方MVP示例项目](https://github.com/googlesamples/android-architecture/tree/todo-mvp/)的写法。
把所有View和Presenter的方法都提取成了接口放在这里，作为一个规则、协议。
比如下面的代码，就是示例项目中意见反馈页面的Contact协议类，提供了View和Presenter的接口。
其中BaseView和BasePresenter是提供了一些基础方法，自己可以按需添加，后面会介绍具体用法。
```java
public interface FeedBackContract {    
    interface View extends BaseView<Presenter> {        
        void addFeedbackSuccess();    
    }    

    interface Presenter extends BasePresenter {        
        void addFeedback(String content, String email);    
    }
}
```

* ####Presenter
负责处理业务逻辑代码，和Model进行交互处理。然后分发给View层的抽象接口。
  1. 所有的逻辑、和Model业务处理全部统一放在Presenter中，Activity中不应该再有任何调用接口、复杂的逻辑数据等代码。
  2. 处理完成后的数据分发给View层的 **抽象接口**，不关心View的具体实现。

  具体代码如下
```java
public class FeedBackPresenter implements FeedBackContract.Presenter {
    private final FeedBackContract.View view;
    private final HttpRequest.ApiService api;

    public FeedBackPresenter(FeedBackContract.View view, HttpRequest.ApiService api) {
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void addFeedback(String content, String email) {
        // 开始验证输入内容
        if (StringUtils.isEmpty(content)) {
            view.showTip("反馈内容不能为空");
            return;
        }
        if (StringUtils.isEmpty(email)) {
            view.showTip("请输入邮箱地址,方便我们对您的意见进行及时回复");
            return;
        }

        view.showProgress();

        // 使用自定义对象存至云平台,作为简易版的反馈意见收集
        FeedBack fb = new FeedBack();
        fb.setContent(content);
        fb.setEmail(email);
        Observable<BaseEntity> observable = ObservableDecorator.decorate(api.addFeedBack(fb));
        observable.subscribe(new Subscriber<BaseEntity>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                if (!view.isActive()) {
                    return;
                }

                view.dismissProgress();
                view.showTip("反馈提交失败");
            }
            @Override
            public void onNext(BaseEntity entity) {
                if (!view.isActive()) {
                    return;
                }

                view.dismissProgress();
                view.addFeedbackSuccess();
            }
        });
    }
}
```
  写法很简单，新建一个Presenter类实现对应协议类中的Presenter接口，然后在实现方法中处理业务逻辑，比如这里是调用意见反馈接口，判断数据非空等，最后分发给View调用相应的抽象方法。
根据这段代码，回头看上面的优点。
  1 很好理解，所有业务统一放在P中，这样Activity不会有大量接口代码等太混乱，代码结构更清晰~没毛病！
  2 上面例子中的，本地错误、网络请求错误的提示都是调用一个view.showTip(string)方法。
  这个是  BaseView接口中的一个抽象方法，不包含任何实际内容，最后会在View层中具体实现，比如显示Toast。

  ** 为什么P要调用V的抽象接口呢？ 这个特点其实是MVP结构最重要的地方。**
  也是因为这种代码结构方式，所以才形成了MVP主要的几个优势：
  1. 更好的拓展性。
    * 某天页面需要加功能了，协议类中先写好对应的P逻辑方法、V页面方法，然后在实现类中分别编写具体代码即可。
    * 某天突然改功能了，说所有错误提示我们不用Toast，用Dialog吧，那直接在一个地方showTip修改就行了。
  2. 解耦、更好的代码结构。
    * 业务逻辑 和 页面UI 分开，自然结构就更清晰了。
    * 某天业务逻辑变化后，只改P中代码即可，不用关心UI具体实现。
      比如产品突然告诉你说意见反馈，失败我们也让用户觉得成功，你就不用在Error回调里也写一堆代码，直接调用个view反馈成功对应的抽象方法即可。
    * 还有代码可以分工合作，核心业务逻辑你在P中自己写，UI的具体实现直接丢给小弟或者其他人合作写。
  3. 可复用性。
  比如项目例子中的注册功能，注册步骤1页面和步骤2页面中都有发送验证码功能，那就可以使用同一个P处理业务逻辑，因为请求验证码接口的一样的，而View实现在不同页面中就可以自定义区分处理，步骤1获取验证码后跳转到页面2，页面2获取成功后是开始数字倒计时。
  4. 利于单元测试。
  P 中没有任何Android相关的代码，比如Toast啊、setText等等。
  这意味着~ 你就可以写junit测试了！！！就是针对java的测试，不用运行模拟器的测试！速度起飞！！！也是我觉得MVP结构最最重要的一个优点。至于怎么写测试会放在最后单独说。

* ####View
注意不是Android中的TextView、ImageView的View，是指UI的具体实现模块。
Google的例子中，每个Activity都会用一个Fragment显示在页面中作为View实现，也可以直接将Activity本身就视为View。本示例代码中两种方式都有，可以根据习惯选择使用即可~ 

  注意，上面FeedBackPresenter代码中在接口返回数据后，会判断，如果View是已激活，才接着调用View的方法进行各种UI更新操作。因为接口是异步的，可能返回数据后视图已经销毁，那就没必要更新了，更新反而还会崩溃报错。

---

## 测试介绍
这里我们会分别介绍两种测试
1. 使用Espresso进行UI测试
2. 使用Mockito对Presenter进行Junit测试

在开始之前，很多人肯定会这样一个疑惑
* ####为什么要写测试代码？不是浪费时间吗？
测试其实除了检测bug验证逻辑之外，还有最重要的一个功能是**提高开发速度！**
你没有看错，虽然写了更多的代码，但实际效率是提升的，尤其对越庞大越复杂的应用来说。
可能我这样说不够权威，可以看下经典书籍《重构》然后自己尝试一下，可能就会有感受了。

* ####Espresso测试
这个其实和MVP结构关系不大，所以这里暂时不做介绍，内容也很多，以后单独拿出来再详细展开。
项目中androidTest文件夹中就是针对安卓用Espresso进行的UI测试，而test文件夹才是Junit部分的单元测试。

* #### 单元测试
Espresso进行的UI类测试，其实就是模拟行为，让它自动进行的“点击某个位置”、“输入某些字符串”等行为。
所以UI测试是依赖安卓设备的，测试的时候可以在手机或模拟器屏幕上看到点点点，输输输，页面跳来跳去。

  这种测试虽然接近真实场景，但是有个缺点是要运行应用到模拟器上，速度就会有影响，慢~
开发中其实也会有这样一个需要，比如调试接口时，我不想点点点跳转到那个页面再输入东西，费时间啊~
用postman啥的工具也麻烦，header还要重新写，如果有参数加密就更蛋疼了。

  所以，这个时候你就需要Junit单元测试了，最大的特点就是**不用运行安卓设备，直接run代码，速度飞快！**

* ####单元测试代码示例
先举个例子，看一下单元测试什么样感受下，如下图
针对示例项目中意见反馈Presenter分别测试了几个场景
  * 真实接口提交成功
  * 模拟接口提交成功
  * 模拟接口提交失败

![意见反馈Presenter代码截图](http://upload-images.jianshu.io/upload_images/1513977-e63720e22dbfd3c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

三个Test方法，针对三个测试场景。
左下角运行情况可以看到，一共用了852ms，1秒不到！！！
第一个因为是真实调用接口数据，所以稍微耗费点时间。
右下角也可以看到3个用例全测试成功通过，也打印了真实调用数据的接口日志。
完美~

---
##怎么写单元测试
按照以下步骤进行

**1. 新建Presenter的测试类**
右键Presenter类 -> Go To -> Test -> create new test
![](http://upload-images.jianshu.io/upload_images/1513977-d4e327220c223843.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/620)
弹出一个创建测试类对话框，然后勾选需要测试的方法（当然也可以自己手动创建方法）。
然后OK，选择test文件夹完成测试类创建。
![](http://upload-images.jianshu.io/upload_images/1513977-91538231b56a4d2e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/620)

**2. 测试类的初始化**
  代码如下（mockito的gradle配置等参考项目中build.gradle）

```java
// 用于测试真实接口返回数据
private FeedBackPresenter presenter;

// 用于测试模拟接口返回数据
private FeedBackPresenter mockPresenter;

@Mock
private FeedBackContract.View view;
@Mock
private HttpRequest.ApiService api;

@Before
public void setupMocksAndView() {
    // 使用Mock标签等需要先init初始化一下
    MockitoAnnotations.initMocks(this);

    // 当view调用isActive方法时，就返回true表示UI已激活。方便测试接口返回数据后测试view的方法
    when(view.isActive()).thenReturn(true);

    // 设置单元测试标识
    BoreConstants.isUnitTest = true;

    // 用真实接口创建反馈
    Presenter presenter = new FeedBackPresenter(view, HttpRequest.getInstance().service);
    // 用mock模拟接口创建反馈
    Presenter mockPresenter = new FeedBackPresenter(view, api);
}
```

这里用到了一个很重要的框架 Mockito。
看名字mock就知道有模拟的意思，对于无法获取或者难以获取的对象，我们就可以手动的模拟一个。
不过要注意，mock相关方法比如verify、when等，使用者也都必须是mock对象。

网上很多例子其实是纯mock模拟测试，也就是接口api也是模拟的。
但是实际工作场景里有时候会需要测试真实的接口返回情况，因此这里两个类型都提供了写法和处理。
我们分别mock虚拟了一个api和view，然后用真实和虚拟的api分别创建了两个Presenter。
  
** 3. 测试方法编写 **
通常Presenter中的一个业务方法会对应至少一个测试方法。
比如这里的意见反馈业务，就分别对应意见提交成功、失败两种情况（还可以添加反馈内容为空等各种测试用例方法）
方法名字可以随便定，有个@Test标签即可，推荐方法取名为：test+待测方法原名+测试场景

我这里写的单元测试代码，对于接口又分了两种：** 模拟接口 ** 和 ** 真实接口 **

直接全部用真实接口测不很好吗，实际场景，还能测出来接口问题。为什么要mock模拟测试呢？
好吧，比如我们这个意见反馈，不像登录还有密码错误的情况，很少有场景能失败。怎么办？
所以我们就可以用mockito框架模拟个失败接口返回结果，然后验证失败后的一系列逻辑~

* ######模拟接口测试方法示例 - 模拟提交失败
```java
@Test
public void testAddFeedback_Mock_Error() throws Exception {
    // 模拟数据，当api调用addFeedBack接口传入任意值时，就抛出错误error
    when(api.addFeedBack(any(FeedBack.class)))
        .thenReturn(Observable.<BaseEntity>error(new Exception("孙贼你说谁辣鸡呢？")));

    String content = "这个App真是辣鸡！";
    String email = "120@qq.com";
    mockPresenter.addFeedback(content, email);

    verify(view).showProgress();
    verify(view).dismissProgress();
    verify(view).showTip("反馈提交失败");
}
```
这里重点是when的运用，如果api调用addFeedBack接口时，就回调error结果。
然后调用mockPresenter的意见反馈业务方法，最后验证结果。
成功，完美~


* ######真实接口测试方法示例 - 提交成功
```java
@Test
public void testAddFeedback_Success() throws Exception {
    // 真实数据，调用实际接口
    String content = "这个App真是好！";
    String email = "110@qq.com";
    presenter.addFeedback(content, email);

    verify(view).showProgress();
    verify(view).dismissProgress();
    verify(view).addFeedbackSuccess();
}
```
这里用了真实接口对应的presenter对象，调用接口，然后验证成功结果。
成功，完美~

** 4. 运行单元测试用例 **
* 右键方法，run 测试单个用例方法
* 右键类，run 测试该类中包含的全部用例方法

最后控制台看结果
参考最上面单元测试代码示例中的截图，下面控制台会显示测试了哪些方法，测试成功通过了几个方法，然后打印相应日志，如果不通过还会打印对应错误信息。

---

#####有几个重点、坑要注意下：
真实接口

---

![MVP关系图](http://upload-images.jianshu.io/upload_images/1513977-ea87ee781b4a7481.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
