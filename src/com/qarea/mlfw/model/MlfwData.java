package com.qarea.mlfw.model;

import java.io.Serializable;

import android.database.Cursor;

public abstract class MlfwData implements Serializable {
    /**
     * Local variable is serialVersionUID with type long
     */
    private static final long serialVersionUID = -2883997389349412976L;

    public MlfwData(Cursor cursor) {
        fromCursor(cursor);
    }

    public MlfwData() {

    }

    protected abstract void fromCursor(Cursor cursor);

}
