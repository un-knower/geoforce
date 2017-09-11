package com.supermap.lbsp.provider.bean;


import com.supermap.lbsp.provider.hibernate.lbsp.Person;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonPlan;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonSign;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;


public class PersonPlanBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
		private PersonPlan personPlan;
		private Person person;
		private PersonStore personStore;
		private Short status;//状态
		private String id;
		private  PersonSign personSign;
		private String storeName;//门店名称
		
		
		public String getStoreName() {
			return storeName;
		}
		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}
		public PersonSign getPersonSign() {
			return personSign;
		}
		public void setPersonSign(PersonSign personSign) {
			this.personSign = personSign;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Short getStatus() {
			return status;
		}
		public void setStatus(Short status) {
			this.status = status;
		}
	public PersonPlan getPersonPlan() {
		return personPlan;
	}
	public void setPersonPlan(PersonPlan personPlan) {
		this.personPlan = personPlan;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public PersonStore getPersonStore() {
		return personStore;
	}
	public void setPersonStore(PersonStore personStore) {
		this.personStore = personStore;
	}
	
	
	
}
