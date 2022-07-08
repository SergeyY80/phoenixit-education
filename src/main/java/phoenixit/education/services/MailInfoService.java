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
import phoenixit.education.repositories.PhoneCallRepository;
import phoenixit.education.util.TimeUtil;
import phoenixit.education.services.specifications.MailInfoSpecification;
import phoenixit.education.services.specifications.MailInfoWithCallsSpecification;
import phoenixit.education.services.specifications.SearchCriteria;
import phoenixit.education.repositories.MailInfoRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class MailInfoService {

    private final MailInfoRepository mailInfoRepository;
    private final PhoneCallRepository phoneCallRepository;
    private final PhoneCallInfoService phoneCallInfoService;

    private final String startDateFieldName = "startDate";
    private final String endDateFieldName = "endDate";


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public List<MailInfo> saveMails(List<MailInfo> mails) throws MailHandleException {
        mails.forEach(x -> x.setId(null));
        checkMailsDate(mails, "Emails not saved cause some has a future date");
        return saveMailList(mails);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public List<MailInfo> updateMails(List<MailInfo> mails) throws MailHandleException {
        checkMailsDate(mails, "Emails not updated cause some has a future date");
        return saveMailList(mails);
    }

    private List<MailInfo> saveMailList(List<MailInfo> mails) {
        mails = mailInfoRepository.saveAll(mails);

        for (MailInfo mail : mails)
            for (PhoneCall call : mail.getCalls()) {
                call.setMessageId(mail.getId());
                phoneCallRepository.updateMessageId(call.getId(), mail.getId());
            }

        return mails;
    }

    private void checkMailsDate(List<MailInfo> mails, String errMsg) throws MailHandleException {
        Date now = new Date();
        List<MailInfo> errMails = mails.stream().filter(x -> x.getDate() != null && x.getDate().after(now))
                .collect(Collectors.toList());

        if (errMails.size() > 0)
            throw new MailHandleException(errMsg, errMails);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public List<MailInfo> deleteByFilters(Filters filters) {
        List<MailInfo> mails = findByFilters(filters);
        mailInfoRepository.deleteAll(mails);
        return mails;
    }

    @Transactional(readOnly = true)
    public List<MailInfo> findByFilters(Filters filters) {
        Specification<MailInfo> spec = Specification.where(null);

        // фильтрация по полям звонков
        if (filters.getCallFilters() != null && !filters.getCallFilters().isEmpty()) {
            List<PhoneCall> calls = phoneCallInfoService.findByFilters(filters.getCallFilters());
            if (calls.isEmpty())
                return new ArrayList<>();

            List<Integer> callsId = calls.stream().map(PhoneCall::getMessageId).collect(Collectors.toList());
            spec = spec.and(new MailInfoWithCallsSpecification(callsId));
        }

        // фильтрация по полям письма
        if (filters.getFilters() != null) {
            var specifications = filters.getFilters().stream()
                    .filter(x -> !startDateFieldName.equals(x.getName()) && !endDateFieldName.equals(x.getName()))
                    .map(x -> createSpecification(x.getName(), "=", x.getValue()))
                    .collect(Collectors.toList());

            for (MailInfoSpecification specification : specifications)
                spec = spec.and(specification);

            // фильтрация по дате письма
            Filter startDateFilter = filters.getFilters().stream().filter(x -> startDateFieldName.equals(x.getName()))
                                      .findFirst().orElse(null);

            if (startDateFilter != null) {
                try {
                    Date startDate = TimeUtil.DateFromStr(startDateFilter.getValue());
                    spec = spec.and(createSpecification(MailInfo.dateField, ">=", startDate));
                }
                catch (ParseException e) {
                    log.error("Error parse start date: " + startDateFilter.getValue(), e);
                    return new ArrayList<>();
                }
            }

            Filter endDateFilter = filters.getFilters().stream().filter(x -> endDateFieldName.equals(x.getName()))
                    .findFirst().orElse(null);

            if (endDateFilter != null) {
                try {
                    Date endDate = TimeUtil.DateFromStr(endDateFilter.getValue());
                    spec = spec.and(createSpecification(MailInfo.dateField, "<=", endDate));
                }
                catch (ParseException e) {
                    log.error("Error parse end date: " + endDateFilter.getValue(), e);
                    return new ArrayList<>();
                }
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



}
