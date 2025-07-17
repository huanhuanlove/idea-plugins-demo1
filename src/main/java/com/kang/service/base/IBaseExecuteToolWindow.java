package com.kang.service.base;

import com.intellij.openapi.wm.ToolWindow;

/**
 * 工具窗口相关操作 {@link com.intellij.openapi.wm.ToolWindow}
 *
 * @author Kang
 */
public interface IBaseExecuteToolWindow {

    /**
     * 绘制工具窗口 UI 界面
     */
    void createUI(ToolWindow toolWindow);

}
