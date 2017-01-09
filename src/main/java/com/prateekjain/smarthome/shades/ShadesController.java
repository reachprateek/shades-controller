package com.prateekjain.smarthome.shades;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.prateekjain.smarthome.util.CommandArguments;
import com.prateekjain.smarthome.util.SampleUtil;

public class ShadesController {
	
    private static AWSIotMqttClient awsIotClient;
    
    //family room shades
    private static final String fr_ch0_off = "0100100000110001111100110101000000011110";
    private static final String fr_ch0_on = "0100100000110001111100110101000000111100";
    private static final String fr_ch0_stop = "0100100000110001111100110101000001010101";


    private static final String fr_ch1_off = "0100100000110001111100110101000100011110";
    private static final String fr_ch1_on = "0100100000110001111100110101000100111100";
    private static final String fr_ch1_stop = "0100100000110001111100110101000101010101";


    private static final String fr_ch2_off = "0100100000110001111100110101001000011110";
    private static final String fr_ch2_on = "0100100000110001111100110101001000111100";
    private static final String fr_ch2_stop = "0100100000110001111100110101001001010101";


    private static final String fr_ch3_off = "0100100000110001111100110101001100011110";
    private static final String fr_ch3_on = "0100100000110001111100110101001100111100";
    private static final String fr_ch3_stop = "0100100000110001111100110101001101010101";


    private static final String fr_ch4_off = "0100100000110001111100110101010000011110";
    private static final String fr_ch4_on = "0100100000110001111100110101010000110011";
    private static final String fr_ch4_stop = "0100100000110001111100110101010001010101";

    //bedrooms
    private static final String br_ch0_off = "1101000000110001001101110010000000010001";
    private static final String br_ch0_on = "1101000000110001001101110010000000110011";
    private static final String br_ch0_stop = "1101000000110001001101110010000001010101";

    //study room shade
    private static final String sr_ch1_off = "1101000000110001001101110010000100010001";
    private static final String sr_ch1_on = "1101000000110001001101110010000100110011";
    private static final String sr_ch1_stop = "1101000000110001001101110010000101010101";

    //aarav room shade
    private static final String ar_ch2_off = "1101000000110001001101110010001000010001";
    private static final String ar_ch2_on = "1101000000110001001101110010001000110011";
    private static final String ar_ch2_stop = "1101000000110001001101110010001001010101";    
    
    public static void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

    private static void initClient(CommandArguments arguments) {
    	
        String clientEndpoint = arguments.getNotNull("clientEndpoint", SampleUtil.getConfig("clientEndpoint"));
        String clientId = arguments.getNotNull("clientId", SampleUtil.getConfig("clientId"));
    	
        String awsAccessKeyId = arguments.get("awsAccessKeyId", SampleUtil.getConfig("awsAccessKeyId"));
        String awsSecretAccessKey = arguments.get("awsSecretAccessKey", SampleUtil.getConfig("awsSecretAccessKey"));
        String sessionToken = arguments.get("sessionToken", SampleUtil.getConfig("sessionToken"));

        if (awsAccessKeyId != null && awsSecretAccessKey != null) {
            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                    sessionToken);
        }

        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    	
    }
    
    private static void initDevices() throws AWSIotException {
    	
        ConnectedShade familyRoomShadeOne = new ConnectedShade("family-room-shade1", fr_ch1_on, fr_ch1_off, fr_ch1_stop);
        ConnectedShade familyRoomShadeTwo = new ConnectedShade("family-room-shade2", fr_ch2_on, fr_ch2_off, fr_ch2_stop);
        ConnectedShade familyRoomShadeThree = new ConnectedShade("family-room-shade3", fr_ch3_on, fr_ch3_off, fr_ch3_stop);
        ConnectedShade familyRoomShadeFour = new ConnectedShade("family-room-shade4", fr_ch4_on, fr_ch4_off, fr_ch4_stop);
        
        ConnectedShade studyRoomShadeOne = new ConnectedShade("study-room-shade", sr_ch1_on, sr_ch1_off, sr_ch1_stop);
        ConnectedShade aaravRoomShade = new ConnectedShade("aarav-room-shade", ar_ch2_on, ar_ch2_off, ar_ch2_stop);
        
        	       
        awsIotClient.attach(familyRoomShadeOne);
        awsIotClient.attach(familyRoomShadeTwo);
        awsIotClient.attach(familyRoomShadeThree);
        awsIotClient.attach(familyRoomShadeFour);
        awsIotClient.attach(studyRoomShadeOne);
        awsIotClient.attach(aaravRoomShade);
        awsIotClient.connect();

        //Delete existing document if any
        familyRoomShadeOne.delete();
        familyRoomShadeTwo.delete();
        familyRoomShadeThree.delete();
        familyRoomShadeFour.delete();
        studyRoomShadeOne.delete();
        aaravRoomShade.delete();        
    }

	public static void main(String[] args) throws AWSIotException, InterruptedException {
		
		 CommandArguments arguments = CommandArguments.parse(args);
	        initClient(arguments);
	        
	        awsIotClient.setWillMessage(new AWSIotMessage("client/disconnect", AWSIotQos.QOS0, awsIotClient.getClientId()));	        
	        initDevices();
	        AWSIotConnectionStatus status = AWSIotConnectionStatus.DISCONNECTED;
	        while (true) {
	            AWSIotConnectionStatus newStatus = awsIotClient.getConnectionStatus();
	            if (!status.equals(newStatus)) {
	                System.out.println(System.currentTimeMillis() + " Connection status changed to " + newStatus);
	                status = newStatus;
	            }
	            Thread.sleep(1000);
	        }
	}	  
}
