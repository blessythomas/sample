package src.java.beans;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "deal")
public class Deal {
	

	@XmlAttribute
	private Integer dlId;
	
	@Column(name = "DEAL_DESC")
	private String dealDesc;
	
	@Column(name = "DEAL_START_DATE")
	private Date dealStartDate;
	
	@Column(name = "DEAL_MATURITY_DATE")
	private Date dealMaturityDate;

	@OneToMany(fetch = FetchType.EAGER,mappedBy = "deal", cascade = {CascadeType.ALL})
	private List<Facility> facilityList = new ArrayList<Facility>();

	public String getDealDesc() {
		return dealDesc;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}

	@Override
	public String toString() {
		return "Deal [id=" + dlId + ", dealDesc=" + dealDesc+", satrtdate = "+dealStartDate +"]";
	}

	public Integer getDlId() {
		return dlId;
	}

	public void setDlId(Integer dlId) {
		this.dlId = dlId;
	}

	/**
	 * @return the facilityList
	 */
	public List<Facility> getFacilityList() {
		return facilityList;
	}

	/**
	 * @param facilityList the facilityList to set
	 */
	public void setFacilityList(List<Facility> facilityList) {
		this.facilityList = facilityList;
	}

	/**
	 * @return the dealStartDate
	 */
	public Date getDealStartDate() {
		return dealStartDate;
	}

	/**
	 * @param dealStartDate the dealStartDate to set
	 */
	public void setDealStartDate(Date dealStartDate) {
		this.dealStartDate = dealStartDate;
	}

	/**
	 * @return the dealMaturityDate
	 */
	public Date getDealMaturityDate() {
		return dealMaturityDate;
	}

	/**
	 * @param dealMaturityDate the dealMaturityDate to set
	 */
	public void setDealMaturityDate(Date dealMaturityDate) {
		this.dealMaturityDate = dealMaturityDate;
	}

}
