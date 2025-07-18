import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

// 插件声明区
plugins {
    id("java") // Java support
    alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
    alias(libs.plugins.changelog) // changelog 管理
}

// 插件信息，唯一标识和版本号
group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// 仓库配置，先走国内镜像加速仓库没有再走中央仓库
repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

// 依赖配置 - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    implementation(libs.guava)
    implementation(libs.fastjson2)
    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)

    // Gradle配置 - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        // 本地 Idea 路径
        local("D:\\idea\\ideaIU-2025.1.3.win")
        // 自动下载 Idea 调试
        // create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))

        // 配置来自：gradle.properties
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })

        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)
    }
}

// 核心配置 - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        // 插件元数据，分别是：插件市场名字、插件版本号、插件描述、新版本变更日志
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        // 插件描述。从 README.md 中截取 <!-- Plugin description --> 与 <!-- Plugin description end --> 之间的 Markdown 并转成 HTML
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        // 新版本变更日志，显示在插件市场的「What’s New」面板，使用CHANGELOG.md文件内当前版本号的改变内容
        val changelog = project.changelog
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        // 声明插件兼容的 IDE 构建号区间
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    // 签名 & 发布（环境变量）
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    //  兼容性检查
    pluginVerification {
        ides {
            recommended()
        }
    }

    // 节省构建时间，仅调试时生效，打包发布无需修改没有影响
    buildSearchableOptions = false   // 跳过 searchable-options节省构建时间
    autoReload = true              // 热重载插件
}

// changelog 插件，自动把 CHANGELOG.md 渲染成 HTML 写进 plugin.xml - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    // 清空分组或设置常用分组，都不设置会使用默认分组
    // groups.empty()
     groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    // 默认分组：listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security")
        // Added	新增功能	新特性、新模块
        // Changed	已有功能的变更	API 调整、行为变化
        // Deprecated	已弃用的功能	即将移除的接口
        // Removed	已移除的功能	破坏性变更
        // Fixed	Bug 修复	修复崩溃、逻辑错误
        // Security	安全相关	漏洞修复、依赖升级

    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
    // 在 CHANGELOG.md 顶部自动添加一个新的 [Unreleased] 空节
    keepUnreleasedSection.set(true)
    combinePreReleases.set(true)
}

// java配置
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(providers.gradleProperty("jdk").get()) // 用哪个版本 JDK 编译
    }
    withSourcesJar()      // 发布时附带源码
    withJavadocJar()      // 发布时附带 Javadoc
}

// tasks 区，保证 gradlew 版本一致，发布前自动补 changelog
tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    // 三级流水线（build → sign → verify → publish），发布前强制重新打包 & 签名
    signPlugin {
        dependsOn(buildPlugin)
    }
    verifyPlugin {
        dependsOn(signPlugin)
    }
    publishPlugin {
        dependsOn(patchChangelog)
    }
}

// 运行 UI 自动化测试
intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
