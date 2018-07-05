package com.maoding.task.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Idccapp21 on 2016/12/30.
 */
public class ProejctTaskForDesignerDTO {

    /**
     * 项目名称
     */
    private String rootId;


    private int seq;


    private List<ProejctTaskForDesignerListDTO> taskList=new ArrayList<ProejctTaskForDesignerListDTO>();


    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public List<ProejctTaskForDesignerListDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ProejctTaskForDesignerListDTO> taskList) {
        this.taskList = taskList;
    }
}
