package phoenixit.education.services.specifications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import phoenixit.education.components.MailInfo;

import javax.persistence.criteria.*;
import java.util.Date;


@AllArgsConstructor
public class MailInfoSpecification implements Specification<MailInfo> {

    private final SearchCriteria criteria;


    @Override
    public Predicate toPredicate(Root<MailInfo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());

        }

        return null;
    }



}
