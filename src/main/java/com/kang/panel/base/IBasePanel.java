package com.kang.panel.base;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * 窗口布局基类
 *
 * @author Kang
 */
public abstract class IBasePanel {

    /**
     * 绘制工具窗口 UI 界面 - 必须重写
     */
    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Hello from MyToolWindow!"));
        return panel;
    };

}
