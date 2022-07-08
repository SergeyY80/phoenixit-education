package phoenixit.education.services.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import phoenixit.education.components.MailInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@AllArgsConstructor
public class MailInfoWithCallsSpecification implements Specification<MailInfo> {

    private final List<Integer> callsId;


    @Override
    public Predicate toPredicate(Root<MailInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return root.get("id").in(callsId);
    }


}
