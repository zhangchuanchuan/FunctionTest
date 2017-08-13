# FunctionTest
#### 背景
开发JS接口方法过程中，要调试自己的方法，经常写一些测试的数据传到自己的方法上调试，每次修改自己的测试的数据都比较麻烦，而且测试的代码还有忘记删掉的风险。对于QA测试来说测试也比较麻烦，不能灵活的改变参数来测试我们写的方法。

因此这里想办法通过注解配置的形式，自动生成一个测试这类方法的工具。

#### 适用场景
当我们给具体的业务场景或者某个组件调用者提供API的时候，通常情况下都满足这两个条件：
1、各个方法运行的环境一致；
2、各个方法传入的参数类型一致（比如JSONObject、Map等）。

比如，我自己是要给M端提供一些本地方法的api。这时候，我们都可以使用这种方法来做一个测试的小工具。这次的例子将通过JSONObejct作为参数来实现。当然也可以换做其他的参数。

#### 模拟环境

我们先在例子中模拟出开发中一些场景，ContainerActivity作为业务方的运行环境，LocalFunctionAPI为我们开发的api方法。可以看到，业务方可以通过api来传入不同的参数，来满足业务需求。但是我们怎么来测试我们的api呢？
```
    LocalFunctionAPI mAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        mAPI = new LocalFunctionAPI(this);

        findViewById(R.id.alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realDo();
            }
        });
    }

    //业务方调用我们api
    private void realDo() {
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hello World");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAPI.alert(object);
    }
```

#### 定义注解

要测试我们的api，我们要知道单个接口所有的参数，最好还要有一些预先配置的测试参数。为了更好的知道API的功能，还提供了一个name来显示出来，一目了然。
```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface APITest{
    //api的名称
    public String name() default "";
    //api的参数,竖线分割
    public String param() default "";
    //api的测试数据,JsonArray形式，内容是JsonObject
    public String testParam() default "";
}
```

我们的api中方法，就可以根据注解的规则来写注解。
```
    /**
     * 弹窗的api，参数{"msg":"hello world"}
     * @param object json Object
     */
    @APITest(name = "alert", param = "msg",
            testParam = "[{\"msg\":\"hello world\"}, {\"msg\":\"hello android\"}]")
    public void alert(JSONObject object) {
        // check runtime
        if (context == null) {
            throw new RuntimeException("无运行环境");
        }
        String msg = "";
        // check param
        if (object.has("msg")) {
            try {
                msg = object.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("传入参数不正确");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(msg).create().show();
    }
```

#### 解析注解
可以通过class的一些属性来获取方法和注解信息。
```
    private List<APITestVo> getApiTestList() {
        List<APITestVo> list = new ArrayList<>();
        Method[] declaredMethods = LocalFunctionAPI.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            APITest apiTest = method.getAnnotation(APITest.class);
            if (apiTest != null) {
                APITestVo apiTestVo = new APITestVo();
                apiTestVo.setApiTest(apiTest);
                apiTestVo.setMethod(method.getName());
                list.add(apiTestVo);
            }
        }
        return list;
    }
```

有了数据我们可以展现出API的列表。
![function_list.png](http://upload-images.jianshu.io/upload_images/2346620-f6d370e80120b7ea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### JsonObejct转换问题
由于传过来的字符串不能直接用JSONObejct来解析出来，只能先通过Gson的JsonObejct解析出来，然后转换成JSONObejct的形式来做。调试时发现JSONObejct不能直接用，才发现的这里问题，希望大家少走弯路吧。
```
            mTestVos = new TestParamVo[objects.length];
            for (int i = 0; i < objects.length; i++) {
                TestParamVo paramVo = new TestParamVo();
                JsonObject object = objects[i];
                Set<String> keys = object.keySet();
                JSONObject jsonObject = new JSONObject();
                for (String key : keys) {
                    try {
                        jsonObject.put(key, object.get(key).getAsString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                paramVo.setParam(jsonObject);
                mTestVos[i] = paramVo;
            }
```

#### 测试环境

测试的Activity中构建一个和ContainerAcitivity一样的空间，只是页面不同。这里的页面来展现出解析的测试参数和值。分别用两个list来实现。这里代码不在赘述，就是一些简单的交互。

![image.png](http://upload-images.jianshu.io/upload_images/2346620-9bbbc117f79fc965.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

当点击执行的时候，获取所有的参数，组装参数，反射执行api的方法：
```
                //反射执行
                JSONObject jsonObject = new JSONObject();
                for (ParamVo paramVo : mParamVos) {
                    if (!TextUtils.isEmpty(paramVo.getValue())) {
                        try {
                            jsonObject.put(paramVo.getName(), paramVo.getValue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Method[] declaredMethods = mAPI.getClass().getDeclaredMethods();
                boolean hasExecute = false;
                for (Method method : declaredMethods) {
                    if (mMethodName.equals(method.getName())) {
                        try {
                            hasExecute = true;
                            method.invoke(mAPI, jsonObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!hasExecute) {
                    Toast.makeText(TestAPIActivity.this, "没有这个方法", Toast.LENGTH_SHORT).show();
                }
```

####总结
生活工作中应该多多思考，尤其是当发现我们经常做一些重复繁琐的事情的时候就应该多想想是不是有什么解决方案来解决这些问题呢？

希望这个例子能给大家带来一些帮助，这个例子的源码都在我自己的github上的 [FunctionTest](https://github.com/zhangchuanchuan/FunctionTest) 项目中能找到。
