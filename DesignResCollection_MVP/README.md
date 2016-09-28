## 为什么选择MVP？

相信大部分人都听过这个框架，或者已经使用过。
了解和简单运用的过程中大家一定会有这样几个问题或者痛点：
* [**MVP有什么好处，为什么要用它？**](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP#mvp有什么好处为什么要用它)  
* [**MVP结构代码怎么写？**](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP#mvp结构代码怎么写) 
* [**为什么MVP结构利于单元测试？而且我为什么要写测试代码呢？**](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP#我为什么要写测试代码呢不是浪费时间吗) 
* [**好了你说服我了，但是我不会写单元测试啊！**](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP#怎么写测试代码呢) 
* [**MVP多了好多类，还要写测试代码，写起来好累啊！老娘不想这么麻烦啊！**](https://github.com/boredream/DesignResCollection/tree/master/DesignResCollection_MVP#mvp多了好多类还要写测试代码写起来好累啊老娘不想这么麻烦啊) 

这里班门弄斧的分享下我的经验，挨个解决这几个问题。

---

## MVP有什么好处，为什么要用它？
网上文章一大堆，总结下来主要有下面几个优点：
* 代码解耦、结构更清晰
* 更好的拓展性
* 可复用性
* 利于单元测试

优点其实主要是相对传统MVC结构而言的，简单对比下：
* MVC（Model-View-Controller）
传统MVC结构中，C承担着一个总控制器的作用，处理Model数据，再控制View的显示。
大部分时候Activity类就是这个角色，我们在Activity中调用接口，接口返回数据后各种setText setImage显示到UI上。
* MVP（Model-View-Presenter）
重点在于Presenter，它其实是将Model和View分开了，在其中起到一个中转站的角色。
把Model数据拿来一通处理，然后丢给View让它自己去解决具体的UI显示。

**打个比方
如果处理Model处理业务逻辑就是加工食材做菜。把菜送到客户手里呈现给客户就是View的展示。
那MVC就是大排档。C就是独自运营的老板，自己炒菜，做完再自己送到小桌子上的客户面前，一条龙。
MVP就是正规大餐厅，P则是后厨中心，海绵宝宝做好蟹黄堡后放到窗口处，叮一下通知前台好了可以送餐了，不用关心菜是怎么送到客户手里的。然后由服务员章鱼哥在窗口处取了餐，再或跑或跳或踩着轱辘鞋最后送到客户手里，合作完成。**

所以这里也可以看出来，MVP最重要的特点就是：
**将 Model业务逻辑处理 和 View页面处理 分开！！！**

MVP的良好拓展性、解耦、利于单元测试等优点基本都是来源于此。

纯语言描述大家可能还是不好理解，下面上实战项目。

---

## MVP结构代码怎么写？
示例项目中的MVP结构参考了[谷歌官方MVP示例项目](https://github.com/googlesamples/android-architecture/tree/todo-mvp/)中的写法。每个功能模块都包含以下几部分：
* **Contract协议类**  
这个Contract协议类不是MVP中的任何一个模块，是把所有View和Presenter的方法都提取成了接口放在这里，作为一个总的规则、协议，方便统一管理。
比如下面的代码，就是示例项目中意见反馈页面的Contract协议类，提供了View和Presenter的接口。
其中BaseView和BasePresenter是提供了一些基础方法，比如显示进度showProgress等，自己可以按需添加。
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
* **Model**  
数据层，和MVC结构中的无区别，没啥好说的。

* **Presenter**  
负责处理业务逻辑代码，处理Model数据，然后分发给View层的抽象接口。
注意，这里是将处理好的数据派发给View的抽象接口，是一个简单的中转分发出去，并不负责具体展示
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

* **View**  
负责UI具体实现展现。比如Presenter派发过来一个动作是showProgress显示进度命令，那由我这个View负责实现具体UI，是显示进度框还是显示一个下拉刷新圈圈等，都是View这里自行控制。
Google的例子中，每个Activity中都会添加一个Fragment作为View实现，Activity仅仅作为一个容器，包含一个Fragment在其中显示各种控件。我觉得其实也可以直接将Activity作为View。本示例代码中两种方式都有，可以根据需要自行选择方式~ 
```java
public class FeedBackActivity extends BaseActivity implements FeedBackContract.View {
    private FeedBackContract.Presenter presenter;
    private EditText et_content;
    private EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        presenter = new FeedBackPresenter(this, HttpRequest.getInstance().service);
        initBackTitle("意见反馈")
                .setRightText("提交")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
        et_content = (EditText) findViewById(R.id.et_content);
        et_email = (EditText) findViewById(R.id.et_email);
    }

    private void submit() {
        // 开始验证输入内容
        String content = et_content.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        presenter.addFeedback(content, email);
    }

    @Override
    public void addFeedbackSuccess() {
        showToast("反馈成功");
        finish();
    }

    @Override
    public void setPresenter(FeedBackContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void showTip(String message) {
        showToast(message);
    }
}
```

  注意，这里BaseView中会有一个isActivite方法，用于判断视图是否被销毁。我在BaseActivity中会统一处理，添加一个isActivite变量，onStart时设为true，onStop时设为false。
然后在presenter里的接口返回数据后，判断view是否被销毁然后再控制显示，因为接口是异步的，所以返回数据后视图可能已经销毁，那就没必要更新了，更新反而还会崩溃报错。

**好了，现在再回头看看MVP的几个优点，可能就有更好的理解了（当然，还是要自己撸过一遍最好）**
  1. 更好的拓展性。  
    * 某天页面需要加功能了，协议类中先写好对应的P逻辑方法、V页面方法，然后在实现类中分别编写具体代码即可。
    * 某天突然改功能了，说所有错误提示我们不用Toast，用Dialog吧，那直接在showTip处修改即可。
    * 某天产品突然告诉你说意见反馈，失败我们也让用户觉得成功，那直接在Error回调里调用view抽象方法即可。
  2. 解耦、更好的代码结构。  
    * 业务逻辑 和 页面UI 代码分开，不揉在一起，改逻辑的时候不用关心UI，反之亦然。
    * 想了解某个模块功能时，直接在协议类中看一个个抽象方法，不用关心代码，清晰明了。
    * 还有代码可以分工合作，核心业务逻辑你在P中自己写，UI的具体实现直接给其他人合作写。
  3. 可复用性。  
  比如本项目中的注册功能，注册步骤1和步骤2页面中都有发送验证码功能，那就可以使用同一个P了，在其中调用获取验证码接口。然后各自实现具体View显示，步骤1页面获取验证码成功后跳转到页面2，页面2获取成功后开始数字倒计时。

---

## 为什么MVP结构利于单元测试？
之前提到过，MVP结构最大的特点是，P将逻辑和UI分开了。即P 中没有任何Android相关的代码，比如Toast啊、setText等等。这意味着~ 你可以针对Presenter写junit测试了。只对java代码的测试，不用涉及任何UI！！！不用运行模拟器的测试！！！！！速度起飞的测试！！！！！！！！

说的这么热闹，那么

## 我为什么要写测试代码呢？不是浪费时间吗？
测试其实除了检测bug验证逻辑之外，还有最重要的一个功能是**提高开发速度！**
你没有看错，虽然写了更多的代码，但实际效率是提升的，尤其对越庞大越复杂的应用来说。
可能我这样说不够权威，可以看下经典书籍《重构》然后自己尝试一下，可能就会有感受了。

---

## 怎么写测试代码呢？
我们先介绍下Android中的两种测试
* **UI测试（本项目中使用框架Espresso）**  
UI测试其实就是模拟机器上的操作行为，让它自动进行的“点击某个位置”、“输入某些字符串”等行为。
是依赖安卓设备的，测试的时候可以在手机或模拟器屏幕上看到页面被各种点点点，输输输，跳来跳去。

  这个其实和MVP结构关系不大，MVC，MVP，或者MVABCDEFG都可以进行UI测试，所以这里暂时不多做介绍，可以直接参考示例项目中的代码。UI测试部分的内容其实也很多，以后单独拿出来再详细展开。

  项目中androidTest文件夹里的就是UI测试代码，而test文件夹才是Junit部分的单元测试代码。

* **对Presenter进行Junit单元测试（本项目中使用框架Mockito）**  
UI测试虽然接近真实场景，但是有个缺点是要运行应用到模拟器上，所以速度就会有影响，慢~
而且开发中也会常有这样一个需要，调试接口时，我不想点点点跳转到那个页面再输入东西再点按钮，费时间啊~而用postman啥的工具也麻烦，header还要重新写，如果有参数加密就更蛋疼了。

  所以，这个时候你就需要Junit单元测试了，最大的特点就是**不用运行安卓设备，直接run代码，速度飞快！**

* **单元测试代码示例**  
正式开始介绍怎么写之前，先感受下单元测试是什么样的，如下图

![意见反馈Presenter代码截图](http://upload-images.jianshu.io/upload_images/1513977-e63720e22dbfd3c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里针对示例项目中意见反馈Presenter分别测试了几个场景
  * 真实接口提交成功
  * 模拟接口提交成功
  * 模拟接口提交失败

三个Test方法，针对三个测试场景。
突破左下角运行情况可以看到，一共用了852ms，1秒不到！！！
第一个测试方法因为是真实调用接口数据，所以稍微耗费点时间。
右下角也可以看到3个用例全测试成功通过，也打印了真实调用数据的接口日志。
完美~

---
##如何写单元测试代码
编写步骤按照以下进行

**1. 新建Presenter的测试类**  
右键Presenter类 -> Go To -> Test -> create new test  
![](http://upload-images.jianshu.io/upload_images/1513977-d4e327220c223843.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/620)  
弹出一个创建测试类对话框，然后勾选需要测试的方法（当然也可以自己手动创建方法）。然后OK，选择test文件夹完成测试类创建。  
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
#### Mockito框架介绍
* **Mockito框架是干什么的？**  
mockito框架是用来模拟数据和情景的，方便我们的测试工作进行。
* **为什么要用Mockito框架？**  
比如我们MVP结构中P的测试，有个问题是：创建Presenter对象的时候这个View怎么办？传入null会空指针啊。还有很多接口调用等逻辑，很多奇怪的失败情况怎么测试？
这个时候就可以用mockito了~ 直接模拟一个view接口对象，不用关心它的具体实现；失败情况直接用when方法搞定；此外还提供了其他一系列方便测试的方法，比如verify用于判断某对象是否执行了某个方法等。后面会根据例子挨个介绍。

网上很多例子其实是纯mock模拟测试，也就是接口api也是模拟的，模拟接口调用，模拟接口返回数据。
虽然这样速度快且方便模拟各种错误情况，但是有时候也会想要测试真实的接口返回情况，因此本项目示例中提供了两种模拟和真实接口的写法和处理。参考上面代码里的presenter和mockPresenter对象。

注意，mock相关方法比如verify、when等使用者也都必须是mock对象，所以使用presenter的时候不能用when什么的方法模拟接口返回。

@Before标签的方法，是每个测试方法调用前都会走一遍的方法，因此在里面放了一系列的初始化操作，每个操作都添加了注释。其中需要单独解释的是when方法。
```java
when(view.isActive()).thenReturn(true);
```
这个是mockito框架提供的一个方法，看英文基本就能了解什么意思了，当xx方法调用时就返回xx
因为我们的view的模拟的，所以没有实现isActive方法，则p中数据返回后就无法继续走下去了，因此这里when处理一下。只要调用这个方法就返回true。
  
**3. 测试方法编写**  
通常Presenter中的一个业务方法会对应至少一个测试方法。
比如这里的意见反馈业务，就分别对应意见提交成功、失败两种情景。
方法名字可以随便定，有个@Test标签即可，推荐方法取名为：test+待测方法原名+测试场景
测试场景一共有哪些呢？这个最好问测试要个测试用例按照待测功能对应的所有情景挨个来。

我这里写的单元测试代码，对于接口又分了两种：** 模拟接口 ** 和 ** 真实接口 **

直接全部用真实接口测不很好吗，为什么要mock模拟测试呢？
好吧，比如我们这个意见反馈，不像登录还有密码错误的情况，很少有场景能失败。怎么办？
所以对于难以模拟的情景，还是需要用mockito框架模拟的，模拟个失败，然后验证失败后的一系列逻辑~

下面挨个介绍测试方法，模拟成功和失败差不多就只介绍失败了。

* **模拟接口测试方法示例 - 模拟提交失败**
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
这里重点是when的运用，当模拟的api调用addFeedBack时，就返回error结果。
然后调用mockPresenter的意见反馈业务方法，最后验证结果。
注意，这个verify方法也是特别常用的一个mockito方法，用于验证某个对象是否执行了某个方法。
最后运行测试，成功，完美~


* **真实接口测试方法示例 - 提交成功**  
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
运行测试，成功，完美~

  再次强调，mockito的方法都是针对模拟对象的，所以调用真实请求api时，你也想用when去处理，那就会报错~

  注意，真实接口由于是异步的，所以如果不做任何处理是无法测试通过的，接口数据还没返回就运行下面的验证了，自然失败。因此需要对回调做一个处理，将其修改为同步请求，这样就能一条线下来了，运行完接口再进行验证。项目是基于Retrofit框架的，使用RxJava处理回调，我这里所有的回调都会用一个ObservableDecorator处理一下，而在其中我会判断，如果当前是测试状态（也就是Before中的那个isUnitTest 参数），就将回调设置为同步，具体代码参考项目中。

**4. 运行单元测试用例**  
* 右键方法，run 测试单个用例方法
* 右键类，run 测试该类中包含的全部用例方法

最后控制台看结果
参考最上面单元测试代码示例中的截图，下面控制台会显示测试了哪些方法，测试成功通过了几个方法，然后打印相应日志，如果不通过还会打印对应错误信息。

好了，写法介绍完毕~
更多例子请去项目中查看，这里篇幅有限就不太详细的展开了，简单列举几个例子让大家感受下。

---

## MVP多了好多类，还要写测试代码，写起来好累啊！老娘不想这么麻烦啊！
这一点估计是最重要的原因把绝大部分人阻挡在门外。
毕竟平常普通的撸就那么累了，还要这么麻烦，没时间啊没精力啊！！！

* **不一定所有功能都用MVP**  
就像之前例子举得那样，大排档和正规餐厅。你在一个超级偏远没人流量生意差到爆的地方还整个后厨中心，就过了。同理，如果你有的功能业务逻辑比较简单，自然就没必要MVP了，简单的关于页面你也一顿MVP可能就有点猛了，所以不一定所有功能都使用MVP。

* **单元测试利于开发**  
代码结构啥的就不说了，单元测试这个有时候真的很方便，尤其是运行快。相信大部分人都有经验，遇到个不靠谱后台的时候，经常要陪他们调接口，再遇到那种特别深的页面简直是浪费人生。单元测试代码，run，唰~秒搞定。自测某些逻辑功能时也很有用，这一点上看来绝对是节省时间的。

* **LiveTemplate（干货！！！一键生成模板代码，模板可自定义！！！）**  
我通常撸的时候特别特别注重速度效率。之前也开发过很多插件工作，比如已经发布的自动生成代码布局的开源AndroidStudio插件。https://github.com/boredream/BorePlugin

  然后就寻思，写这种特别有规律的MVP各种类，还有测试类等的时候，要不也弄个插件生成下？
但是想了下觉得插件生成模板代码的话，模板怎么写呢？尤其MVP这种不同的人写法也不同啊。
最后突然想起来了AndroidStudio里自带的LiveTemplate这东西，是AS中自带的一个模板代码系统。

####使用LiveTemplate模板
先展示下该功能的强大，这里我以前提前写好过几个模板了。拿协议类举例。  
1. 右键需要生成的位置 -> New -> 选择模板（如下图的MvpContract）  
![](http://upload-images.jianshu.io/upload_images/1513977-01bbba5d35b7cbd6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
2. 然后弹出对话框，为模板输入需要的变量，OK生成  
![](http://upload-images.jianshu.io/upload_images/1513977-7ddead582f672c79.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
3. 这样就按照我们的模板创建了一个文件，右侧文件代码全部都是自动生成的，然后按需修改加入方法即可。  
![](http://upload-images.jianshu.io/upload_images/1513977-8dbb18d5fc8bddff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

那么模板哪里来的呢~下面介绍

####编辑/创建LiveTemplate模板
1. **编辑已有模板**： New -> 选择模板的时候，模板底部有个Edit File Template，点击之。参见上面使用步骤1的图。  
2. **创建新的模板**：打开你希望生成模板的文件，选择工具栏中的Tool -> Save File as Template  
3. 步骤1、 2都会打开下面这样一个编辑页面，区别在于创建比编辑少个左侧的已有模板列表  
给模板起个名字，然后在内容页面里根据需要删删改改即可，模板里所有${NAME}的地方都会替换成你创建模板时候输入的文件名，其他的${XXX}的作用可参考下面Description里的描述。最后OK保存模板。  
![](http://upload-images.jianshu.io/upload_images/1513977-4b5be95db3d361d5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

LiveTemplate虽然无法替你搞定绝大部分代码，但是这样一个快捷的模板，可以灵活的随时编辑还是很方便的，还是能节省相当代码量的。

和本期主题无关的插个话，LiveTemplate是个很神奇的东西，很多地方都可以用，不光有文件的模板，代码也是。比如输入sout+回车就会自动生成System.out.print()代码，输入Toast+回车就会自动生成Toast.make blablabl的代码，超级方便。比如你们项目有BaseActivity，需要复写几个方法，那就可以自定义创建个页面类文件模板里面处理好继承和方法，就不用每次新建完Activity都去写一下继承了。更多用法期待你滴挖掘~

---

## 结语
好了，之前提的所有问题和痛点都挨个解答过了，尤其最后的LiveTemplate，对于还不知道的同学，即使最后你还是不愿意用MVP和写单元测试，那这部分你也算赚到了哈哈。

因为要介绍的内容比较多，MVP啊~测试啊~Junit单元测试啊~LiveTemplate啊~ 所以介绍的比较精简，主旨在抛砖引玉，希望大家对这几个东西能有个了解，感兴趣后再深入研究，也希望与我多多交流大家共同进步。

本项目里Junit测试模块其实还是有几个问题的，比如Presenter我是将接口Api作为构造函数参数依赖注入的，所以其实还可以再加入Dagger2改进一番，下一个框架就会在MVP的结构上加入Dagger2。

谷歌例子中RxJava是单独拎出来说的，我这里Retrofit2+RxJava是作为所有例子通用框架的，用法可以给大家作为一个参考，这里就不扫盲Retrofit用法了。

最后，如果文章对你有一点作用和启发，希望能支持一下，欢迎follow我和star本项目
