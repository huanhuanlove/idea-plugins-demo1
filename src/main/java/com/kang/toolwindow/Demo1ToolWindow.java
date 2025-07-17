package com.kang.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.kang.panel.DemoPanel;
import com.kang.toolwindow.base.BaseToolWindow;
import org.jetbrains.annotations.NotNull;

/**
 * 工具窗口
 *
 * @author Kang
 */
public class Demo1ToolWindow extends BaseToolWindow {

    /**
     * 重写窗口UI绘制方法
     *
     * @param project 当前打开的项目信息
     * @param toolWindow 工具窗口信息
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 操作项目
        getExecuteProject().preExecute(project);

        // 工具窗口UI绘制
        createUI(toolWindow, new DemoPanel().createPanel(), "开发助手");
    }

}
