package com.kang.i18n;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * 创建单例国际化语言获取类
 *
 * 使用示例：
 *      1.MyBundle.message("random.num");
 *      2.MyBundle.message("random.label", 5);
 */
public final class MyBundle extends DynamicBundle {

    private static final String BUNDLE = "messages.MyBundle";
    private static final MyBundle INSTANCE = new MyBundle();

    private MyBundle() { super(BUNDLE); }

    /**
     * 根据 ID 获取配置的语言内容
     * @param key MyBundle.properties文件中的 key (key=value)
     * @param params 可变参数，替换多语言中的占位符 - {}
     * @return MyBundle.properties文件中的 value (key=value)
     */
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
