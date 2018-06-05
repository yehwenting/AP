package myandroidhello.com.ap_project.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehwenting on 2018/4/25.
 */

public class Competitions {
    private String name;
    private String content;
    private String num;
    private String deadline;
    private String goal;
    private Boolean isExpendable;
    private List<CompetitionGroup> competitions=new ArrayList<>();
    private List<CompetitionGroup> competitionsRecord=new ArrayList<>();


    public Competitions(String name, String content, String num, String deadline, String goal,Boolean isExpendable,
                        List<CompetitionGroup> competitions,List<CompetitionGroup> competitionsRecord) {
        this.name = name;
        this.content = content;
        this.num = num;
        this.deadline = deadline;
        this.goal = goal;
        this.isExpendable=isExpendable;
        this.competitions=competitions;
        this.competitionsRecord=competitionsRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Boolean getExpendable() {
        return isExpendable;
    }

    public void setExpendable(Boolean expendable) {
        isExpendable = expendable;
    }

    public List<CompetitionGroup> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<CompetitionGroup> competitions) {
        this.competitions = competitions;
    }

    public List<CompetitionGroup> getCompetitionsRecord() {
        return competitionsRecord;
    }

    public void setCompetitionsRecord(List<CompetitionGroup> competitionsRecord) {
        this.competitionsRecord = competitionsRecord;
    }
}
