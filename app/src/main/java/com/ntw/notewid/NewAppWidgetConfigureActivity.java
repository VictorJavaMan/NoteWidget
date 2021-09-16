package com.ntw.notewid;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

public class NewAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.ntw.notewid.NewAppWidget";
    public final static String WIDGET_TEXT = "widget_text";
    public final static String WIDGET_IMAGE = "widget_image";
    public final static String WIDGET_TEXTSIZE = "widget_textsize";
    public final static String WIDGET_TEXTCOLOR = "widget_textcolor";
    public final static String WIDGET_IMAGECOLOR = "widget_imagecolor";

    public static DisplayMetrics metrics;
    private ViewPager viewpager;
    private LinearLayout liner;
    private SlideAdapter myadapter;
    private TextView[] mdots;
    private Button next, back, button_about, button_clear, button_lang;
    private int mCurrentPage;
    private Spinner spinnerColorText, spinnerColorBack;
    private SeekBar seekBar;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText textArea;
    public static String[] list_description;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                final Context context = NewAppWidgetConfigureActivity.this;
                String widgetText = textArea.getText().toString();
                saveTitlePref(context, mAppWidgetId, widgetText, viewpager.getCurrentItem(), seekBar.getProgress(), spinnerColorText.getSelectedItemPosition(), spinnerColorBack.getSelectedItemPosition());
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                NewAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
            catch (Exception e) { }
        }
    };

    public NewAppWidgetConfigureActivity() {
        super();
    }

    static void saveTitlePref(Context context, int appWidgetId, String text, int image, int textsize, int textcolor, int imagecolor) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(WIDGET_TEXT + appWidgetId, text);
        prefs.putInt(WIDGET_IMAGE + appWidgetId, image + 1);
        prefs.putInt(WIDGET_TEXTSIZE + appWidgetId, textsize + 1);
        prefs.putInt(WIDGET_TEXTCOLOR + appWidgetId, textcolor + 1);
        prefs.putInt(WIDGET_IMAGECOLOR + appWidgetId, imagecolor + 1);
        prefs.apply();
    }

    static String[] loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String[] strings = {"", "", "", "", ""};
        strings[0] = prefs.getString(WIDGET_TEXT + appWidgetId, null);
        strings[1] = Integer.toString(prefs.getInt(WIDGET_IMAGE + appWidgetId, 0) - 1);
        strings[2] = Integer.toString(prefs.getInt(WIDGET_TEXTCOLOR + appWidgetId, 0) - 1);
        strings[3] = Integer.toString(prefs.getInt(WIDGET_IMAGECOLOR + appWidgetId, 0) - 1);
        strings[4] = Integer.toString(prefs.getInt(WIDGET_TEXTSIZE + appWidgetId, 0) - 1);
        return strings;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(WIDGET_TEXT + appWidgetId);
        prefs.remove(WIDGET_IMAGE + appWidgetId);
        prefs.remove(WIDGET_TEXTSIZE + appWidgetId);
        prefs.remove(WIDGET_TEXTCOLOR + appWidgetId);
        prefs.remove(WIDGET_IMAGECOLOR + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        setResult(RESULT_CANCELED);
        setContentView(R.layout.new_app_widget_configure);
        textArea = findViewById(R.id.textArea);
        seekBar = findViewById(R.id.seekBar);
        findViewById(R.id.button_execute_check_).setOnClickListener(mOnClickListener);

        viewpager = findViewById(R.id.viewpager);
        liner = findViewById(R.id.dots);

        next = findViewById(R.id.nextBtn);
        back = findViewById(R.id.backBtn);
        button_about = findViewById(R.id.button_about);
        button_clear = findViewById(R.id.button_clear);
        button_lang = findViewById(R.id.button_lang);

        myadapter = new SlideAdapter(this);
        viewpager.setAdapter(myadapter);
        adddots(0);

        viewpager.addOnPageChangeListener(viewlistener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCurrentPage + 1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCurrentPage - 1);
            }
        });

        button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAppWidgetConfigureActivity.this);
                builder.setTitle(getResources().getString(R.string.title_alert)).setMessage(getResources().getString(R.string.body_alert)).setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
            }
        });

        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textArea.setText("");
            }
        });

        button_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = textArea.getText().toString();
                textArea.setText(TableSymbols.convertLang(str));
                textArea.setSelection(textArea.getText().length());
            }
        });


        Resources resources = NewAppWidgetConfigureActivity.this.getResources();
        String[] data_textcolor = { resources.getString(R.string.l_gray), resources.getString(R.string.l_blue), resources.getString(R.string.l_green), resources.getString(R.string.l_orange), resources.getString(R.string.l_purple),  resources.getString(R.string.l_red),  resources.getString(R.string.l_white),  resources.getString(R.string.l_yellow) };
        String[] data_backcolor = { resources.getString(R.string.l_white), resources.getString(R.string.l_gray), resources.getString(R.string.l_green), resources.getString(R.string.l_orange), resources.getString(R.string.l_purple),  resources.getString(R.string.l_red),  resources.getString(R.string.l_blue),  resources.getString(R.string.l_yellow) };
        list_description = new String[]{resources.getString(R.string.imtext_1), resources.getString(R.string.imtext_2), resources.getString(R.string.imtext_3), resources.getString(R.string.imtext_4)};

        ArrayAdapter<String> adapterColorText = new ArrayAdapter<>(this, R.layout.spinner_list, data_textcolor);
        adapterColorText.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColorText = (Spinner) findViewById(R.id.spinner_colortext);
        spinnerColorText.setAdapter(adapterColorText);
        spinnerColorText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> adapterColorBack = new ArrayAdapter<>(this, R.layout.spinner_list, data_backcolor);
        adapterColorBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColorBack = (Spinner) findViewById(R.id.spinner_colorback);
        spinnerColorBack.setAdapter(adapterColorBack);
        spinnerColorBack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        String[] loadPref = NewAppWidgetConfigureActivity.loadTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId);
        if (loadPref != null && loadPref.length == 5) {
            textArea.setText(loadPref[0]);
            viewpager.setCurrentItem((Integer.parseInt(loadPref[1]) == -1) ? 0 : Integer.parseInt(loadPref[1]));
            spinnerColorText.setSelection((Integer.parseInt(loadPref[2]) == -1) ? 0 : Integer.parseInt(loadPref[2]));
            spinnerColorBack.setSelection((Integer.parseInt(loadPref[3]) == -1) ? 0 : Integer.parseInt(loadPref[3]));
            seekBar.setProgress((Integer.parseInt(loadPref[4]) == -1) ? 45 : Integer.parseInt(loadPref[4]));
        }

    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            adddots(position);
            mCurrentPage = position;
            if (position == 0) {
                next.setText(">");
                back.setText("");
            } else if (position == mdots.length - 1) {
                next.setText("");
                back.setText("<");
            } else {
                next.setText(">");
                back.setText("<");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void adddots(int i) {
        mdots = new TextView[4];
        liner.removeAllViews();
        for (int x = 0; x < mdots.length; x++) {
            mdots[x] = new TextView(this);
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(40);
            mdots[x].setTextColor(Color.BLACK);
            liner.addView(mdots[x]);
        }
        if (mdots.length > 0) {
            mdots[i].setTextColor(Color.GRAY);
        }
    }
}

