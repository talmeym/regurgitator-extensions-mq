package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

public class CreateJmsResponse extends Identifiable implements Step {
	private final Log log = getLog(this);
	private final CreateResponse response;
	private final String destination;
	private final String correlationId;
	private final String type;

	public CreateJmsResponse(CreateResponse response, String destination, String correlationId, String type) {
		super(response.getId());
		this.response = response;
		this.destination = destination;
		this.correlationId = correlationId;
		this.type = type;
	}

	@Override
	public void execute(Message message) throws RegurgitatorException {
		Parameters responseMetadata = message.getContext(RESPONSE_METADATA_CONTEXT);

		if(destination != null) {
			responseMetadata.setValue(JMS_DESTINATION, destination);
		}

		if(correlationId != null) {
			responseMetadata.setValue(JMS_CORRELATION_ID, correlationId);
		}

		if(type != null) {
			responseMetadata.setValue(JMS_TYPE, type);
		}

		response.execute(message, log);
	}
}
