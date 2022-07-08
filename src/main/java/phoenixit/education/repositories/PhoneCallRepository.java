package phoenixit.education.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import phoenixit.education.components.PhoneCall;
import phoenixit.education.models.CallInternalStatus;


@Repository
public interface PhoneCallRepository extends JpaRepository<PhoneCall, String>, JpaSpecificationExecutor<PhoneCall> {

    @Modifying
    @Query("UPDATE PhoneCall C SET C.status = :callStatus WHERE C.id = :callId")
    void updateCallStatus(@Param("callId") String callId, @Param("callStatus") CallInternalStatus callInternalStatus);


}
