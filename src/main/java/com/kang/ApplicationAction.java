package com.kang;

import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.Messages;
import com.kang.i18n.MyBundle;

public class ApplicationAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 1. 弹窗
        Messages.showMessageDialog(
                MyBundle.message("popup.message"),
                MyBundle.message("popup.title"),
                Messages.getInformationIcon()
        );

        // 2. 通知（右下角气泡）
        NotificationGroup group = NotificationGroupManager.getInstance().getNotificationGroup("notification.event01Notification");
        if (group != null) {
            Notification notification = group.createNotification(
                    MyBundle.message("notification.title"),
                    MyBundle.message("notification.content"),
                    NotificationType.INFORMATION
            );
            notification.notify(e.getProject());
        }
    }
}
