package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.MenuOption;
import com.etsisi.dev.etsisicrowdsensing.R;

public class CampusGridAdapter extends BaseAdapter{
    private final Context mContext;
    private final MenuOption[] options;

    public CampusGridAdapter(Context context, MenuOption[] options){
        this.mContext = context;
        this.options = options;
    }
    @Override
    public int getCount() {
        return options.length;
    }

    @Override
    public Object getItem(int position) {
        return options[position];
    }

    @Override
    public long getItemId(int position) {
        return options[position].getName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Find the proper option
        final MenuOption option = options[position];

        // If convertView is null, you instantiate a new cell view by using a LayoutInflater
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.relativelayout_campus_option, null);
        }

        // Create references for each individual view created in the XML layout file
        final ImageView optionImage = (ImageView) convertView.findViewById(R.id.optionImage);
        final TextView optionName = (TextView)convertView.findViewById(R.id.optionName);
        final RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);


        // Set the option’s image and name using the above references.
        optionImage.setImageResource(option.getImageResource());
        optionName.setText(mContext.getString(option.getName()));

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(ContextCompat.getColor(mContext, option.getColor()));
        gd.setCornerRadius(30);
        relativeLayout.setBackground(gd);
        return convertView;
    }
}
