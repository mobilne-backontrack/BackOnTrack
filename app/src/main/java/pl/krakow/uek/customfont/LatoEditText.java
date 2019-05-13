package pl.krakow.uek.customfont;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class LatoEditText extends AppCompatEditText {

    public LatoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoEditText(Context context) {
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