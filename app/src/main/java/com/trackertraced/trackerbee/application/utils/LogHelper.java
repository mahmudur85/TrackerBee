package com.trackertraced.trackerbee.application.utils;

import android.util.Log;

/**
 * Created by Mahmudur on 2/17/2015.
 * Logging Class to show formatted log
 */
public class LogHelper {
    private String mTag;
    private String mClassName;
    private boolean mActive = false;

    public static class LogTags {
        public static final String KMR = "KMR";
        public static final String PCR = "PCR";
        public static final String MR = "MR";
        public static final String AN = "AN";
        public static final String YR = "YR";
    }

    /**
     * LogHelper Constructor
     *
     * @param tag
     * @param className
     * @param active
     */
    public LogHelper(String tag, String className, boolean active) {
        if (tag != null && className != null) {
            this.mTag = tag;
            this.mClassName = className;
            this.mActive = active;
        } else {
            throw new ClassCastException(LogHelper.this.toString()
                    + " must implement set tag and className");
        }
    }

    public LogHelper(String tag, String className) {
        this(tag, className, false);
    }

    private boolean isActive() {
        return this.mActive;
    }

    public void d(Object msg) {
        if (isActive()) Log.d(this.mTag, this.mClassName + ": " + msg);
    }

    public void v(Object msg) {
        if (isActive()) Log.v(this.mTag, this.mClassName + ": " + msg);
    }

    public void i(Object msg) {
        if (isActive()) Log.i(this.mTag, this.mClassName + ": " + msg);
    }

    public void e(Object msg, Throwable trw) {
        if (isActive()) Log.e(this.mTag, this.mClassName + ": " + msg, trw);
    }

    public void e(Object msg) {
        if (isActive()) Log.e(this.mTag, this.mClassName + ": " + msg);
    }
}
