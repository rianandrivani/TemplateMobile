package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class mMenuData {

	public mMenuData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIntId() {
		return intId;
	}

	public void setIntId(Integer intId) {
		this.intId = intId;
	}

	public Integer getIntOrder() {
		return IntOrder;
	}

	public void setIntOrder(Integer intOrder) {
		IntOrder = intOrder;
	}

	public Integer getIntParentID() {
		return IntParentID;
	}

	public void setIntParentID(Integer intParentID) {
		IntParentID = intParentID;
	}

	public String getTxtDescription() {
		return txtDescription;
	}

	public void setTxtDescription(String txtDescription) {
		this.txtDescription = txtDescription;
	}

	public String getTxtLink() {
		return txtLink;
	}

	public void setTxtLink(String txtLink) {
		this.txtLink = txtLink;
	}

	public String getTxtMenuName() {
		return txtMenuName;
	}

	public void setTxtMenuName(String txtMenuName) {
		this.txtMenuName = txtMenuName;
	}

	public String getTxtVisible() {
		return txtVisible;
	}

	public void setTxtVisible(String txtVisible) {
		this.txtVisible = txtVisible;
	}

	public String getIntMenuID() {
		return IntMenuID;
	}

	public void setIntMenuID(String intMenuID) {
		IntMenuID = intMenuID;
	}

	public String getTxtIcon() {
		return txtIcon;
	}

	public void setTxtIcon(String txtIcon) {
		this.txtIcon = txtIcon;
	}

	@DatabaseField(id = true,columnName = "intId")
	public Integer intId;
	@DatabaseField(columnName = "IntOrder")
	public Integer IntOrder;
	@DatabaseField(columnName = "IntParentID")
	public Integer IntParentID;
	@DatabaseField(columnName = "TxtDescription")
	public String txtDescription;
	@DatabaseField(columnName = "TxtLink")
	public String txtLink;
	@DatabaseField(columnName = "TxtMenuName")
	public String txtMenuName;
	@DatabaseField(columnName = "txtVisible")
	public String txtVisible;
	@DatabaseField(columnName = "IntMenuID")
	public String IntMenuID;
	@DatabaseField(columnName = "TxtIcon")
	public String txtIcon;
	
	public String Property_intVisible="intVisible";
	public String Property_intId="intId";
	public String Property_API_IntMenuID="IntMenuID";
	public String Property_IntOrder="IntOrder";
	public String Property_IntParentID="IntParentID";
	public String Property_TxtDescription="TxtDescription";
	public String Property_TxtLink="TxtLink";
	public String Property_TxtMenuName="TxtMenuName";
	public String Property_ListOfMMenuData="ListOfMMenuData";
	public String Property_TxtIcon="TxtIcon";

	public String Property_All=Property_intId +","+
			Property_IntOrder +","+
			Property_IntParentID +","+
			Property_TxtDescription +","+
			Property_TxtLink +","+
			Property_TxtMenuName +","+
			Property_intVisible +","+
			Property_TxtIcon + "," +
			Property_API_IntMenuID
			;
}
