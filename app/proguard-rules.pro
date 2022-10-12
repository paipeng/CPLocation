# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/paipeng/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# -keep public class * extends android.app.Activity
#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
-keep class com.paipeng.dao.**{ *;}
-keep class com.paipeng.bean.**{ *;}
-keep class com.paipeng.adapterData.**{ *;}

-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**

-keep class * extends java.lang.annotation.Annotation {*;}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn com.google.json.**
-keep class com.google.json.** { *;}

-dontwarn com.j256.ormlite.**
-keep class com.j256.ormlite.** { *;}

-dontwarn org.apache.commons.**
-keep class org.apache.commons.** { *;}
-dontwarn org.apache.commons.**
-keep class org.apache.commons.** { *;}


-dontwarn com.orhanobut.**
-keep class com.orhanobut.**{ *;}

-dontwarn com.github.hotchemi.**
-keep class com.github.hotchemi.**{ *;}

-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{ *;}

-dontwarn eu.the4thfloor.volley.**
-keep class eu.the4thfloor.volley.**{ *;}

-dontwarn com.facebook.fresco.**
-keep class com.facebook.fresco.**{ *;}

-dontwarn com.android.support.**
-keep class com.android.support.**{ *;}



-ignorewarnings
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

-keepattributes *Annotation*




# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile