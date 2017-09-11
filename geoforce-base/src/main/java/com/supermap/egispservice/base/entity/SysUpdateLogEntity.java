package com.supermap.egispservice.base.entity;
// default package

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * SysUpdateLogEntity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="sys_update_log"
)

public class SysUpdateLogEntity  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private Date onlineTime;  //上线时间
     private String versionName; //版本名称
     private String versionNumber;//版本号
     private String newFunctions;//新增功能
     private String improveFunctions;//改进功能
     private String repairBugs;//缺陷修复
     private String developers;//开发人员 ---暂不使用
     private String workHours;//工时  --暂不使用
     private String createUserid; //创建人
     private Date createTime;//创建时间
     private Date updateTime;//修改时间
     private int deleteflag;//删除标识  0未删，1已删


    // Constructors

    /** default constructor */
    public SysUpdateLogEntity() {
    }

	/** minimal constructor */
    public SysUpdateLogEntity(String id) {
        this.id = id;
    }
    
    /** full constructor */
    public SysUpdateLogEntity(String id, Date onlineTime, String versionName, String versionNumber, String newFunctions, String improveFunctions, String repairBugs, String developers, String workHours, String createUserid, Date createTime, Date updateTime, int deleteflag) {
        this.id = id;
        this.onlineTime = onlineTime;
        this.versionName = versionName;
        this.versionNumber = versionNumber;
        this.newFunctions = newFunctions;
        this.improveFunctions = improveFunctions;
        this.repairBugs = repairBugs;
        this.developers = developers;
        this.workHours = workHours;
        this.createUserid = createUserid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleteflag = deleteflag;
    }

   
    // Property accessors
    @Id 
    @GeneratedValue(generator="idGener")
	@GenericGenerator(name="idGener",strategy="uuid")
    @Column(name="id", unique=true, nullable=false, length=32)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="online_time", length=19)

    public Date getOnlineTime() {
        return this.onlineTime;
    }
    
    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }
    
    @Column(name="version_name", length=50)

    public String getVersionName() {
        return this.versionName;
    }
    
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    
    @Column(name="version_number", length=20)

    public String getVersionNumber() {
        return this.versionNumber;
    }
    
    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    @Column(name="new_functions", length=1000)

    public String getNewFunctions() {
        return this.newFunctions;
    }
    
    public void setNewFunctions(String newFunctions) {
        this.newFunctions = newFunctions;
    }
    
    @Column(name="improve_functions", length=1000)

    public String getImproveFunctions() {
        return this.improveFunctions;
    }
    
    public void setImproveFunctions(String improveFunctions) {
        this.improveFunctions = improveFunctions;
    }
    
    @Column(name="repair_bugs", length=500)

    public String getRepairBugs() {
        return this.repairBugs;
    }
    
    public void setRepairBugs(String repairBugs) {
        this.repairBugs = repairBugs;
    }
    
    @Column(name="developers", length=100)

    public String getDevelopers() {
        return this.developers;
    }
    
    public void setDevelopers(String developers) {
        this.developers = developers;
    }
    
    @Column(name="work_hours", length=20)

    public String getWorkHours() {
        return this.workHours;
    }
    
    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }
    
    @Column(name="create_userid", length=32)

    public String getCreateUserid() {
        return this.createUserid;
    }
    
    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time", length=19)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_time", length=19)

    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Column(name="deleteflag")

    public int getDeleteflag() {
        return this.deleteflag;
    }
    
    public void setDeleteflag(int deleteflag) {
        this.deleteflag = deleteflag;
    }
   

}