package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.ContactService;
import com.jamie.home.api.service.FaqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/contact/*")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            List<CONTACT> list = contactService.list(search);
            Integer cnt = contactService.listCnt(search);
            VoList<CONTACT> result = new VoList<>(cnt, list);

            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CONTACT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute CONTACT contact) {
        try {
            int result = contactService.save(contact);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CONTACT_SUCCESS", contact);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modi(@PathVariable("key") int key, @Validated @ModelAttribute CONTACT contact) {
        try {
            contact.setContact(key);
            int result = contactService.modi(contact);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CONTACT_SUCCESS", contact);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_FAIL", null);
        }
    }
}