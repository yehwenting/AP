package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import myandroidhello.com.ap_project.Activity.CompeteGroupMemberActivity;
import myandroidhello.com.ap_project.Model.CompetitionGroup;
import myandroidhello.com.ap_project.R;

public class CompetitionGroupAdapter1 extends  RecyclerView.Adapter<CompetitionGroupAdapter1.CompetitionViewHolder> {
    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        TextView rank,cga_name,goal,num;
        ImageView pic,more;

        public CompetitionViewHolder(View itemView) {
            super(itemView);

            cga_name=itemView.findViewById(R.id.cga_name);
            num=itemView.findViewById(R.id.cga_num);
            rank=itemView.findViewById(R.id.rank);
            pic=itemView.findViewById(R.id.groupImg);
            goal=itemView.findViewById(R.id.goal);
            more=itemView.findViewById(R.id.more);

        }
    }

    private List<CompetitionGroup> competitionGroups;
    Context context;

    public CompetitionGroupAdapter1(List<CompetitionGroup> competitions) {
        this.competitionGroups = competitions;
        Log.d("hhhhh","hello");

    }

    @NonNull
    @Override
    public CompetitionGroupAdapter1.CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_competition_group1,null);

        return new CompetitionGroupAdapter1.CompetitionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CompetitionGroupAdapter1.CompetitionViewHolder holder, int position) {
        final CompetitionGroup competitionGroup=competitionGroups.get(position);
        Log.d("hhhhh","hello");
//            if(competitionGroup.getStatus().equals("true")){
        holder.cga_name.setText(competitionGroup.getCpName());
        holder.goal.setText(competitionGroup.getRemainGoal());
        holder.num.setText(competitionGroup.getRemain());
        holder.more.setOnClickListener(view -> {
            Intent intent=new Intent(context, CompeteGroupMemberActivity.class);
            intent.putExtra("group_number",competitionGroup.getCid());
            context.startActivity(intent);
        });
        int num=position+1;
        String t="No."+num;
        holder.rank.setText(t);
        displayProfilePic(holder.pic,competitionGroup.getuPic());
        Log.d("ttttt",competitionGroup.getCid());

    }

    @Override
    public int getItemCount() {
        Log.d("hhhhh",String.valueOf(competitionGroups.size()));
        return competitionGroups.size();
    }


    private void displayProfilePic(ImageView imageView, String url) {

        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }

}
