package phoenixit.education.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import phoenixit.education.components.MailInfo;
import phoenixit.education.services.MailInfoService;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Controller
@AllArgsConstructor
public class MailInfoController {

    private final MailInfoService mailInfoService;
    private final ObjectMapper objectMapper;


    @ResponseBody
    @PostMapping("/save_mails")
    public ResponseEntity<String> saveMails(@RequestBody List<MailInfo> mails) {
        try {
            mails = mailInfoService.saveMails(mails);
            List<Integer> ids = mails.stream().map(MailInfo::getId).collect(Collectors.toList());
            return new ResponseEntity<>(objectMapper.writeValueAsString(ids), HttpStatus.OK);
        }
        catch (Throwable e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/update_mails")
    public ResponseEntity<String> updateMails(@RequestBody List<MailInfo> mails) {
        try {
            List<Integer> ids = mailInfoService.updateMails(mails).stream().map(MailInfo::getId)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(objectMapper.writeValueAsString(ids), HttpStatus.OK);
        }
        catch (Throwable e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/delete_mails")
    public ResponseEntity<String> deleteMails(@RequestBody Filters filters) {
        try {
            List<Integer> ids = mailInfoService.deleteByFilters(filters).stream().map(MailInfo::getId)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(objectMapper.writeValueAsString(ids), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/get_mails")
    public ResponseEntity<String> getMails(@RequestBody Filters filters) {
        try {
            List<MailInfo> mails = mailInfoService.findByFilters(filters);
            return new ResponseEntity<>(objectMapper.writeValueAsString(mails), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
