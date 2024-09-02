package com.th.pm.constant;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum Status {
    CREATED("Created"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed");

    private String status;
    private Status(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return this.status;
    }

    public List<String> getValueList(){
        Set<Status> statusSet = EnumSet.allOf(Status.class);
        List<String> statusList = new ArrayList<>();

        for(Status status : statusSet){
            statusList.add(status.toString());
        }
        return statusList;
    }
}
