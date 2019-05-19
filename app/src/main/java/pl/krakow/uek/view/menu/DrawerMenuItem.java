package pl.krakow.uek.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import pl.krakow.uek.R;


public enum DrawerMenuItem {
    TODAY_TASKS(R.string.home, R.drawable.ic_home_outline_grey),
    CALENDAR(R.string.calendar, R.drawable.ic_outline_calendar),
    SCANNER(R.string.scanner, R.drawable.ic_outline_camera),
    EMPTY(0, 0),
    SING_OUT(R.string.sign_out, R.drawable.ic_logout_outline_grey);

    private final int title;
    private final int icon;

    DrawerMenuItem(int title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    @Nullable
    public Drawable getIcon(Context context) {
        if (this.equals(EMPTY)) {
            return null;
        }
        return ContextCompat.getDrawable(context, icon);
    }

    @Nullable
    public String getTitle(Context context) {
        if (this.equals(EMPTY)) {
            return null;
        }
        return context.getString(title);
    }

    private int color(Context context, int color) {
        return ContextCompat.getColor(context, color);
    }

    public DrawerItem createItem(Context context) {
        if (this.equals(EMPTY)) {
            return new SpaceItem(48);
        }

        return new SimpleItem(getIcon(context), getTitle(context))
                .withIconTint(color(context, R.color.colorPrimary))
                .withTextTint(color(context, R.color.colorPrimary))
                .withSelectedIconTint(color(context, R.color.colorAccent))
                .withSelectedTextTint(color(context, R.color.colorAccent));
    }
}
