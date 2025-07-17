package com.kang.action.base;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Action基类
 *
 * @author Kang
 */
public abstract class BaseAction extends AnAction {

    /**
     * action需要执行的逻辑
     *
     * @param event 事件
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        executeAction(event);
    }

    /**
     * 默认实现action需要执行的逻辑 - 必须重写
     *
     * @param event 事件
     */
    public void executeAction(@NotNull AnActionEvent event) {
        // 1. 弹窗
        Messages.showMessageDialog(
                "A message from a pop-up window, please check it out！",
                "演示弹窗",
                Messages.getInformationIcon()
        );

        // 2. 通知（右下角气泡）
        NotificationGroup group = NotificationGroupManager.getInstance().getNotificationGroup("notification.event01Notification");
        if (group != null) {
            Notification notification = group.createNotification(
                    "Notification",
                    "The operation has been successfully executed!",
                    NotificationType.INFORMATION
            );
            notification.notify(event.getProject());
        }
    }

}
