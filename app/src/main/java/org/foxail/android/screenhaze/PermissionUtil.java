package org.foxail.android.screenhaze;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class PermissionUtil {

    // Linux command of grant permission android.permission.WRITE_SECURE_SETTINGS to this app
    private final static String GRANT_PERMISSION = "pm grant org.foxail.android.screenhaze android.permission.WRITE_SECURE_SETTINGS";

    // grant permission
    public static boolean grantPermission(){
        return execRootCmd(GRANT_PERMISSION);
    }

    // check if this device have been rooted
    public static boolean isRooted() {
        return execRootCmd("echo test");
    }

    // execute Linux command
    private static boolean execRootCmd(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            command = command + "\n";
            dataOutputStream.writeBytes(command);
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            int result = process.exitValue();
            return (result != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
