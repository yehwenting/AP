package myandroidhello.com.ap_project.model;

/**
 * Created by Yehwenting on 2018/2/4.
 */

public class Item {
    private String text,subtext;
    private Boolean isExpendable;

    public Item(String text, String subtext, Boolean expendable) {
        this.text = text;
        this.subtext = subtext;
        isExpendable = expendable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public Boolean getExpendable() {
        return isExpendable;
    }

    public void setExpendable(Boolean expendable) {
        isExpendable = expendable;
    }
}
