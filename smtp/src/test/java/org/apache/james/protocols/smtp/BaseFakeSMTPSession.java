/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/


package org.apache.james.protocols.smtp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.protocols.api.LineHandler;
import org.apache.james.protocols.api.Response;
import org.apache.james.protocols.smtp.SMTPConfiguration;
import org.apache.james.protocols.smtp.SMTPSession;

/**
 * Abstract class to simplify the mocks
 */
public class BaseFakeSMTPSession implements SMTPSession {

    private static final Log log = LogFactory.getLog(BaseFakeSMTPSession.class);

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getConfigurationData()
     */
    public SMTPConfiguration getConfigurationData() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getConnectionState()
     */
    public Map<String, Object> getConnectionState() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getRcptCount()
     */
    public int getRcptCount() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getRemoteHost()
     */
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getRemoteIPAddress()
     */
    public String getRemoteIPAddress() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getSessionID()
     */
    public String getSessionID() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getState()
     */
    public Map<String, Object> getState() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getUser()
     */
    public String getUser() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#isAuthSupported()
     */
    public boolean isAuthSupported() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#isRelayingAllowed()
     */
    public boolean isRelayingAllowed() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#resetState()
     */
    public void resetState() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#setRelayingAllowed(boolean)
     */
    public void setRelayingAllowed(boolean relayingAllowed) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#setUser(java.lang.String)
     */
    public void setUser(String user) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }


    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#popLineHandler()
     */
    public void popLineHandler() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.smtpserver.protocol.SMTPSession#pushLineHandler(org.apache.james.api.protocol.LineHandler)
     */
    public void pushLineHandler(LineHandler<SMTPSession> overrideCommandHandler) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.api.protocol.LogEnabledSession#writeResponse(org.apache.james.api.protocol.Response)
     */
    public void writeResponse(Response response) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#sleep(long)
     */
    public void sleep(long ms) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
    }

    public String getHelloName() {
        return getConfigurationData().getHelloName();
    }

    public long getMaxMessageSize() {
        return getConfigurationData().getMaxMessageSize();
    }

    public String getSMTPGreeting() {
        return getConfigurationData().getSMTPGreeting();
    }

    public boolean useAddressBracketsEnforcement() {
        return getConfigurationData().useAddressBracketsEnforcement();
    }

    public boolean useHeloEhloEnforcement() {
        return getConfigurationData().useAddressBracketsEnforcement();
    }

    /**
     * @see org.apache.james.protocols.smtp.SMTPSession#getLogger()
     */
    public Log getLogger() {
        return log;
    }

	public boolean isStartTLSSupported() {
		return getConfigurationData().isStartTLSSupported();
	}

	public boolean isTLSStarted() {
        throw new UnsupportedOperationException("Unimplemented Stub Method");
	}

	public void startTLS() throws IOException {
        throw new UnsupportedOperationException("Unimplemented Stub Method");

	}

	public void writeStream(InputStream stream) {
        throw new UnsupportedOperationException("Unimplemented Stub Method");		
	}
}