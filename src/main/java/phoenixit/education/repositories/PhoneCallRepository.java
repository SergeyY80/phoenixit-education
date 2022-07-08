package phoenixit.education.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import phoenixit.education.components.PhoneCall;


@Repository
public interface PhoneCallRepository extends JpaRepository<PhoneCall, String>, JpaSpecificationExecutor<PhoneCall> {

    @Modifying
    @Transactional(propagation = Propagation.REQUIRED)
    @Query("UPDATE PhoneCall C SET C.messageId = :messageId WHERE C.id = :callId")
    void updateMessageId(@Param("callId") Integer callId, @Param("messageId") Integer messageId);


}
