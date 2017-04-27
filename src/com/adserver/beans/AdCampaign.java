package com.adserver.beans;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class AdCampaign implements Serializable {

	private static final long serialVersionUID = 3883087118262144399L;
	@JsonProperty("partner_id")
	private String partnerId;
	//Duration in Minutes
	@JsonProperty("duration")
	private String duration;
	@JsonProperty("ad_content")
	private String adContent;
	private Date creationTIme;

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAdContent() {
		return adContent;
	}

	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

	public void setCreationTIme(Date creationTIme) {
		this.creationTIme = creationTIme;
	}

	public Date getCreationTIme() {
		return creationTIme;
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object != null && object instanceof AdCampaign) {
			result = this.partnerId.equals(((AdCampaign) object).partnerId);
		}
		return result;
	}

}