# LLog：Android移动端日志监视器
[![](https://jitpack.io/v/silvertheo/llog.svg)](https://jitpack.io/#silvertheo/llog)

## 功能
集成到项目中后，可以随时通过监视器查看调试日志。
- **支持Log监视器全局浮窗显示**
- **支持Log按级别筛选**
- **支持Log项点击展开详情**
- **自动权限检测，跳转浮窗权限管理页面**
- **支持拖拽、缩放、隐藏监视器窗口**

## 图示
|多级日志|展开详情|级别筛选|
|:---:|:---:|:---:|
|![](https://github.com/silvertheo/llog/blob/master/doc/screenshot_llog_01.png)|![](https://github.com/silvertheo/llog/blob/master/doc/screenshot_llog_03.png)|![](https://github.com/silvertheo/llog/blob/master/doc/screenshot_llog_04.png)|

## 集成
- **在项目的根目录的`build.gradle`添加：**
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
- **在应用模块的`build.gradle`添加：**
```
dependencies {
    implementation 'com.github.silvertheo:llog:master-SNAPSHOT'
}
```
- **在`AndroidManifest.xml`进行权限声明：**
```
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```
- **在`Application`中初始化：**
```
LLog.install(this)
```
