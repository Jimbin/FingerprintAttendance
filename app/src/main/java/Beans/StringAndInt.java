package Beans;

/**
 * Created by Jim斌 on 2017/7/1.
 */

public class StringAndInt {
    public String Name;
    public int ID;
    public boolean view_flag;

    public StringAndInt(String Name,int ID, boolean view_flag){
        this.Name=Name;
        this.ID=ID;
        this.view_flag=view_flag;
    }

    public boolean isView_flag() {
        return view_flag;
    }

    public void setView_flag(boolean view_flag) {
        this.view_flag = view_flag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
