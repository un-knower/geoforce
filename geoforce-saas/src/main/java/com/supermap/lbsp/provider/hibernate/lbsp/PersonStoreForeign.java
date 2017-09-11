package com.supermap.lbsp.provider.hibernate.lbsp;




public class PersonStoreForeign implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private String personId;
	private String storeId;

	// Constructors

	/** default constructor */
	public PersonStoreForeign() {
	}

	/** full constructor */
	public PersonStoreForeign(String id, String personId, String storeId) {
		this.id = id;
		this.personId = personId;
		this.storeId = storeId;
	}

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	
	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

}