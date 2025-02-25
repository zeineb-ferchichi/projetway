package util;

import java.awt.*;

public class WindowsNotificationUtil {

    public static void showWindowsNotification(String title, String message) {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                TrayIcon trayIcon = new TrayIcon(image, "Notification");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Gestion des Transports");
                tray.add(trayIcon);

                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Les notifications ne sont pas supportées sur ce système.");
        }
    }
}
