package com.th.pm.constant;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum Priority {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String priority;

    private Priority(String priority){
        this.priority = priority;
    }

    @Override
    public String toString(){
        return this.priority;
    }

    public List<String> getValueList(){
        Set<Priority> prioritySet = EnumSet.allOf(Priority.class);
        List<String> priorityList = new ArrayList<>();
        for(Priority priority: prioritySet){
            priorityList.add(priority.toString());
        }
        return priorityList;
    }
}
