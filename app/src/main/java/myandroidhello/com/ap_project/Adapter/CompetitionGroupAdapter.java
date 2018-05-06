package myandroidhello.com.ap_project.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.CompetitionGroup;

public class CompetitionGroupAdapter extends  RecyclerView.Adapter<CompetitionGroupAdapter.CompetitionViewHolder> {
    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        TextView gname,uname,num,time,place,remain,note;
        Button join;
        ImageView pic;

        public CompetitionViewHolder(View itemView) {
            super(itemView);

            gname=itemView.findViewById(R.id.cga_name);
            uname=itemView.findViewById(R.id.uName);
            num=itemView.findViewById(R.id.cga_num);
            time=itemView.findViewById(R.id.cga_time);
            place=itemView.findViewById(R.id.cga_place);
            remain=itemView.findViewById(R.id.cga_remain);
            note=itemView.findViewById(R.id.cga_note);
            join=itemView.findViewById(R.id.cga_create_button);
            pic=itemView.findViewById(R.id.groupImg);

        }
    }

    private List<CompetitionGroup> competitionGroups;
    Context context;

    public CompetitionGroupAdapter(List<CompetitionGroup> competitions) {
        this.competitionGroups = competitions;
        Log.d("hhhhh","hello");

    }

    @NonNull
    @Override
    public CompetitionGroupAdapter.CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_competition_group,null);

        return new CompetitionGroupAdapter.CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionGroupAdapter.CompetitionViewHolder holder, int position) {
            final CompetitionGroup competitionGroup=competitionGroups.get(position);
            Log.d("hhhhh","hello");
            holder.gname.setText(competitionGroup.getCpName());
            holder.uname.setText(competitionGroup.getuName());
            holder.time.setText(competitionGroup.getTime());
            holder.place.setText(competitionGroup.getPlace());
            holder.note.setText(competitionGroup.getNote());
            holder.remain.setText(competitionGroup.getRemain());
    }

    @Override
    public int getItemCount() {
        Log.d("hhhhh",String.valueOf(competitionGroups.size()));
        return competitionGroups.size();
    }
}
