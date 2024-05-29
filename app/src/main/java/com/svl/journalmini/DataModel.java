package com.svl.journalmini;

public class DataModel {
    private String subjectName;
    private String teacherName;
    private String finishedAt;
    private String startedAt;
    private String auditory;

    public DataModel(String subjectName, String teacherName, String finishedAt, String startedAt, String auditory) {
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.finishedAt = finishedAt;
        this.startedAt = startedAt;
        this.auditory = auditory;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getAuditory() {
        return auditory;
    }

    public void setAuditory(String auditory) {
        this.auditory = auditory;
    }
}
