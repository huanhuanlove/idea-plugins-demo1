package com.kang;

import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.Messages;

public class ApplicationAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 1. 弹窗
        Messages.showMessageDialog(
                "Hello from IntelliJ IDEA Plugin!",
                "Demo Popup",
                Messages.getInformationIcon()
        );

        // 2. 通知（右下角气泡）
        NotificationGroupManager groupManager = NotificationGroupManager.getInstance();
        NotificationGroup group = groupManager.getNotificationGroup("notification.event01Notification");
        Notification notification = group.createNotification(
                "Demo Notification",
                "Your action has been executed successfully!",
                NotificationType.INFORMATION
        );
        notification.notify(e.getProject());
    }
}
