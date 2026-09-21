package collection.zq.basicclass.demo;

import java.util.ArrayList;
import java.util.List;



public class Course {

    Object o = null;
    private String name;
    private int credit;
    private List<Student> allStudents;

    public Course() {
        this.allStudents = new ArrayList<>();
    }

    public Course(String name, int credit) {
        this();
        this.name = name;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public List<Student> getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(List<Student> allStudents) {
        this.allStudents = allStudents;
    }
}
