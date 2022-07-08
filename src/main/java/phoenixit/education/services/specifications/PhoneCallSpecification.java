package phoenixit.education.services.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import phoenixit.education.components.PhoneCall;
import phoenixit.education.models.CallInternalStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;


@AllArgsConstructor
public class PhoneCallSpecification implements Specification<PhoneCall> {

    private final SearchCriteria criteria;


    @Override
    public Predicate toPredicate(Root<PhoneCall> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case ">=":
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                    return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                }

                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());

            case "<=":
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                    return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (Date) criteria.getValue());
                }

                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());

            case "=":
                if (root.get(criteria.getKey()).getJavaType() == CallInternalStatus.class) {
                    CallInternalStatus status = null;
                    try {
                        status = CallInternalStatus.valueOf(criteria.getValue().toString());
                    }
                    catch (Exception ignored) {
                    }

                    return builder.equal(root.get(criteria.getKey()), status);
                }

                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

        return null;
    }


}
