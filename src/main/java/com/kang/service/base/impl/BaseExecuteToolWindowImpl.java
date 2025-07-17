package com.kang.service.base.impl;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.kang.service.base.IBaseExecuteToolWindow;

import javax.swing.*;

/**
 * 工具窗口相关操作 {@link com.intellij.openapi.wm.ToolWindow}
 *
 * 重写接口通用方法操作工具窗口
 *
 * @author Kang
 */
public class BaseExecuteToolWindowImpl implements IBaseExecuteToolWindow {


    /**
     * 配置一个默认值，每个 toolWindow 必须有一个实现类重写这个方法
     */
    @Override
    public void createUI(ToolWindow toolWindow) {
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Hello from MyToolWindow!"));

        Content content = ContentFactory.getInstance().createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
