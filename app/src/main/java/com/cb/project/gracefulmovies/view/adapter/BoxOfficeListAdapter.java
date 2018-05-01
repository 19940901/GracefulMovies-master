package com.cb.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cb.project.gracefulmovies.model.BoxOfficeModel;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.BoxOfficeModel;

import org.polaric.colorful.Colorful;

/**
 * plan列表适配器
 * <p/>
 * Created by cb
 */
public class  BoxOfficeListAdapter extends BaseRecyclerAdapter<BoxOfficeModel, BoxOfficeListAdapter.BoxOfficeVH> {


    @Override
    protected BoxOfficeVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_DATA) {
            return new BoxOfficeVH(inflater.inflate(R.layout.layout_place_holder, parent, false));
        }
        return new BoxOfficeVH(inflater.inflate(R.layout.item_box_office, parent, false));
    }

    @Override
    protected void onBind(final BoxOfficeVH holder, int position) {
        if (checkNoDataItemByPosition(position)) {
            ((TextView) holder.itemView.findViewById(R.id.place_holder_hint_text)).setText(isLoading()
                    ? mContext.getString(R.string.data_loading) : mContext.getString(R.string.has_no_data));
            return;
        }

        BoxOfficeModel model = mData.get(position);

        if (model.getRankInt() == 1) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_gold_metal);
            holder.rankText.setText("");
        } else if (model.getRankInt() == 2) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_silver_metal);
            holder.rankText.setText("");
        } else if (model.getRankInt() == 3) {
            holder.rankImg.setVisibility(View.VISIBLE);
            holder.rankImg.setImageResource(R.drawable.svg_ic_bronze_metal);
            holder.rankText.setText("");
        } else {
            holder.rankImg.setVisibility(View.INVISIBLE);
            holder.rankText.setText(model.getRank());
        }
        holder.nameText.setText(model.getName());

        holder.text1.setTextColor(ContextCompat.getColor(mContext,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        holder.text2.setTextColor(ContextCompat.getColor(mContext,
                Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        holder.text1.setText(model.getBoxOffice());
        holder.text2.setText(model.getSumBoxOffice());
        try {
            int day = Integer.parseInt(model.getMovieDays());
            if (day == 0) {
                holder.dayHintText.setText("上映日期");
                holder.text3.setText("今天");
                holder.text3.setTextSize(16);
            } else if (day > 0) {
                holder.dayHintText.setText("上映天数");
                holder.text3.setText(String.valueOf(day));
                holder.text3.setTextSize(24);
            } else {
                holder.dayHintText.setText("距离上映");
                holder.text3.setText(String.valueOf(-day));
                holder.text3.setTextSize(24);
            }
        } catch (Exception e) {
            holder.dayHintText.setText("上映天数");
            holder.text3.setText(model.getMovieDays());
            holder.text3.setTextSize(24);
        }
        holder.text4.setText(model.getBoxPer());
        String text=model.getTime().substring(11);
        holder.text5.setText(text);
    }

    class BoxOfficeVH extends BaseRecyclerViewHolder {

        AppCompatImageView rankImg;
        TextView rankText;
        TextView nameText;
        TextView text1, text2, text3, text4, text5;
        TextView dayHintText;

        BoxOfficeVH(View itemView) {
            super(itemView);

            rankImg = findView(R.id.bo_rank_img);
            rankText = findView(R.id.bo_rank_text);
            nameText = findView(R.id.bo_name_text);
            text1 = findView(R.id.bo_text_1);
            text2 = findView(R.id.bo_text_2);
            text3 = findView(R.id.bo_text_3);
            text4 = findView(R.id.bo_text_4);
            text5 = findView(R.id.bo_text_5);
            dayHintText = findView(R.id.bo_hint_3);
        }
    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1f).setDuration(400),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100, 0).setDuration(400)
        };
    }
}
