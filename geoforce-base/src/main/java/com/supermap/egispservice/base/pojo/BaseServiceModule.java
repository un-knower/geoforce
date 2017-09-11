package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;


public class BaseServiceModule  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String status;
	private int useLimit;
	private String url;
	private String ref_url;
	private float price;
	private String remarks;
	private String code;
	private String icon_url;
	private String type;
	
	
	private BaseServiceModule serviceModule[] = null;
	
	@Override
	public String toString() {
		return new StringBuilder().append(id).append(",").append(name).append(",").append(status).append(",").append(
				useLimit).append(",").append("url").append(",").append(ref_url).append(",").append(
				",").append(code).append(",").append(price).toString();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUseLimit() {
		return useLimit;
	}
	public void setUseLimit(int useLimit) {
		this.useLimit = useLimit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BaseServiceModule[] getServiceModule() {
		return serviceModule;
	}
	public void setServiceModule(BaseServiceModule[] serviceModule) {
		this.serviceModule = serviceModule;
	}
	public void setServiceModule(List<BaseServiceModule> serviceModule) {
		if(null != serviceModule){
			this.serviceModule = new BaseServiceModule[serviceModule.size()];
			for(int i=0;i<serviceModule.size();i++){
				this.serviceModule[i] = serviceModule.get(i);
			}
		}
	}

	public String getRef_url() {
		return ref_url;
	}

	public void setRef_url(String refUrl) {
		ref_url = refUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String iconUrl) {
		icon_url = iconUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
