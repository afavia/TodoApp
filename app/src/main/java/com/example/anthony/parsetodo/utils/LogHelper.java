package com.example.anthony.parsetodo.utils;

import android.util.Log;

/**
 * Created by anthony on 8/12/15.
 */
public class LogHelper {

    static String LOG_TAG = "ParseTodo";

    public static void configure(String logTag) {
        LOG_TAG = logTag;
    }

    public static void logThreadId(String message) {
        long processId = android.os.Process.myPid();
        long threadId = Thread.currentThread().getId();
        Log.d(LOG_TAG, String.format("[ Process: %d | Thread: %d] %s", processId, threadId, message));
    }
}
