package org.bangeek.whichway.utils;

import java.io.Closeable;

/**
 * Created by BinGan on 2016/9/7.
 */
public class CommonUtil {
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static boolean isEqual(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
}
