package com.ntw.notewid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import static com.ntw.notewid.NewAppWidgetConfigureActivity.list_description;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] list_images = { R.drawable.sv_1_d, R.drawable.sv_3_d, R.drawable.vis_1_d, R.drawable.vis_2_d, R.drawable.spec_1_d };

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == (LinearLayout) obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container, false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView img = (ImageView) view.findViewById(R.id.slideimg);
        TextView txt1 = (TextView) view.findViewById(R.id.slidetitle);

        img.setImageResource(list_images[position]);
        txt1.setText(list_description[position]);
        linearLayout.setBackgroundColor(Color.WHITE);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
