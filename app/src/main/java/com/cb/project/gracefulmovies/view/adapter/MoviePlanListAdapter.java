package com.cb.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.view.activity.MainActivity;
import com.cb.project.gracefulmovies.view.activity.MovieTableActivity;

public class MoviePlanListAdapter extends BaseRecyclerAdapter<PlanModel, MoviePlanListAdapter.PlanVH> {
    private static String moviename;

    public MoviePlanListAdapter(String moviename) {
        this.moviename = moviename;
    }

    @Override
    protected PlanVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_DATA) {
            return new MoviePlanListAdapter.PlanVH(inflater.inflate(R.layout.layout_place_holder, parent, false));
        }
        return new MoviePlanListAdapter.PlanVH(inflater.inflate(R.layout.item_movie_plan, parent, false));
    }

    @Override
    protected void onBind(final PlanVH holder, int position) {
        if (checkNoDataItemByPosition(position)) {
            ((TextView) holder.itemView.findViewById(R.id.place_holder_hint_text)).setText(isLoading()
                    ? mContext.getString(R.string.data_loading) : mContext.getString(R.string.has_no_data));
            return;
        }
        PlanModel planModel = mData.get(position);
        holder.setPlan(planModel);
        holder.text1.setText(moviename);
        holder.text2.setText(planModel.getStartTime());
        holder.text3.setText(planModel.getSumTime());
        holder.text4.setText(planModel.getHall_Name());
        holder.text5.setText(String.valueOf(planModel.getPrice()));

    }

    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1f).setDuration(400),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100, 0).setDuration(400)
        };
    }

    class PlanVH extends BaseRecyclerViewHolder {

        TextView nametext;
        TextView text1, text2, text3, text4, text5;
        PlanModel plan;

        public PlanVH(final View itemView) {
            super(itemView);


            nametext = findView(R.id.plan_name_text);
            text1 = findView(R.id.plan_text_1);
            text2 = findView(R.id.plan_text_2);
            text3 = findView(R.id.plan_text_3);
            text4 = findView(R.id.plan_text_4);
            text5 = findView(R.id.plan_text_5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), MovieTableActivity.class);

                    intent.putExtra("plan",plan);

                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public PlanModel getPlan() {
            return plan;
        }

        public void setPlan(PlanModel plan) {
            this.plan = plan;
        }
    }


}
