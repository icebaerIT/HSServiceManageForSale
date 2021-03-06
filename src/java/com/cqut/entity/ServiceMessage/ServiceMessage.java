package com.cqut.entity.ServiceMessage;

import java.util.Date;
import com.cqut.service.tableCreator.ID;

public class ServiceMessage{
	
	@ID
	private String  ID;
	private String ProjectName;
	private String OfferorsID;
	private double TheTotalOffer;
	private Date offerTime;
	private String pictureID;
	private String  TemplateName;
	private String ReceiverID;
	private String Title;
	private String contactsName;
	
	
	public String getID() {
		return ID;
	}	
	
	public void setID(String ID) {
		this.ID = ID;
	}
	public String getOfferorsID() {
		return OfferorsID;
	}	
	
	public void setOfferorsID(String OfferorsID) {
		this.OfferorsID = OfferorsID;
	}
	public double getTheTotalOffer() {
		return TheTotalOffer;
	}	
	
	public void setTheTotalOffer(double TheTotalOffer) {
		this.TheTotalOffer = TheTotalOffer;
	}
	public Date getOfferTime() {
		return offerTime;
	}	
	
	public void setOfferTime(Date offerTime) {
		this.offerTime = offerTime;
	}
	public String getPictureID() {
		return pictureID;
	}	
	
	public void setPictureID(String pictureID) {
		this.pictureID = pictureID;
	}
	
	public String getReceiverID() {
		return ReceiverID;
	}

	public void setReceiverID(String receiverID) {
		ReceiverID = receiverID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	@Override
	public String toString() {
		return "ServiceMessage [ID=" + ID + ", ProjectName=" + ProjectName
				+ ", OfferorsID=" + OfferorsID + ", TheTotalOffer="
				+ TheTotalOffer + ", offerTime=" + offerTime + ", pictureID="
				+ pictureID + ", TemplateName=" + TemplateName + ", ReceiverID="
				+ ReceiverID + ", Title=" + Title + ",contactsName=" + contactsName +"]";
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getTemplateName() {
		return TemplateName;
	}

	public void setTemplateName(String TemplateName) {
		this.TemplateName = TemplateName;
	}
	
}
