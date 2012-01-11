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

package org.apache.james.protocols.pop3.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.james.protocols.api.ProtocolSession.State;
import org.apache.james.protocols.api.Request;
import org.apache.james.protocols.api.Response;
import org.apache.james.protocols.pop3.POP3Response;
import org.apache.james.protocols.pop3.POP3Session;
import org.apache.james.protocols.pop3.POP3StreamResponse;
import org.apache.james.protocols.pop3.mailbox.MessageMetaData;

/**
 * Handles TOP command
 */
public class TopCmdHandler extends RetrCmdHandler implements CapaCapability {
    private static final Collection<String> COMMANDS = Collections.unmodifiableCollection(Arrays.asList("TOP"));
    private static final Set<String> CAPS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("TOP")));
    
    private static final Response SYNTAX_ERROR = new POP3Response(POP3Response.ERR_RESPONSE, "Usage: TOP [mail number] [Line number]").immutable();
    private static final Response ERROR_MESSAGE_RETR = new POP3Response(POP3Response.ERR_RESPONSE, "Error while retrieving message.").immutable();

    /**
     * Handler method called upon receipt of a TOP command. This command
     * retrieves the top N lines of a specified message in the mailbox.
     * 
     * The expected command format is TOP [mail message number] [number of lines
     * to return]
     */
    @SuppressWarnings("unchecked")
    @Override
    public Response onCommand(POP3Session session, Request request) {
        String parameters = request.getArgument();
        if (parameters == null) {
            return SYNTAX_ERROR;
        }

        String argument = "";
        String argument1 = "";
        int pos = parameters.indexOf(" ");
        if (pos > 0) {
            argument = parameters.substring(0, pos);
            argument1 = parameters.substring(pos + 1);
        }

        if (session.getHandlerState() == POP3Session.TRANSACTION) {
            int num = 0;
            int lines = -1;
            try {
                num = Integer.parseInt(argument);
                lines = Integer.parseInt(argument1);
            } catch (NumberFormatException nfe) {
                return SYNTAX_ERROR;
            }
            try {
                
                MessageMetaData data = MessageMetaDataUtils.getMetaData(session, num);
                if (data == null) {
                    StringBuilder responseBuffer = new StringBuilder(64).append("Message (").append(num).append(") does not exist.");
                    return  new POP3Response(POP3Response.ERR_RESPONSE, responseBuffer.toString());
                }
                
                List<Long> deletedUidList = (List<Long>) session.getAttachment(POP3Session.DELETED_UID_LIST, State.Transaction);

                Long uid = data.getUid();
                if (deletedUidList.contains(uid) == false) {

                    InputStream body = new CountingBodyInputStream(new ExtraDotInputStream(new CRLFTerminatedInputStream(session.getUserMailbox().getMessageBody(uid))), lines);
                    InputStream headers = session.getUserMailbox().getMessageHeaders(uid);
                    if (body != null && headers != null) {
                        return new POP3StreamResponse(POP3Response.OK_RESPONSE, "Message follows", new SequenceInputStream(headers, body));

                    } else {
                        StringBuilder exceptionBuffer = new StringBuilder(64).append("Message (").append(num).append(") does not exist.");
                        return new POP3Response(POP3Response.ERR_RESPONSE, exceptionBuffer.toString());
                    }

                } else {
                    StringBuilder responseBuffer = new StringBuilder(64).append("Message (").append(num).append(") already deleted.");
                    return new POP3Response(POP3Response.ERR_RESPONSE, responseBuffer.toString());
                }
            } catch (IOException ioe) {
                return ERROR_MESSAGE_RETR;
            } catch (IndexOutOfBoundsException iob) {
                StringBuilder exceptionBuffer = new StringBuilder(64).append("Message (").append(num).append(") does not exist.");
                return new POP3Response(POP3Response.ERR_RESPONSE, exceptionBuffer.toString());
            } catch (NoSuchElementException iob) {
                StringBuilder exceptionBuffer = new StringBuilder(64).append("Message (").append(num).append(") does not exist.");
                return new POP3Response(POP3Response.ERR_RESPONSE, exceptionBuffer.toString());
            }
        } else {
            return POP3Response.ERR;
        }

    }

    /**
     * @see org.apache.james.pop3server.core.CapaCapability#getImplementedCapabilities(org.apache.james.pop3server.POP3Session)
     */
    @SuppressWarnings("unchecked")
    public Set<String> getImplementedCapabilities(POP3Session session) {
        if (session.getHandlerState() == POP3Session.TRANSACTION) {
            return CAPS;
        } else {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * @see org.apache.james.protocols.api.handler.CommandHandler#getImplCommands()
     */
    @Override
    public Collection<String> getImplCommands() {
        return COMMANDS;
    }

    /**
     * This {@link InputStream} implementation can be used to limit the body
     * lines which will be read from the wrapped {@link InputStream}
     */   
    private final class CountingBodyInputStream extends InputStream {

        private int count = 0;
        private int limit = -1;
        private int lastChar;
        private InputStream in;

        /**
         * 
         * @param in
         *            InputStream to read from
         * @param limit
         *            the lines to read. -1 is used for no limits
         */
        public CountingBodyInputStream(InputStream in, int limit) {
            this.in = in;
            this.limit = limit;
        }

        @Override
        public int read() throws IOException {
            if (limit != -1) {
                if (count <= limit) {
                    int a = in.read();

                    if (lastChar == '\r' && a == '\n') {
                        count++;
                    }
                    lastChar = a;

                    return a;
                } else {
                    return -1;
                }
            } else {
                return in.read();
            }

        }

        @Override
        public long skip(long n) throws IOException {
            return in.skip(n);
        }

        @Override
        public int available() throws IOException {
            return in.available();
        }

        @Override
        public void close() throws IOException {
            in.close();
        }

        @Override
        public void mark(int readlimit) {
            // not supported
        }

        @Override
        public void reset() throws IOException {
            // do nothing as mark is not supported
        }

        @Override
        public boolean markSupported() {
            return false;
        }

    }
}
