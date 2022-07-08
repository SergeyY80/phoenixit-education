package phoenixit.education.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import phoenixit.education.components.MailInfo;
import phoenixit.education.exceptions.MailHandleException;
import phoenixit.education.services.MailInfoService;

import java.util.List;


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
            mailInfoService.saveMails(mails);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Throwable e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("/delete_mails")
    public ResponseEntity<String> deleteMails(@RequestBody Filters filters) {
        try {
            Integer i = mailInfoService.deleteByFilters(filters);
            return new ResponseEntity<>(i.toString(), HttpStatus.OK);
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
            String resp = objectMapper.writeValueAsString(mails);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
