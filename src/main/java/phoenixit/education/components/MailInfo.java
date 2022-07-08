package phoenixit.education.components;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import phoenixit.education.util.TimeUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Slf4j
@Setter
@Getter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "mail_info")
public class MailInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Почтовый ящик из которого получено письмо
     */
    @Column(name = "email")
    private String email;

    /**
     * Получатели письма
     */
    @Column(name = "recipient")
    private String recipient;

    /**
     * Отправитель письма
     */
    @Column(name = "sender")
    private String sender;

    /**
     * Тема
     */
    @Column(name = "subject")
    private String subject;

    /**
     * Дата письма
     */
    @Column(name = "date")
    private Date date;
    public static String dateField = "date";


    public void setDate(String dateStr) {
        try {
            this.date = TimeUtil.DateFromStr(dateStr);
        }
        catch (Exception ex) {
            log.error("Error message date parsing - " + dateStr);
            dateStr = null;
        }
    }

    /**
     * Дозвоны совершённые по данному сообщению
     */
    @OneToMany(mappedBy = "mailInfo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PhoneCall> calls = new ArrayList<>();

    public void addPhoneCall(PhoneCall phoneCall) {
        phoneCall.setMailInfo(this);
        phoneCall.setMessageId(id);
        calls.add(phoneCall);
    }


}
