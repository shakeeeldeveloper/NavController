# ------------------------------
# General AndroidX & Compose
# ------------------------------
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.constraintlayout.** { *; }
-keep class androidx.datastore.** { *; }
-keep class androidx.media3.** { *; }

# Prevent stripping annotations (important for Compose)
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# For Lottie
-keep class com.airbnb.lottie.** { *; }

# ------------------------------
# Hilt / Dagger
# ------------------------------
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }

# Keep generated Hilt code
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

# Prevent obfuscation of ViewModels using @HiltViewModel
-keepclassmembers class ** extends androidx.lifecycle.ViewModel {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# Required for javapoet (if you're using annotation processing)
-keep class com.squareup.javapoet.** { *; }

# ------------------------------
# Gson
# ------------------------------
-keep class com.google.gson.** { *; }
-keep class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ------------------------------
# ML Kit
# ------------------------------
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.internal.mlkit_vision_** { *; }
-keep class com.google.android.gms.internal.mlkit_common.** { *; }

# ------------------------------
# Coil (Image loading)
# ------------------------------
-keep class coil.** { *; }
-keep class coil.compose.** { *; }
-dontwarn coil.**

# ------------------------------
# JUnit / Espresso (Test Only)
# ------------------------------
-dontwarn org.junit.**
-dontwarn androidx.test.**
-dontwarn org.hamcrest.**

# ------------------------------
# Material 3 & UI
# ------------------------------
-keep class com.google.android.material.** { *; }
-keep class androidx.compose.material3.** { *; }

# ------------------------------
# Multidex support
# ------------------------------
-keep class androidx.multidex.** { *; }

# ------------------------------
# Accompanist
# ------------------------------
-keep class com.google.accompanist.** { *; }

# ------------------------------
# Keep Application class
# ------------------------------
-keep class com.example.navcontroller.activities.MyApp extends android.app.Application { *; }

# ------------------------------
# Keep everything in your app's base package (if needed)
# ------------------------------
-keep class com.example.navcontroller.** { *; }


# ---------------------------------------------
# Fix: Missing compiler-related classes for R8
# From: build/outputs/mapping/release/missing_rules.txt
# ---------------------------------------------
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.TypeParameterElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ErrorType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.NoType
-dontwarn javax.lang.model.type.PrimitiveType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.type.WildcardType
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor8
-dontwarn javax.lang.model.util.SimpleElementVisitor8
-dontwarn javax.lang.model.util.SimpleTypeVisitor8
-dontwarn javax.lang.model.util.Types
-dontwarn javax.tools.JavaFileObject$Kind
-dontwarn javax.tools.JavaFileObject
-dontwarn javax.tools.SimpleJavaFileObject
