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
 * SysUpdateLogUserEntity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="sys_updatelog_user"
)

public class SysUpdateLogUserEntity  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private String updateLogid;
     private String userid;
     private int readFlag;//默认为1

     private Date createTime;

    // Constructors

    /** default constructor */
    public SysUpdateLogUserEntity() {
    }

	/** minimal constructor */
    public SysUpdateLogUserEntity(String id) {
        this.id = id;
    }
    
    /** full constructor */
    public SysUpdateLogUserEntity(String id, String updateLogid, String userid, int readFlag) {
        this.id = id;
        this.updateLogid = updateLogid;
        this.userid = userid;
        this.readFlag = readFlag;
    }

   
    // Property accessors
    @Id 
    @GeneratedValue(generator="idGener")
   	@GenericGenerator(name="idGener",strategy="uuid")
    @Column(name="id", nullable=false, length=32)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="update_logid", length=32)

    public String getUpdateLogid() {
        return this.updateLogid;
    }
    
    public void setUpdateLogid(String updateLogid) {
        this.updateLogid = updateLogid;
    }
    
    @Column(name="userid", length=32)

    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    @Column(name="read_flag")

    public int getReadFlag() {
        return this.readFlag;
    }
    
    public void setReadFlag(int readFlag) {
        this.readFlag = readFlag;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time",length=19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}