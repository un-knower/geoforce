package com.supermap.egispservice.pathplan.util;

public class CondTypeOne {
	String nodeID;
	String fEdgeID;
	String tEdgeID;
	String condType;

	public CondTypeOne(String nodeID, String fEdgeID, String tEdgeID, String condType) {
		this.nodeID = nodeID;
		this.fEdgeID = fEdgeID;
		this.tEdgeID = tEdgeID;
		this.condType = condType;
	}

	public String getNodeID() {
		return nodeID;
	}

	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}

	public String getfEdgeID() {
		return fEdgeID;
	}

	public void setfEdgeID(String fEdgeID) {
		this.fEdgeID = fEdgeID;
	}

	public String gettEdgeID() {
		return tEdgeID;
	}

	public void settEdgeID(String tEdgeID) {
		this.tEdgeID = tEdgeID;
	}

	public String getCondType() {
		return condType;
	}

	public void setCondType(String condType) {
		this.condType = condType;
	}

	@Override
	public String toString() {
		return String.format("%s-%s-%s", nodeID, fEdgeID, tEdgeID);
	}

}
