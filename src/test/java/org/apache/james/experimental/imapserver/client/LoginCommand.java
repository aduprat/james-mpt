package org.apache.james.experimental.imapserver.client;


public class LoginCommand extends AbstractCommand {

    
    public LoginCommand(String userName, String password) {
        command="LOGIN "+userName+" "+password+"\n";
        statusResponse="OK LOGIN completed.";
    }



}
