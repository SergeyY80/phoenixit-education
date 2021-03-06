package phoenixit.education.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phoenixit.education.components.PhoneCall;
import phoenixit.education.controllers.Filter;
import phoenixit.education.services.specifications.PhoneCallSpecification;
import phoenixit.education.repositories.PhoneCallRepository;
import phoenixit.education.services.specifications.SearchCriteria;

import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
@AllArgsConstructor
public class PhoneCallInfoService {

    private final PhoneCallRepository phoneCallRepository;

    @Transactional(readOnly = true)
    public List<PhoneCall> findByFilters(List<Filter> filters) {
        var specifications = filters.stream()
                .map(x -> createSpecification(x.getName(), "=", x.getValue()))
                .collect(Collectors.toList());

        Specification<PhoneCall> spec = Specification.where(null);

        for (PhoneCallSpecification specification : specifications)
            spec = spec.and(specification);

        return phoneCallRepository.findAll(spec);
    }


    private PhoneCallSpecification createSpecification(String key, String operation, Object value) {
        SearchCriteria criteria = new SearchCriteria(key, operation, value);
        return new PhoneCallSpecification(criteria);
    }



}
