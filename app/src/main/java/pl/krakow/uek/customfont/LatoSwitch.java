package pl.krakow.uek.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Switch;

public class LatoSwitch extends Switch {

    public LatoSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoSwitch(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }
}
