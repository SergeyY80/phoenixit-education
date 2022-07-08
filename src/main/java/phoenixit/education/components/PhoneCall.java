package phoenixit.education.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import phoenixit.education.models.CallInternalStatus;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;


@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "phone_call")
public class PhoneCall {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MailInfo mailInfo;


}
