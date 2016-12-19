-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class butterknife.** { *; }
-keep class **$$ViewInjector { *; }
-keep class org.apache.http.** { *; }
-keep class android.net.http.** { *; }
-keep public class org.apache.** {*;}


-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#-libraryjars /libs/YouTubeAndroidPlayerApi.jar
-keepnames class org.apache.** {*;}
-dontwarn butterknife.internal.**
-dontwarn com.viewpagerindicator.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.**
-dontwarn android.net.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.annotation.ThreadSafe
-dontwarn org.apache.http.annotation.Immutable
-dontwarn org.apache.http.annotation.NotThreadSafe
-dontwarn org.apache.http.impl.auth.GGSSchemeBase
-dontwarn org.apache.http.impl.auth.KerberosScheme
-dontwarn org.apache.http.impl.auth.NegotiateScheme
-dontwarn org.apache.http.impl.auth.KerberosScheme
-dontwarn org.apache.http.impl.auth.SPNegoScheme
-dontwarn se.emilsjolander.stickylistheaders.StickyListHeadersListView
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator
-dontwarn com.google.common.cache.Striped64
-dontwarn com.google.common.cache.Striped64$1
-dontwarn com.google.common.cache.Striped64$Cell
-dontwarn com.google.common.primitives.UnsignedBytes$LexicographicalComparatorHolder$UnsafeComparator
-dontwarn com.google.common.primitives.UnsignedBytes$LexicographicalComparatorHolder$UnsafeComparator$1



-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5

-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}