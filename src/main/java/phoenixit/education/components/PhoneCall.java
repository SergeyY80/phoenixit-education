package phoenixit.education.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import phoenixit.education.models.CallInternalStatus;

import javax.persistence.*;
import java.util.Date;


@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "phone_call")
public class PhoneCall {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "messageId", insertable = false, updatable = false)
    private Integer messageId;

    @Column(name = "status")
    private CallInternalStatus status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "callDate")
    private Date callDate;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private MailInfo mailInfo;


    public PhoneCall(MailInfo mailInfo) {
        this.mailInfo = mailInfo;
    }

}
