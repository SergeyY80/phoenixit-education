package phoenixit.education.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import phoenixit.education.components.MailInfo;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
public class MailHandleExceptionInfo {

    private String message;
    private List<MailInfo> mailInfo;


}
