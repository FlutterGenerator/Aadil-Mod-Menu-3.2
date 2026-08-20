package com.aadil;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class CrashHandler {

    public static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER =
            Thread.getDefaultUncaughtExceptionHandler();

    private CrashHandler() {
    }

    public static void init(final Context context, boolean enabled) {

        if (!enabled) {
            return;
        }

        final Context appContext = context.getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {

                    @Override
                    public void uncaughtException(
                            Thread thread,
                            Throwable throwable) {

                        Log.e(
                                "AppCrash",
                                "Application crashed",
                                throwable
                        );

                        try {
                            tryUncaughtException(
                                    appContext,
                                    thread,
                                    throwable
                            );

                        } catch (Throwable error) {

                            Log.e(
                                    "AppCrash",
                                    "Crash handler failed",
                                    error
                            );

                            if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {

                                DEFAULT_UNCAUGHT_EXCEPTION_HANDLER
                                        .uncaughtException(
                                                thread,
                                                throwable
                                        );

                            } else {

                                System.exit(2);
                            }
                        }
                    }

                    private void tryUncaughtException(
                            Context context,
                            Thread thread,
                            Throwable throwable) {

                        StringBuilder report =
                                new StringBuilder();

                        report.append("Thread: ")
                                .append(thread != null
                                        ? thread.getName()
                                        : "unknown")
                                .append("\n\n");

                        report.append("Exception:\n");

                        report.append(
                                Log.getStackTraceString(throwable)
                        );

                        File dir =
                                new File(
                                        context.getFilesDir(),
                                        "crash"
                                );

                        File file =
                                new File(
                                        dir,
                                        "crash_"
                                                + System.currentTimeMillis()
                                                + ".txt"
                                );

                        try {

                            writeFile(
                                    file,
                                    report.toString()
                            );

                            Log.e(
                                    "AppCrash",
                                    "Crash report saved: "
                                            + file.getAbsolutePath()
                            );

                        } catch (IOException e) {

                            Log.e(
                                    "AppCrash",
                                    "Unable to save crash report",
                                    e
                            );
                        }
                    }

                    private void writeFile(
                            File file,
                            String text
                    ) throws IOException {

                        File parent =
                                file.getParentFile();

                        if (parent != null &&
                                !parent.exists()) {

                            if (!parent.mkdirs() &&
                                    !parent.exists()) {

                                throw new IOException(
                                        "Unable to create directory: "
                                                + parent
                                                .getAbsolutePath()
                                );
                            }
                        }

                        FileOutputStream output =
                                new FileOutputStream(file);

                        try {

                            output.write(
                                    text.getBytes("UTF-8")
                            );

                            output.flush();

                        } finally {

                            try {
                                output.close();
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }
        );
    }
}