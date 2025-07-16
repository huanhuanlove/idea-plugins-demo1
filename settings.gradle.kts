// 根据 gradle.properties 配置的版本自动下载jdk
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

// 根项目名称，可自定义无约束。Gradle 生成的 jar/目录名都会用这个名字，IDEA 侧边栏也显示这个名字
// 命名规则：只能用 字母、数字、下划线、连字符（不建议用中文或空格）
rootProject.name = "Develop-Assistant"

// 还可以设置多模块、子模块别名、指定插件仓库等
