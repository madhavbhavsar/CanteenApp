package com.madapps.canteen.owner.facultyactivity;

public class FacultyModel {

    String fid;
    String facultyname;

    public FacultyModel() {
    }

    public FacultyModel(String fid, String facultyname) {
        this.fid = fid;
        this.facultyname = facultyname;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFacultyname() {
        return facultyname;
    }

    public void setFacultyname(String facultyname) {
        this.facultyname = facultyname;
    }
}
