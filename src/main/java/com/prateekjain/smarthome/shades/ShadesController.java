package com.prateekjain.smarthome.shades;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.prateekjain.smarthome.util.CommandArguments;
import com.prateekjain.smarthome.util.SampleUtil;

public class ShadesController {
	
    private static AWSIotMqttClient awsIotClient;
    
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
    

	public static void main(String[] args) {
		
		 CommandArguments arguments = CommandArguments.parse(args);
	        initClient(arguments);

	        awsIotClient.setWillMessage(new AWSIotMessage("client/disconnect", AWSIotQos.QOS0, awsIotClient.getClientId()));

	        String thingName = arguments.getNotNull("thingName", SampleUtil.getConfig("thingName"));
	        ConnectedShade connectedWindow = new ConnectedShade(thingName);

	        awsIotClient.attach(connectedWindow);
	        awsIotClient.connect();

	        // Delete existing document if any
	        connectedWindow.delete();

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

}
