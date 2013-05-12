package cz.zcu.kiv.eegdatabase.webservices.client.wrappers;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroupMembershipId;

/**
 * Class for gathering important information about research group membership.
 * Meant to be sent between eegclient and portal's client service.
 * 
 * @author František Liška
 */
public class ResearchGroupMembershipInfo {
	private ResearchGroupMembershipId id = new ResearchGroupMembershipId();
	private String authority;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public ResearchGroupMembershipId getId() {
		return id;
	}

	public void setId(ResearchGroupMembershipId id) {
		this.id = id;
	}
}
