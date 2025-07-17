package com.kang.toolwindow.base;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.kang.project.IBaseExecuteProject;
import com.kang.project.impl.BaseExecuteProjectImpl;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 基础自定义工具窗口，其他工具窗口均继承BaseToolWindow
 * 示例：
 *  1. public class ApplicationToolWindow extends BaseToolWindow {}
 *  2.<toolWindow id="开发助手"
 *                     anchor="right"
 *                     icon="AllIcons.Toolwindows.ToolWindowBuild"
 *                     factoryClass="com.kang.toolwindow.ApplicationToolWindow"/>
 *
 *
 * @author Kang
 */
public abstract class BaseToolWindow implements ToolWindowFactory {

    /**
     * 作用：当 IDE 处于“Dumb Mode”（索引尚未完成）时，返回 true 表示该 ToolWindow 仍然可用
     * 何时用：ToolWindow 完全不依赖索引即可工作（如展示日志、远程监控面板等）
     * 注意事项：只要工具不查询 PSI、不遍历文件，就返回 true
     *
     * @return true 默认返回true
     */
    @Override
    public boolean isDumbAware() {
        return true;
    }

    /**
     *  作用：根据条件决定当前打开项目中是否应该出现这个 ToolWindow 工具窗口
     *  何时用：根据项目特征动态隐藏/显示窗口（如仅 Maven 项目可见）
     *
     * @param project 当前打开项目相关信息
     * @return true 当前打开项目显示 ToolWindow 工具窗口
     */
    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    /**
     * 作用：ToolWindow 实例创建后、内容填充前调用，可设置标题、帮助 ID 等一次性属性
     * 何时用：想改标题、禁止关闭按钮等
     *
     * @param toolWindow 修改toolWindow初始化属性
     */
    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    /**
     * ** 唯一必须重写的方法
     * 作用：真正生成 ToolWindow 内部 UI 的回调，每个工具有自己UI需要重写这个方法。
     * 何时用：任何需要展示 Swing/JavaFX 组件的地方。
     *
     * @param project 当前打开的项目信息
     * @param toolWindow 工具窗口信息
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Hello from MyToolWindow!"));

        Content content = ContentFactory.getInstance().createContent(panel, "开发助手", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * ** 一般不需要重写
     * 作用：异步版本的 shouldBeAvailable，可执行耗时检查
     * 何时用：判断条件需要联网、读大量文件等耗时操作
     * 注意：返回值为 kotlin.coroutines 的挂起结果
     *
     * @param project 当前打开的项目信息
     * @param completion 是否完成
     * @return 返回 Boolean.TRUE/FALSE 或 null（使用默认实现）
     */
    @Override
    public @Nullable Object isApplicableAsync(@NotNull Project project, @NotNull Continuation<? super Boolean> completion) {
        return ToolWindowFactory.super.isApplicableAsync(project, completion);
    }

    /**
     * 当前打开项目相关操作 - BaseExecuteProjectImpl基础实现不要修改 - 需要时可重写
     *
     * @return 操作类接口
     */
    public IBaseExecuteProject getExecuteProject() {
        return new BaseExecuteProjectImpl();
    }

    /**
     * 创建工具窗口
     *
     * @param toolWindow 工具窗口
     * @param panel 窗口内布局
     * @param title 窗口 Tab 标题
     */
    public void createUI(@NotNull ToolWindow toolWindow, @NotNull JPanel panel, String title) {
        Content content = ContentFactory.getInstance().createContent(panel, title, false);
        toolWindow.getContentManager().addContent(content);
    }
}
