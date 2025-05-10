package gui;

public class Session {
    private static boolean isAdmin;

    public static void setIsAdmin(boolean value) {
        isAdmin = value;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }
}
