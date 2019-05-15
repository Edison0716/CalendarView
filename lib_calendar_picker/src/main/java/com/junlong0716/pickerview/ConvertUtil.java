package com.junlong0716.pickerview;

import android.content.Context;

public class ConvertUtil {
    public static int dp2px(float dpValue, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
