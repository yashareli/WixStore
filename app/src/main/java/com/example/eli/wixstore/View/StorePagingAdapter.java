package com.example.eli.wixstore.View;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eli.wixstore.Logic.NetWork.ProductData;
import com.example.eli.wixstore.R;
import com.example.eli.wixstore.View.PagingListSourceCode.PagingBaseAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DecimalFormat;


public class StorePagingAdapter extends PagingBaseAdapter<ProductData> {

    private int mResourceId;
    private Context mContext;
    private Transformation mTransformation;
    private int mRowHeight;

    public StorePagingAdapter (Context context, int resourceId) {
        mResourceId = resourceId;
        mContext = context;
        mTransformation = new RoundedCornersTransformation(30, 5);
        mRowHeight =0;
    }
    @Override
    public int getCount() {
        return items==null ?0 :items.size();
    }

    @Override
    public ProductData getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder ;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(mResourceId, null);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mPrice.setText(parsePrice(position));
        viewHolder.mTitle.setText(getTitle(position));
        int height = getItemHeight();

        Picasso.with(mContext).load(getItem(position).getImageLink())
                .placeholder(R.drawable.placeholder).resize(height, height).centerCrop().transform(mTransformation)
                .into(viewHolder.mImageView);
        return convertView;
    }
    private int getItemHeight() {
        if(mRowHeight ==0){
            TypedValue value = new TypedValue();
            DisplayMetrics metrics = new DisplayMetrics();

            mContext.getTheme().resolveAttribute(
                    android.R.attr.listPreferredItemHeight, value, true);
            ((WindowManager) (mContext.getSystemService(Context.WINDOW_SERVICE)))
                    .getDefaultDisplay().getMetrics(metrics);

           mRowHeight = (int)TypedValue.complexToDimension(value.data, metrics);
        }
        return mRowHeight;
    }
    private String getTitle(int position) {
        String title = getItem(position).getTitle();
        if(title ==null || title.equals("")){
            return "Cool Dish";
        }
        return title;
    }
    private String parsePrice(int position) {
        try{
            String priceStr  = getItem(position).getPrice();
            double priceDouble = Double.parseDouble(priceStr);
            return new DecimalFormat("$##.##").format(priceDouble);
        }catch(NumberFormatException e) {

        }
        return "$XX.XX";
    }

    private static class ViewHolder{
        private TextView mTitle;
        private TextView mPrice;
        private ImageView mImageView;
    }
}
