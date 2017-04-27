package com.adserver.webservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jackson.map.ObjectMapper;

import com.adserver.beans.AdCampaign;

@Path("/webservice")
@SuppressWarnings("unchecked")
public class AdWebService {
	public final static String fileName = "src/adCampaign.txt";

	@GET
	@Path("/getCampaign/{partnerId}")
	@Consumes("application/json")
	@Produces("application/json")
	public String getCampaign(@PathParam("partnerId") String partnerId)
			throws IOException {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		String result = "Success";
		List<AdCampaign> adCampaignList = null;
		Date currentTime = new Date();
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			adCampaignList = (List<AdCampaign>) ois.readObject();
			if (adCampaignList != null) {
				for (AdCampaign adCampaign : adCampaignList) {
					if (adCampaign.getPartnerId().equals(partnerId)) {
						if (compareTime(adCampaign, currentTime)) {
							ObjectMapper mapper = new ObjectMapper();
							result = mapper.writeValueAsString(adCampaign);
						} else {
							result = "Error : No active ad campaigns exist for the specified partner";
						}
					}
				}
			}

		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			if (ois != null) {
				ois.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return result;

	}

	@POST
	@Path("/saveCampaign")
	@Consumes("application/json")
	@Produces("application/json")
	public String saveCampaign(String requestString) throws IOException {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		String result = "Success";
		List<AdCampaign> adCampaignList = new ArrayList<AdCampaign>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			adCampaignList = (List<AdCampaign>) ois.readObject();
		} catch (Exception e) {
		}
		try {
			AdCampaign adCampaign = mapper.readValue(requestString,
					AdCampaign.class);
			adCampaign.setCreationTIme(new Date());
			if (adCampaignList.contains(adCampaign)) {
				result = "Error : Only one active campaign can exist for a given partner";
			} else {
				adCampaignList.add(adCampaign);
				fout = new FileOutputStream(fileName, true);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(adCampaignList);
			}
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			if (fout != null) {
				fout.close();
			}
			if (oos != null) {
				oos.close();
			}
			if (ois != null) {
				ois.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return result;

	}

	private boolean compareTime(AdCampaign adCampaign, Date currentTime) {
		boolean result = true;
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date creationTime = adCampaign.getCreationTIme();
			int duration = Integer.parseInt(adCampaign.getDuration());
			Calendar cal = Calendar.getInstance();
			cal.setTime(creationTime);
			cal.add(Calendar.MINUTE, duration);
			String newTime = sdfDate.format(cal.getTime());
			Date validDate = sdfDate.parse(newTime);
			if (currentTime.after(validDate)) {
				result = false;
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
}