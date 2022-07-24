package com.ntw.notewid;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.RemoteViews;

import androidx.core.content.res.ResourcesCompat;

import static com.ntw.notewid.NewAppWidgetConfigureActivity.metrics;

public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            String[] loadPref = NewAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
            CharSequence widgetText = "";
            if (loadPref != null && loadPref.length == 5) {
                widgetText = loadPref[0];
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            metrics = context.getApplicationContext().getResources().getDisplayMetrics();

            if (loadPref != null && loadPref.length == 5) {
                Bitmap bitmap = combineImages(decodeSampledBitmapFromResource(context.getResources(), getImageColorIdent(context, Integer.parseInt(loadPref[1]), Integer.parseInt(loadPref[3])), 150, 150), textAsBitmap(context, widgetText.toString(), getTextColor(Integer.parseInt(loadPref[2])), Integer.parseInt(loadPref[4])));
                views.setImageViewBitmap(R.id.slideimg_, bitmap);
            }

            Intent configIntent = new Intent(context, NewAppWidgetConfigureActivity.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.slideimg_, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        catch (Exception e) { }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        int[] allids = appWidgetManager.getAppWidgetIds(new ComponentName(context, NewAppWidget.class));
//        for (int appWidgetId : allids) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    public static Bitmap textAsBitmap(Context context, String messageText, int color, int size) {
        Typeface font = ResourcesCompat.getFont(context, R.font.font_wid);
        TextPaint paint = new TextPaint();
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)size, metrics));
        paint.setTypeface(Typeface.create(font, Typeface.BOLD));
        paint.setColor(color);
        paint.setTextAlign(Paint.Align.LEFT);
        int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, metrics);
        int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, metrics);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        float scale = context.getResources().getDisplayMetrics().density;
        int textWidth = canvas.getWidth() - (int) (16 * scale);
        StaticLayout textLayout = new StaticLayout(messageText, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(0, 30);
        textLayout.draw(canvas);
        canvas.restore();

        return image;
    }

    public static Bitmap combineImages(Bitmap c, Bitmap s)
    {
        Bitmap cs = null;
        cs = Bitmap.createBitmap(c.getWidth(), c.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35f, metrics), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, metrics), null);
        return cs;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap yourBitmap = BitmapFactory.decodeResource(res, resId, options);
        return Bitmap.createScaledBitmap(yourBitmap, (int)(yourBitmap.getWidth() * 1.15), (int)(yourBitmap.getHeight() * 1.55), true);
    }

    public static int getTextColor(int position_color) {
        int result = 0;
        if (position_color == 0) {
            result = Color.DKGRAY;
        } else if (position_color == 1) {
            result = Color.BLUE;
        } else if (position_color == 2) {
            result = Color.GREEN;
        } else if (position_color == 3) {
            result = Color.rgb(255, 102, 0);
        } else if (position_color == 4) {
            result = Color.rgb(102, 0, 153);
        } else if (position_color == 5) {
            result = Color.RED;
        } else if (position_color == 6) {
            result = Color.WHITE;
        } else if (position_color == 7) {
            result = Color.YELLOW;
        }
        return result;
    }

    public static int getImageColorIdent(Context context, int position_image, int position_color) {
        String result = "";
        if (position_image == 0) {
            result = "sv_1_";
        } else if (position_image == 1) {
            result = "sv_3_";
        } else if (position_image == 2) {
            result = "vis_1_";
        } else if (position_image == 3) {
            result = "vis_2_";
        } else if (position_image == 4) {
            result = "spec_1_d";
        }

        if (position_image != 4) {
            if (position_color == 0) {
                result += "w";
            } else if (position_color == 1) {
                result += "d";
            } else if (position_color == 2) {
                result += "g";
            } else if (position_color == 3) {
                result += "o";
            } else if (position_color == 4) {
                result += "p";
            } else if (position_color == 5) {
                result += "r";
            } else if (position_color == 6) {
                result += "b";
            } else if (position_color == 7) {
                result += "y";
            }
        }

        return context.getApplicationContext().getResources().getIdentifier(result, "drawable", context.getApplicationContext().getPackageName());
    }
}

