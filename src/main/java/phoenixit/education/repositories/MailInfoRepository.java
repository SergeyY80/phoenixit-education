package phoenixit.education.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import phoenixit.education.components.MailInfo;


@Repository
public interface MailInfoRepository extends JpaRepository<MailInfo, String>, JpaSpecificationExecutor<MailInfo> {


}
