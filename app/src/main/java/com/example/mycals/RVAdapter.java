package com.example.mycals;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

   private ArrayList<Meal> detailList;
   private Context context;

    public RVAdapter(ArrayList<Meal> detailList, Context context) {
        this.detailList = detailList;
        this.context = context;
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Toast toast = Toast.makeText(parent.getContext(), "You have clicked an item!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();;

                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        toast.cancel();
                    }
                }, 1000);

            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        holder.label.setText( "Meal : "+detailList.get(position).getLabel());
        holder.carbs.setText("Carbohydrates : "+detailList.get(position).getCarbs());
        holder.salt.setText("Salt : "+detailList.get(position).getSalt());
        holder.fat.setText("Fat : "+detailList.get(position).getFat());
        holder.protein.setText("Protein : "+detailList.get(position).getProtein());
        holder.fibre.setText("Fibre : "+detailList.get(position).getFibre());
        holder.cals.setText("Calories : "+detailList.get(position).getCals());
        holder.sugar.setText("Sugar : "+detailList.get(position).getSugar());

        setAnimation(holder.itemView);

    }

    public void setAnimation(View ViewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_fade_in);
        ViewToAnimate.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView carbs;
        public TextView cals;
        public TextView salt;
        public TextView fat;
        public TextView protein;
        public TextView fibre;
        public TextView sugar;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            carbs = itemView.findViewById(R.id.carbs);
            cals = itemView.findViewById(R.id.cals);
            salt = itemView.findViewById(R.id.salt);
            fat = itemView.findViewById(R.id.fat);
            protein = itemView.findViewById(R.id.protein);
            fibre = itemView.findViewById(R.id.fibre);
            sugar = itemView.findViewById(R.id.sugar);

        }
    }
}
