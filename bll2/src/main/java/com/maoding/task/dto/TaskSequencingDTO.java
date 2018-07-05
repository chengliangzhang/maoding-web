package com.maoding.task.dto;

/**
 * 深圳市设计同道技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2017/8/29-18:45
 * 描    述 : 用于申请调整任务排序编号
 */
public class TaskSequencingDTO {
    /** 任务编号 */
    private String id;
    /** 当前任务排序编号，如果是-1，则根据任务编号从数据库中查询 */
    private Integer seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
