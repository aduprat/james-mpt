package org.apache.james.experimental.imapserver.handler.session;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.james.experimental.imapserver.ProtocolException;
import org.apache.james.experimental.imapserver.client.FetchCommand;
import org.apache.james.experimental.imapserver.client.LoginCommand;
import org.apache.james.experimental.imapserver.client.SelectCommand;
import org.apache.james.experimental.imapserver.client.fetch.FetchBody;
import org.apache.james.experimental.imapserver.client.fetch.FetchHeader;
import org.apache.james.experimental.imapserver.store.MailboxException;
import org.apache.james.experimental.imapserver.util.MessageGenerator;
import org.apache.james.mailboxmanager.MailboxManagerException;

/**
 * 
 * 
 * 
 * TODO test one message number instead of message sets (1 instead of 1:1)
 *
 */

public class BodyFetchSessionTest extends AbstractSessionTest {
    
    String[] onlyInbox = {USER_MAILBOX_ROOT+".INBOX"};
    MimeMessage[] msgs= null;
    long[] uids = null;
    
    public void setUp() throws MailboxException, MessagingException, IOException, MailboxManagerException {
        super.setUp();
        msgs=MessageGenerator.generateSimpleMessages(4);
        createFolders(onlyInbox);
        // increase the uid
        appendMessagesClosed(USER_MAILBOX_ROOT+".INBOX",msgs);
        deleteAll(USER_MAILBOX_ROOT+".INBOX");
        uids=addUIDMessagesOpen(USER_MAILBOX_ROOT+".INBOX",msgs);
    }
    
    
    public void testFetchCompleteAndSize() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        fc.setFetchRfc822Size(true);
        fc.setFetchBody(new FetchBody(true));
        // TODO test \Seen get's not set because of peek and vice versa
        verifyCommandOrdered(fc);
    }
    public void testFetchCompleteHeaderAndSize() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        fc.setFetchRfc822Size(true);
        FetchBody fetchBody=new FetchBody(true);
        fetchBody.setFetchHeader(new FetchHeader());
        fc.setFetchBody(fetchBody);
        verifyCommandOrdered(fc);
    }
    public void testFetchSomeExistingHeaderAndSize() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        fc.setFetchRfc822Size(true);
        FetchHeader fetchHeader=new FetchHeader();
        fetchHeader.setFields(new String[] {"Date","From","To"});
        FetchBody fetchBody=new FetchBody(true);
        fetchBody.setFetchHeader(fetchHeader);
        fc.setFetchBody(fetchBody);
        verifyCommandOrdered(fc);
    }
    public void testFetchSomeNonExistingHeaderAndSize() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        fc.setFetchRfc822Size(true);
        FetchHeader fetchHeader=new FetchHeader();
        fetchHeader.setFields(new String[] {"Blob","Test","Oh"});
        FetchBody fetchBody=new FetchBody(true);
        fetchBody.setFetchHeader(fetchHeader);
        fc.setFetchBody(fetchBody);
        verifyCommandOrdered(fc);
    }
    public void testFetchSomeNoneAndExistingHeaderAndSize() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        fc.setFetchRfc822Size(true);
        FetchHeader fetchHeader=new FetchHeader();
        fetchHeader.setFields(new String[] {"To","Message-ID","Blob","Test","Oh"});
        FetchBody fetchBody=new FetchBody(true);
        fetchBody.setFetchHeader(fetchHeader);
        fc.setFetchBody(fetchBody);
        verifyCommandOrdered(fc);
    }

    
    public void testFetchSomeExistingHeader() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        
        FetchCommand fc=new FetchCommand(msgs,1,-1);
        FetchHeader fetchHeader=new FetchHeader();
        fetchHeader.setFields(new String[] {"Date","From","To"});
        FetchBody fetchBody=new FetchBody(true);
        fetchBody.setFetchHeader(fetchHeader);
        fc.setFetchBody(fetchBody);
        verifyCommandOrdered(fc);
    }        

    public void testFetchBodyNoParenthesisOneSeqNumber() throws ProtocolException, IOException, MessagingException, MailboxManagerException {
        verifyCommand(new LoginCommand(USER_NAME,USER_PASSWORD));
        verifyCommand(new SelectCommand("INBOX", msgs, getUidValidity(USER_MAILBOX_ROOT+".INBOX")));
        msgs=getMessages(USER_MAILBOX_ROOT+".INBOX");
        
        FetchCommand fc=new FetchCommand(msgs,1);
        fc.setFetchBody(new FetchBody(true));
        fc.setUseParenthesis(false);
        verifyCommandOrdered(fc);
        
    }
}
