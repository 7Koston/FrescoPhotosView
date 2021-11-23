#optimized
-dontobfuscate
-optimizations !code/allocation/variable
-repackageclasses ''
-allowaccessmodification

-dontusemixedcaseclassnames
-verbose

#okhttp3
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#Fresco
-keep class com.facebook.imageutils.ImageMetaData { *; }

#AppCompat
-keepclassmembers class androidx.fragment.app.FragmentManager {
    <init>(...);
}