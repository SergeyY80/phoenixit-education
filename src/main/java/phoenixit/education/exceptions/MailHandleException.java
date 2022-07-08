package phoenixit.education.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import phoenixit.education.components.MailInfo;

import java.util.List;


public class MailHandleException extends Throwable {

    private String info;

    public MailHandleException(String msg, List<MailInfo> mailInfo) {
        var exceptionInfo = new MailHandleExceptionInfo(msg, mailInfo);

        try {
            info = new ObjectMapper().writeValueAsString(exceptionInfo);
        }
        catch (Exception e) {
            // NO_OP
        }
    }


    @Override
    public String getMessage() {
        return info;
    }

    @AllArgsConstructor
    private static class MailHandleExceptionInfo {

        private final String message;
        private final List<MailInfo> mailInfo;

    }

}
