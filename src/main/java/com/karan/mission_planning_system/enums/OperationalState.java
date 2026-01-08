package com.karan.mission_planning_system.enums;

public enum OperationalState {

    OPERATIONAL,            // Fully mission capable
    LIMITED_OPERATIONAL,    // Can operate with restrictions
    NON_OPERATIONAL,        // Cannot be deployed
    EMERGENCY_ONLY,         // Use only in emergency
    UNDER_TESTING
}
