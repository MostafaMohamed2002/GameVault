# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.google.devtools.ksp.UtilsKt
-dontwarn com.google.devtools.ksp.processing.CodeGenerator
-dontwarn com.google.devtools.ksp.processing.Dependencies
-dontwarn com.google.devtools.ksp.processing.KSPLogger
-dontwarn com.google.devtools.ksp.processing.Resolver
-dontwarn com.google.devtools.ksp.processing.SymbolProcessor
-dontwarn com.google.devtools.ksp.processing.SymbolProcessorEnvironment
-dontwarn com.google.devtools.ksp.processing.SymbolProcessorProvider
-dontwarn com.google.devtools.ksp.symbol.ClassKind
-dontwarn com.google.devtools.ksp.symbol.KSAnnotated
-dontwarn com.google.devtools.ksp.symbol.KSAnnotation
-dontwarn com.google.devtools.ksp.symbol.KSClassDeclaration
-dontwarn com.google.devtools.ksp.symbol.KSDeclaration
-dontwarn com.google.devtools.ksp.symbol.KSFile
-dontwarn com.google.devtools.ksp.symbol.KSFunctionDeclaration
-dontwarn com.google.devtools.ksp.symbol.KSName
-dontwarn com.google.devtools.ksp.symbol.KSNode
-dontwarn com.google.devtools.ksp.symbol.KSPropertyDeclaration
-dontwarn com.google.devtools.ksp.symbol.KSReferenceElement
-dontwarn com.google.devtools.ksp.symbol.KSType
-dontwarn com.google.devtools.ksp.symbol.KSTypeAlias
-dontwarn com.google.devtools.ksp.symbol.KSTypeArgument
-dontwarn com.google.devtools.ksp.symbol.KSTypeParameter
-dontwarn com.google.devtools.ksp.symbol.KSTypeReference
-dontwarn com.google.devtools.ksp.symbol.KSValueArgument
-dontwarn com.google.devtools.ksp.symbol.KSValueParameter
-dontwarn com.google.devtools.ksp.symbol.Modifier
-dontwarn com.google.devtools.ksp.symbol.Origin
-dontwarn com.google.devtools.ksp.symbol.Variance
-dontwarn com.google.devtools.ksp.symbol.Visibility
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Modifier
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.NestingKind
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.QualifiedNameable
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ErrorType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.IntersectionType
-dontwarn javax.lang.model.type.NoType
-dontwarn javax.lang.model.type.NullType
-dontwarn javax.lang.model.type.PrimitiveType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.type.UnionType
-dontwarn javax.lang.model.type.WildcardType
-dontwarn javax.lang.model.util.AbstractTypeVisitor8
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.Elements
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor8
-dontwarn javax.lang.model.util.SimpleElementVisitor8
-dontwarn javax.lang.model.util.SimpleTypeVisitor8
-dontwarn javax.lang.model.util.Types
-dontwarn javax.tools.Diagnostic$Kind
-dontwarn javax.tools.FileObject
-dontwarn javax.tools.JavaFileManager$Location
-dontwarn javax.tools.StandardLocation
