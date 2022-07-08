package phoenixit.education.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import phoenixit.education.components.MailInfo;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
public class MailHandleExceptionInfo {

    private String message;
    private List<MailInfo> mailInfo;

}
