package com.li.knowledgefarm.Shop;

import android.content.Context;
import android.widget.PopupWindow;

public class PetItemPopUpWindow extends PopupWindow {
    private Context context;

    public PetItemPopUpWindow(Context context) {
        super(context);
        this.context = context;
    }
}
