package com.svute.snakevenom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SnakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<SnakeModel> snakeModelList;
    private IClickItemListener iClickItemListener;
    public SnakeAdapter(List<SnakeModel> snakeModelList,IClickItemListener iClickItemListener){
        this.snakeModelList = snakeModelList;
        this.iClickItemListener = iClickItemListener;
    }

    public interface IClickItemListener{
        void onClickItemIplant(SnakeModel snakeModelModel);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_snake,parent,false);
        return new SnakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SnakeModel snakeModel = snakeModelList.get(position);
        ((SnakeViewHolder) holder).bind(snakeModel);
        ((SnakeViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.onClickItemIplant(snakeModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(snakeModelList == null || snakeModelList.size() == 0){
            return 0;
        }
        return snakeModelList.size();
    }

    class SnakeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtName, txtTypeVenom;
        CardView cardView;
        public SnakeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSnake);
            txtName = itemView.findViewById(R.id.textViewName);
            cardView = itemView.findViewById(R.id.cardview);
        }
        public void bind(SnakeModel snakeModel){
            imageView.setImageResource(snakeModel.getGarbageImg());
            txtName.setText(snakeModel.getGarbageName());
        }
    }
}
