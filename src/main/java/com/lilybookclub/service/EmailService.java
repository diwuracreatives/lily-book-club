package com.lilybookclub.service;

import java.util.Map;

public interface EmailService {
     void sendMail(String mailTo, String mailSubject, String templateLocation, Map<String, Object> params);
}
