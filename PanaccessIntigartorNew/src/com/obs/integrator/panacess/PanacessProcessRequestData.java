
package com.obs.integrator.panacess;


public class PanacessProcessRequestData {
	
	private Long id;
	private Long prdetailsId;
	private String provisioingSystem;

	private Long serviceId;
	private String product;
	private String hardwareId;

	private String requestType;
	private String itemCode;
	private String itemDescription;

	private Long clientId;
	private String accountNo;
	private String firstName;
	private String lastName;

	private String officeUId;
	private String branch;
	private String regionCode;
	private String regionName;
	private String deviceId;
	private String ipAddress;
	private String displayName;
	
	public PanacessProcessRequestData(Long id, Long prdetailsId,String provisioingSystem, Long serviceId, String product,String hardwareId, 
			String requestType, String itemCode,String itemDescription, Long clientId, String accountNo,String firstName, String lastName,
			String officeUId,String branch, String regionCode, String regionName, String deviceId, String ipAddress, String displayName) {
		
		this.id=id;
        this.prdetailsId=prdetailsId;
        this.provisioingSystem=provisioingSystem;
        this.serviceId=serviceId;
        this.product=product; 
        this.hardwareId=hardwareId;
        
        this.requestType=requestType;
        this.itemCode=itemCode;
        this.itemDescription=itemDescription;
        this.clientId=clientId;
        this.accountNo=accountNo;    
        this.firstName=firstName;
        this.lastName=lastName;
        
        this.officeUId=officeUId;
        this.branch=branch;
        this.regionCode=regionCode;
        this.regionName=regionName;
        this.deviceId=deviceId;
        this.ipAddress=ipAddress;
        this.displayName =displayName;
        
	}

	public Long getId() {
		return id;
	}

	public Long getPrdetailsId() {
		return prdetailsId;
	}

	public String getProvisioingSystem() {
		return provisioingSystem;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public String getProduct() {
		return product;
	}

	public String getHardwareId() {
		return hardwareId;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public Long getClientId() {
		return clientId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getOfficeUId() {
		return officeUId;
	}

	public String getBranch() {
		return branch;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setRequestType(String requestType) {
		
		this.requestType = requestType;
		
	}

	public String getDisplayName() {
		return displayName;
	}
		
}
