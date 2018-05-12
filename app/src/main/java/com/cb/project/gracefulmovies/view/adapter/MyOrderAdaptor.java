package com.cb.project.gracefulmovies.view.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.Order;

public class MyOrderAdaptor extends BaseRecyclerAdapter<Order,MyOrderAdaptor.orderVH> {
    @Override
    protected orderVH onCreate(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_NO_DATA) {
            return new MyOrderAdaptor.orderVH(inflater.inflate(R.layout.layout_place_holder, parent, false));
        }
        return new MyOrderAdaptor.orderVH(inflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    protected void onBind(orderVH holder, int position) {
        if (checkNoDataItemByPosition(position)) {
            ((TextView) holder.itemView.findViewById(R.id.place_holder_hint_text)).setText(isLoading()
                    ? mContext.getString(R.string.data_loading) : mContext.getString(R.string.has_no_data));
            return;
        }
        Order order=mData.get(position);

        holder.text1.setText(order.getMovie_Name());
        holder.text2.setText(order.getStartTime());
        holder.text3.setText(order.getSumtime());
        holder.text4.setText(order.getPrice()+"");
        holder.text5.setText(order.getState());
        holder.text6.setText(order.getVerifyCode());
        holder.text7.setText(order.getOrder_id()+"");
        holder.text8.setText(order.getHall_name()+"");
        String[] seatNumber = order.getSeatNumber().split("-");
        String seat=seatNumber[0]+"排"+seatNumber[1]+"座";
        holder.text9.setText(seat);
    }
    @Override
    protected Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1f).setDuration(400),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 100, 0).setDuration(400)
        };
    }





    class orderVH extends BaseRecyclerViewHolder{
        TextView text1,text2,text3,text4,text5,text6,text7,text8,text9;

        public orderVH(View itemView) {
            super(itemView);
            this.text1=findView(R.id.order_text_1);
            this.text2=findView(R.id.order_text_2);
            this.text3=findView(R.id.order_text_3);
            this.text4=findView(R.id.order_text_4);
            this.text5=findView(R.id.order_text_5);
            this.text6=findView(R.id.order_text_6);
            this.text7=findView(R.id.order_text_7);
            this.text8=findView(R.id.order_text_8);
            this.text9=findView(R.id.order_text_9);

        }


    }
}
