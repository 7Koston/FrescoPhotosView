# FrescoPhotosView
[ ![API](https://img.shields.io/badge/API-17%2B-blue.svg?style=flat) ](https://android-arsenal.com/api?level=17)
[![](https://jitpack.io/v/7Koston/FrescoPhotosView.svg)](https://jitpack.io/#7Koston/FrescoPhotosView)

Photos view based on [PhotoView](https://github.com/chrisbanes/PhotoView) with [Fresco](https://github.com/facebook/fresco) support.

This library uses RecyclerView + PagerSnapHelper to implement "pager-like" view which you can use for your purposes.

<p align="center">
  <img src="https://github.com/7Koston/FrescoPhotosView/blob/master/preview/FrescoPhotosView.gif" height="500">
</p>

### Including in project

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
   }
```
Add the dependency:
```gradle
dependencies {
  implementation 'com.github.7Koston:FrescoPhotosView:1.0.9'
}
```

## Thanks to

[Chris Banes](https://github.com/chrisbanes), [贾永凯同学](https://github.com/walkingCoder), [Liberuman](https://github.com/Liberuman)

## License

This project is licensed under the Apache 2.0 - see the [LICENSE](LICENSE) file for details
