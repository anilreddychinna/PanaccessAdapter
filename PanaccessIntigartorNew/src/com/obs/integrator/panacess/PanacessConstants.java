package com.obs.integrator.panacess;

public class PanacessConstants {
	
	public static final String VIDEO_TYPE_DESC = "4_3-fit";
	public static final String VOD_SUBSCRIBER_PACKAGENAME = "Basic";
	public static final String CH_SUBSCRIBER_PACKAGENAME = "Basic";
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss ZZ";
	//public static final String PLANMAPPING = "planIdentification";
	public static final String SERVICES = "services";
	public static final String SERVICEIDENTIFICATION = "serviceIdentification";
	public static final String OLDSERVICES = "oldServices";
	public static final String OLDSERVICEIDENTIFICATION = "oldServiceIdentification";
	public static final String OSD_MESSAGE_TYPE = "genericmessage";
	
	
	//For request Type
	public static final String REQ_CLIENT_ACTIVATION = "CLIENT ACTIVATION";
	public static final String REQ_ACTIVATION = "ACTIVATION";
	public static final String REQ_DISCONNECTION = "DISCONNECTION";
	public static final String REQ_RECONNECTION = "RECONNECTION";
    public static final String REQ_MESSAGE = "MESSAGE";  //OSDMESSAGE
    public static final String REQ_RENEWAL_AE = "RENEWAL_AE";
    public static final String REQ_CHANGE_PLAN = "CHANGE_PLAN";
    public static final String REQ_TERMINATE = "TERMINATE";
    public static final String REQ_DEVICE_SWAP = "DEVICE_SWAP";
    public static final String REQ_RELEASE_DEVICE = "RELEASE DEVICE";
    public static final String REQ_RENEWAL_BE = "RENEWAL_BE";
    
    //Status Types
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String PROSPECTIVE = "PROSPECTIVE";
    public static final String TRUE = "true";
    public static final String DEFAULT_USER_PIN = "1234";
    
    
    //output type
    public static final String SUCCESS = "Success";
    public static final String FAILURE = "failure : ";
    
    public static final String RES_SUCCESSFUL = "Sucessfull";
	public static final String REQUIRED_UID_ALREADY_EXIST = "Required UID already exist! -(subscriberUid)-> Parameters";
	//public static final String REQUIRED_DEVICE_UID_ALREADY_EXIST = "Required UID already exist! -(deviceUid)-> Parameters";
	public static final String REQUIRED_DEVICE_NOT_FOUND = "Required parameter not found! -(deviceUid)-> Parameters";
}
