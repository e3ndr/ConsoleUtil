package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

class Formatting {

    static String parseFormat(@Nullable Object object, @Nullable Object... args) {
        if (object == null) {
            return "null";
        } else if (object instanceof Throwable) {
            return getExceptionStack((Throwable) object);
        } else if (object.getClass().isArray()) {
            return arrayAsString(object);
        } else if ((args == null) || (args.length == 0)) {
            return String.valueOf(object);
        } else {
            // Loop through the args and...
            // - replace Throwables with their stacktraces
            // - replace Arrays with their toString versions.
            for (int argIndex = 0; argIndex < args.length; argIndex++) {
                Object arg = args[argIndex];

                if (arg != null) {
                    if (arg instanceof Throwable) {
                        args[argIndex] = getExceptionStack((Throwable) arg);
                    } else if (arg.getClass().isArray()) {
                        args[argIndex] = arrayAsString(arg);
                    }
                }
            }

            return String.format(String.valueOf(object), args);
        }
    }

    private static String arrayAsString(@NonNull Object array) {
        int arrLen = Array.getLength(array);
        String[] asString = new String[arrLen];

        for (int idx = 0; idx < arrLen; idx++) {
            Object o = Array.get(array, idx);

            asString[idx] = String.valueOf(o);
        }

        return '[' + String.join(", ", asString) + ']';
    }

    private static String getExceptionStack(@NonNull Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        String out = sw.toString();

        pw.flush();
        pw.close();
        sw.flush();

        return out
            .substring(0, out.length() - 2)
            .replace("\r", "");
    }

}
