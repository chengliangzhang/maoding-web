package com.maoding.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Idccapp21 on 2017/3/7.
 */
public class ProjectProcessNode {
    private String id;

    private String nodeName;

    private int seq;

    private List<ProjectProcessNodeDTO> projectProcessNodeDTOList=new ArrayList<ProjectProcessNodeDTO>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<ProjectProcessNodeDTO> getProjectProcessNodeDTOList() {
        return projectProcessNodeDTOList;
    }

    public void setProjectProcessNodeDTOList(List<ProjectProcessNodeDTO> projectProcessNodeDTOList) {
        this.projectProcessNodeDTOList = projectProcessNodeDTOList;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
