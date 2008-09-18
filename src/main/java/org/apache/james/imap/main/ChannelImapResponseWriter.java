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

package org.apache.james.imap.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import org.apache.james.api.imap.AbstractLogEnabled;
import org.apache.james.api.imap.ImapConstants;
import org.apache.james.imap.message.response.imap4rev1.Literal;
import org.apache.james.imapserver.codec.encode.ImapResponseWriter;

/**
 * Class providing methods to send response messages from the server
 * to the client.
 */
public class ChannelImapResponseWriter extends AbstractLogEnabled implements ImapConstants, ImapResponseWriter {
    
    private static final int LOWER_CASE_OFFSET = 'a' - 'A';
    private static final int DEFAULT_BUFFER_SIZE = 128;
    
    private final Charset usAscii;
    private final WritableByteChannel out;
    private final ByteBuffer buffer;
    private boolean skipNextSpace;

    public ChannelImapResponseWriter( final WritableByteChannel out) {
        this(out, DEFAULT_BUFFER_SIZE);
    }
    
    public ChannelImapResponseWriter( final WritableByteChannel out, final int bufferSize  )
    {
        this.out = out;
        skipNextSpace = false;
        buffer = ByteBuffer.allocate(bufferSize);
        usAscii = Charset.forName("US-ASCII");
    }

    /**
     * Writes the message provided to the client, prepended with the
     * untagged marker "*".
     *
     * @param message The message to write to the client.
     * @throws IOException 
     */
    public void untaggedResponse( String message ) throws IOException
    {
        untagged();
        message( message );
        end();
    }
    
    public void byeResponse( String message ) throws IOException {
        untaggedResponse(BYE + SP + message);
    }

    private void write(final ByteBuffer buffer) throws IOException {
        while(out.write(buffer)>0) {
            // Write all
        }
    }
    
    private void writeASCII(final String string) throws IOException {
        final ByteBuffer buffer = usAscii.encode(string);
        write( buffer );
    }
    
    private void write(byte[] bytes) throws IOException {
        final ByteBuffer wrap = ByteBuffer.wrap(bytes);
        write(wrap);
    }
    
    public void untagged() throws IOException {
        writeASCII( UNTAGGED );
    }

    public void tag(String tag) throws IOException {
        writeASCII( tag );
    }

    public void message( String message ) throws IOException
    {
        if ( message != null ) {
            space();
            writeASCII( message );
        }
    }

    public void message( long number ) throws IOException
    {
        space();
        writeASCII( Long.toString(number) );
    }

    public void responseCode( String responseCode ) throws IOException
    {
        if ( responseCode != null ) {
            writeASCII( " [" );
            writeASCII( responseCode );
            write( BYTES_CLOSE_SQUARE_BRACKET );
        }
    }

    public void end() throws IOException
    {
        write(BYTES_LINE_END);
        flush();
    }

    public void commandName(String commandName) throws IOException {
        space();
        writeASCII( commandName );
    }

    public void quote(String message) throws IOException {
        space();
        final int length = message.length();
        buffer.clear();
        buffer.put(BYTE_DQUOTE);
        for (int i=0;i<length;i++) {
            writeIfFull();
            char character = message.charAt(i);
            if (character == ImapConstants.BACK_SLASH 
                    || character == DQUOTE) {
                buffer.put(BYTE_BACK_SLASH);
            }
            writeIfFull();
            // 7-bit ASCII only
            if (character > 128) {
                buffer.put(BYTE_QUESTION);
            } else {
                buffer.put((byte) character);
            }
        }
        writeIfFull();
        buffer.put(BYTE_DQUOTE);
        buffer.flip();
        write(buffer);
    }

    private void writeIfFull() throws IOException {
        if (!buffer.hasRemaining()) {
            buffer.flip();
            write(buffer);
            buffer.clear();
        }
    }
    
    public void flush() throws IOException {
    }

    public void closeParen() throws IOException {
        closeBracket(BYTES_CLOSING_PARENTHESIS);
    }

    private void closeBracket(final byte[] bracket) throws IOException {
        write(bracket);
        clearSkipNextSpace();
    }

    public void openParen() throws IOException {
        openBracket(BYTES_OPENING_PARENTHESIS);
    }

    private void openBracket(final byte[] bracket) throws IOException {
        space();
        write(bracket);
        skipNextSpace();
    }
    
    private void clearSkipNextSpace() {
        skipNextSpace = false;
    }
    
    public void skipNextSpace() {
        skipNextSpace = true;
    }
    
    public void space() throws IOException {
        if (skipNextSpace) {
            skipNextSpace = false;
        } else {
            write(BYTES_SPACE);
        }
    }

    public void literal(Literal literal) throws IOException {
        space();
        write(BYTES_OPEN_BRACE);
        final long size = literal.size();
        writeASCII(Long.toString(size));
        write(BYTES_CLOSE_BRACE);
        write(BYTES_LINE_END);
        if (size>0) {
            literal.writeTo(out);
        }
    }

    public void closeSquareBracket() throws IOException {
        closeBracket(BYTES_CLOSE_SQUARE_BRACKET);
    }

    public void openSquareBracket() throws IOException {
        openBracket(BYTES_OPEN_SQUARE_BRACKET);
    }
    
    public void upperCaseAscii(String message) throws IOException {
        upperCaseAscii(message, false);
    }
    
    private void upperCaseAscii(String message, boolean quote) throws IOException {
        space();
        final int length = message.length();
        buffer.clear();
        if (quote) {
            buffer.put(BYTE_DQUOTE);
        }
        for (int i=0;i<length;i++) {
            writeIfFull();
            final char next = message.charAt(i);
            if (next >= 'a' && next <= 'z') {
                buffer.put((byte)(next - LOWER_CASE_OFFSET));
            } else {
                buffer.put((byte)(next));
            }
        }
        writeIfFull();
        if (quote) {
            buffer.put(BYTE_DQUOTE);
        }
        buffer.flip();
        write(buffer);
    }

    public void quoteUpperCaseAscii(String message) throws IOException {
        upperCaseAscii(message, true);   
    }
}
