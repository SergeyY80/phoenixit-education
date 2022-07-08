package phoenixit.education.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import phoenixit.education.components.MailInfo;
import phoenixit.education.components.PhoneCall;
import phoenixit.education.controllers.Filter;
import phoenixit.education.controllers.Filters;
import phoenixit.education.exceptions.MailHandleException;
import phoenixit.education.util.TimeUtil;
import phoenixit.education.services.specifications.MailInfoSpecification;
import phoenixit.education.services.specifications.MailInfoWithCallsSpecification;
import phoenixit.education.services.specifications.SearchCriteria;
import phoenixit.education.repositories.MailInfoRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class MailInfoService {

    private final MailInfoRepository mailInfoRepository;
    private final PhoneCallInfoService phoneCallInfoService;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void saveMails(List<MailInfo> mails) throws MailHandleException {
        Date now = new Date();
        List<MailInfo> errMails = mails.stream().filter(x -> x.getDate().after(now))
                .collect(Collectors.toList());

        if (errMails.size() > 0)
            throw new MailHandleException("Emails were not saved cause some has a future date", mails);

        mailInfoRepository.saveAll(mails);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public int deleteByFilters(Filters filters) {
        List<MailInfo> mails = findByFilters(filters);
        mailInfoRepository.deleteAll(mails);
        return mails.size();
    }

    @Transactional(readOnly = true)
    public List<MailInfo> findByFilters(Filters filters) {
        Specification<MailInfo> spec = Specification.where(null);

        // фильтрация по полям письма
        if (filters.getFilters() != null) {
            List<PhoneCall> calls = phoneCallInfoService.findByFilters(filters);

            if (calls != null) {
                List<Integer> callsId = calls.stream().map(PhoneCall::getMessageId).collect(Collectors.toList());
                spec = spec.and(new MailInfoWithCallsSpecification(callsId));
            }

            var specifications = filters.getFilters().stream()
                                        .map(x -> createSpecification(x.getName(), "=", x.getValue()))
                                        .collect(Collectors.toList());

            for (MailInfoSpecification specification : specifications)
                spec = spec.and(specification);

            // фильтрация по дате письма
            Filter dateRange = filters.getFilters().stream().filter(x -> "dateRange".equals(x.getName()))
                                      .findFirst().orElse(null);
            if (dateRange != null) {
                List<Date> dates = getDatesFromStr(dateRange.getValue());

                if (dates.get(0) != null)
                    spec = spec.and(createSpecification(MailInfo.dateField, ">=", dates.get(0)));
                if (dates.get(1) != null)
                    spec = spec.and(createSpecification(MailInfo.dateField, "<=", dates.get(1)));
            }
        }

        // указываем сортировку и делаем запрос
        if (filters.getSortField() != null) {
            Sort sort = Sort.by(new Sort.Order(filters.getSortDir(), filters.getSortField()));
            return mailInfoRepository.findAll(spec, sort);
        }
        else
            return mailInfoRepository.findAll(spec);
    }


    private MailInfoSpecification createSpecification(String key, String operation, Object value) {
        SearchCriteria criteria = new SearchCriteria(key, operation, value);
        return new MailInfoSpecification(criteria);
    }

    private List<Date> getDatesFromStr(String s) {
        Date startDate = null;
        Date endDate = null;
        String[] dates = s.split(" ÷ ");

        if (!dates[0].trim().equals("_")) {
            try {
                startDate = TimeUtil.DateFromStr(dates[0]);
            }
            catch (Exception ex) {
                log.error("Error parse start date " + dates[0], ex);
            }
        }

        if (!dates[1].trim().equals("_")) {
            try {
                endDate = TimeUtil.DateFromStr(dates[1]);
            }
            catch (Exception ex) {
                log.error("Error parse end date " + dates[1], ex);
            }
        }

        return Arrays.asList(startDate, endDate);
    }



}
