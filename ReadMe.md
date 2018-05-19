[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

AsmButterknife
----------------
[**中文文档**](https://www.jianshu.com/p/2735b54d8680)

Field and method binding for Android views which uses [ASM](http://asm.ow2.io/index.html) bytecode manipulation to inject boilerplate code for you.

[AsmButterknife] is similar to [Butterknife],except:
* [Butterknife] uses Annotation Processor,while [AsmButterknife] uses bytecode manipulation.
* [Butterknife] Field and method binding can't uses private modifier,while [AsmButterknife] can.

Usage
---------------------------
As [AsmButterknife] doing the exactly same thing as [Butterknife],so I'll keep the usage the same as [Butterknife] as much as possible.

* inject bytecode for ViewHolder:
```
static class ViewHolderTest extends RecyclerView.ViewHolder {

    @BindView(R.id.item)
    private TextView tv; //note that we can use private modifier

    @ViewInject //default value: @ViewInject(ViewInject.ViewHolder)
    public ViewHolderTest (View item) {
        super(item);
        this.tv.setText("with @ViewInject,event bytecodes will be inject below super(item)"); //correct,no NullpointerException
    }

    @OnClick(R.id.item)
    private void onClick() { //note that we can use private method
        Toast.makeText(this.tv.getContext(), (String) this.tv.getTag(), Toast.LENGTH_SHORT).show();
    }
}
```
rebuild and decompile class file,you'll see:

![ViewHolder](https://upload-images.jianshu.io/upload_images/2222997-95fa3e59794faeb7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

the code inside red square is what [AsmButterknife] had injected.

Known that in [Butterknife],you will have to call `Butterknife.bind` somewhere in source code,while [AsmButterknife] uses bytecode manipulation,there is no need(actually even more complex) to do that,all you need is to tell which method to inject by using `@ViewInject` annotation.


* inject bytecdoe for `Activity`:
1. using ViewHolder,known that you have to call the method annotated with '@ViewInject(ViewHolder)' in proper place in source code.
```
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = this.getWindow().getDecorView(); 
        this.inject(view); //you have to call the view inject method
    }

    @ViewInject
    private void inject(@NonNull View view){ //View must be the first argumnet
        //leave it empty,bytecode will be injected into this method 
    }

    @OnClick(R.id.tv)
    private void onTextViewClick() {
        Toast.makeText(this, "onTextViewClick", Toast.LENGTH_SHORT).show();
    }
}
```
2. an easy way to inject bytecode to `Activity`: by annotated `@ViewInject(Activity)`
```
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    private TextView mTextView;

    @Override
    @ViewInject(ViewInject.ACTIVITY) //specify Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //with @ViewInject(ViewInject.Activity),bytcode will be injected after setContentView
    }


    @OnClick(R.id.tv)
    private void onTextViewClick() {
        Toast.makeText(this, "onTextViewClick", Toast.LENGTH_SHORT).show();
    }
}
```
rebuild and decompile class file,you'll see:

![Activity](https://upload-images.jianshu.io/upload_images/2222997-5ae872cc134230bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/624)

the code inside red square is what [AsmButterknife] had injected.

Download
--------
Via Gradle:

first add the plugin to your `buildscript`:
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.whyn:asmbutterknife-plugin:<latest-version>'
    }
}
```
**ps:** you can check the newest version:[asmbutterknife-plugin](https://bintray.com/search?query=asmbutterknife-plugin)

and then apply it in your module:
```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.whyn.plugin.asmbutterknife'
```

TODO
--------
currently [AsmButterknife] only supports `@BindView`,`@OnClick`. ( ╯□╰ )


License
-------

    Copyright 2017 Whyn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



[ButterKnife]:https://github.com/JakeWharton/butterknife
[AsmButterKnife]:https://github.com/Why8n/AsmButterknife
