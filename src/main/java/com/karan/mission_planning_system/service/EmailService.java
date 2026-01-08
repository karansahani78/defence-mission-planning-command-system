package com.karan.mission_planning_system.service;

public interface EmailService {

    void send(String to, String subject, String body);
}
