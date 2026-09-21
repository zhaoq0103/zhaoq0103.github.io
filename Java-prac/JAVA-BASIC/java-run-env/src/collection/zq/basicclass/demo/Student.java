package collection.zq.basicclass.demo;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String name;
    private int age;
    private School school;
    private List<Course> allCourses;

    public Student() {
        this.allCourses = new ArrayList<>();
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public Student(String name, int age) {
        this();
        this.name = name;
        this.age = age;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
