package hospital.management.backend.enums;

/**
 * Triage priority levels (START triage system):
 * P1 = Immediate life threat
 * P2 = Urgent (can wait up to 1 hour)
 * P3 = Less urgent (can wait 2-4 hours)
 * P4 = Non-urgent / minor
 */
public enum TriageLevel {
    P1,
    P2,
    P3,
    P4
}
