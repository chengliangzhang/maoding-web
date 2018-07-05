package com.maoding.system.entity;



import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DataDictionaryEntity
 * 类描述：数据字典
 * 作    者：wangrb
 * 日    期：2015年11月26日-下午2:21:01
 */
public class DataDictionaryEntity extends BaseEntity {
	
    private String name;

    private String code;

    private String pid;

    private String rootId;

    private Integer deepIndex;

    private String vl;

    private String seq;//排序字段

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootid) {
        this.rootId = rootId == null ? null : rootId.trim();
    }


    public Integer getDeepIndex() {
		return deepIndex;
	}

	public void setDeepIndex(Integer deepIndex) {
		this.deepIndex = deepIndex;
	}

	public String getVl() {
        return vl;
    }

    public void setVl(String vl) {
        this.vl = vl == null ? null : vl.trim();
    }

	/**
	 * 获取: seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * 设置:seq
	 */
	public void setSeq(String seq) {
		this.seq = seq;
	}


}