package hospital.management.backend.config;

import hospital.management.backend.entity.Appointment;
import hospital.management.backend.entity.Billing;
import hospital.management.backend.entity.Department;
import hospital.management.backend.entity.Doctor;
import hospital.management.backend.entity.Inventory;
import hospital.management.backend.entity.Medicine;
import hospital.management.backend.entity.Patient;
import hospital.management.backend.entity.Payment;
import hospital.management.backend.entity.Role;
import hospital.management.backend.entity.Supplier;
import hospital.management.backend.entity.User;
import hospital.management.backend.enums.AppointmentStatus;
import hospital.management.backend.enums.BillingStatus;
import hospital.management.backend.enums.DepartmentStatus;
import hospital.management.backend.enums.DoctorStatus;
import hospital.management.backend.enums.Gender;
import hospital.management.backend.enums.PaymentMethod;
import hospital.management.backend.repository.AppointmentRepository;
import hospital.management.backend.repository.BillingRepository;
import hospital.management.backend.repository.DepartmentRepository;
import hospital.management.backend.repository.DoctorRepository;
import hospital.management.backend.repository.InventoryRepository;
import hospital.management.backend.repository.MedicineRepository;
import hospital.management.backend.repository.PatientRepository;
import hospital.management.backend.repository.PaymentRepository;
import hospital.management.backend.repository.RoleRepository;
import hospital.management.backend.repository.SupplierRepository;
import hospital.management.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final SupplierRepository supplierRepository;
    private final MedicineRepository medicineRepository;
    private final InventoryRepository inventoryRepository;
    private final BillingRepository billingRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
        roleRepository.findByName("PATIENT").orElseGet(() -> roleRepository.save(Role.builder().name("PATIENT").build()));
        Role doctorRole = roleRepository.findByName("DOCTOR").orElseGet(() -> roleRepository.save(Role.builder().name("DOCTOR").build()));
        roleRepository.findByName("RECEPTIONIST").orElseGet(() -> roleRepository.save(Role.builder().name("RECEPTIONIST").build()));
        roleRepository.findByName("PHARMACIST").orElseGet(() -> roleRepository.save(Role.builder().name("PHARMACIST").build()));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(adminRole)
                    .build());
        }

        if (patientRepository.count() == 0) {
            patientRepository.save(Patient.builder()
                    .patientNumber("P-0001")
                    .firstName("Asha")
                    .lastName("Patel")
                    .dateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                    .gender(hospital.management.backend.enums.Gender.FEMALE)
                    .email("asha@example.com")
                    .phone("9876543210")
                    .build());
        }

                Department department = departmentRepository.findAll().stream().findFirst().orElseGet(() -> departmentRepository.save(Department.builder()
                    .name("Cardiology").code("CARD").description("Cardiology department").status(DepartmentStatus.ACTIVE).build()));
                User doctorUser = userRepository.findByUsername("doctor").orElseGet(() -> userRepository.save(User.builder()
                    .username("doctor").email("doctor@example.com").password(passwordEncoder.encode("doctor123"))
                    .role(doctorRole).build()));
                if (!doctorRepository.existsByDoctorCode("DOC-0001")) {
                    doctorRepository.save(Doctor.builder().user(doctorUser).department(department).doctorCode("DOC-0001")
                        .firstName("Ravi").lastName("Sharma").licenseNumber("LIC-0001")
                        .specialization("Cardiology").phone("9876543211").yearsExperience(10)
                        .status(DoctorStatus.ACTIVE).consultationFee(1000.0).build());
                }

        // ── Extra departments ──
        Department neurology = departmentRepository.findByCode("NEUR").orElseGet(() -> departmentRepository.save(
                Department.builder().name("Neurology").code("NEUR").description("Neurology department").status(DepartmentStatus.ACTIVE).build()));
        Department pediatrics = departmentRepository.findByCode("PEDS").orElseGet(() -> departmentRepository.save(
                Department.builder().name("Pediatrics").code("PEDS").description("Pediatrics department").status(DepartmentStatus.ACTIVE).build()));
        Department orthopedics = departmentRepository.findByCode("ORTH").orElseGet(() -> departmentRepository.save(
                Department.builder().name("Orthopedics").code("ORTH").description("Orthopedics department").status(DepartmentStatus.ACTIVE).build()));
        Department generalMed = departmentRepository.findByCode("GENM").orElseGet(() -> departmentRepository.save(
                Department.builder().name("General Medicine").code("GENM").description("General Medicine department").status(DepartmentStatus.ACTIVE).build()));

        // ── Extra doctors ──
        if (!doctorRepository.existsByDoctorCode("DOC-0002")) {
            User doc2 = userRepository.findByUsername("dr.gupta").orElseGet(() -> userRepository.save(User.builder()
                    .username("dr.gupta").email("gupta@hms.local").password(passwordEncoder.encode("doctor123")).role(doctorRole).build()));
            doctorRepository.save(Doctor.builder().user(doc2).department(neurology).doctorCode("DOC-0002")
                    .firstName("Anil").lastName("Gupta").licenseNumber("LIC-0002").specialization("Neurology")
                    .phone("9876543212").yearsExperience(8).status(DoctorStatus.ACTIVE).consultationFee(1200.0).build());
        }
        if (!doctorRepository.existsByDoctorCode("DOC-0003")) {
            User doc3 = userRepository.findByUsername("dr.rao").orElseGet(() -> userRepository.save(User.builder()
                    .username("dr.rao").email("rao@hms.local").password(passwordEncoder.encode("doctor123")).role(doctorRole).build()));
            doctorRepository.save(Doctor.builder().user(doc3).department(pediatrics).doctorCode("DOC-0003")
                    .firstName("Priya").lastName("Rao").licenseNumber("LIC-0003").specialization("Pediatrics")
                    .phone("9876543213").yearsExperience(5).status(DoctorStatus.ACTIVE).consultationFee(800.0).build());
        }
        if (!doctorRepository.existsByDoctorCode("DOC-0004")) {
            User doc4 = userRepository.findByUsername("dr.khan").orElseGet(() -> userRepository.save(User.builder()
                    .username("dr.khan").email("khan@hms.local").password(passwordEncoder.encode("doctor123")).role(doctorRole).build()));
            doctorRepository.save(Doctor.builder().user(doc4).department(orthopedics).doctorCode("DOC-0004")
                    .firstName("Imran").lastName("Khan").licenseNumber("LIC-0004").specialization("Orthopedics")
                    .phone("9876543214").yearsExperience(12).status(DoctorStatus.ACTIVE).consultationFee(1500.0).build());
        }
        if (!doctorRepository.existsByDoctorCode("DOC-0005")) {
            User doc5 = userRepository.findByUsername("dr.nair").orElseGet(() -> userRepository.save(User.builder()
                    .username("dr.nair").email("nair@hms.local").password(passwordEncoder.encode("doctor123")).role(doctorRole).build()));
            doctorRepository.save(Doctor.builder().user(doc5).department(generalMed).doctorCode("DOC-0005")
                    .firstName("Meera").lastName("Nair").licenseNumber("LIC-0005").specialization("General Medicine")
                    .phone("9876543215").yearsExperience(3).status(DoctorStatus.ACTIVE).consultationFee(500.0).build());
        }

        // ── Extra patients ──
        if (patientRepository.count() <= 1) {
            patientRepository.saveAll(List.of(
                    Patient.builder().patientNumber("P-0002").firstName("Rohan").lastName("Verma")
                            .dateOfBirth(LocalDate.of(2001, 7, 19)).gender(Gender.MALE)
                            .email("rohan.verma@mail.com").phone("9855555555")
                            .address("78 Salt Lake, Kolkata").bloodGroup("B-").build(),
                    Patient.builder().patientNumber("P-0003").firstName("Priya").lastName("Nair")
                            .dateOfBirth(LocalDate.of(1985, 11, 2)).gender(Gender.FEMALE)
                            .email("priya.nair@mail.com").phone("9833333333")
                            .address("45 Park Street, Kolkata").bloodGroup("A+").build(),
                    Patient.builder().patientNumber("P-0004").firstName("Aarav").lastName("Mehta")
                            .dateOfBirth(LocalDate.of(1990, 4, 12)).gender(Gender.MALE)
                            .email("aarav.mehta@mail.com").phone("9811111111")
                            .address("12 MG Road, Kolkata").bloodGroup("O+").build(),
                    Patient.builder().patientNumber("P-0005").firstName("Sneha").lastName("Reddy")
                            .dateOfBirth(LocalDate.of(1995, 3, 25)).gender(Gender.FEMALE)
                            .email("sneha.reddy@mail.com").phone("9822222222")
                            .address("23 Camac Street, Kolkata").bloodGroup("AB+").build(),
                    Patient.builder().patientNumber("P-0006").firstName("Vikram").lastName("Singh")
                            .dateOfBirth(LocalDate.of(1978, 9, 15)).gender(Gender.MALE)
                            .email("vikram.singh@mail.com").phone("9844444444")
                            .address("56 Ballygunge, Kolkata").bloodGroup("O-").build(),
                    Patient.builder().patientNumber("P-0007").firstName("Ananya").lastName("Das")
                            .dateOfBirth(LocalDate.of(2003, 12, 5)).gender(Gender.FEMALE)
                            .email("ananya.das@mail.com").phone("9866666666")
                            .address("89 Behala, Kolkata").bloodGroup("A-").build(),
                    Patient.builder().patientNumber("P-0008").firstName("Karan").lastName("Malhotra")
                            .dateOfBirth(LocalDate.of(1988, 6, 30)).gender(Gender.MALE)
                            .email("karan.malhotra@mail.com").phone("9877777777")
                            .address("34 Gariahat, Kolkata").bloodGroup("B+").build(),
                    Patient.builder().patientNumber("P-0009").firstName("Diya").lastName("Shah")
                            .dateOfBirth(LocalDate.of(1999, 2, 14)).gender(Gender.FEMALE)
                            .email("diya.shah@mail.com").phone("9888888888")
                            .address("67 Esplanade, Kolkata").bloodGroup("AB-").build(),
                    Patient.builder().patientNumber("P-0010").firstName("Arjun").lastName("Kapoor")
                            .dateOfBirth(LocalDate.of(1972, 8, 21)).gender(Gender.MALE)
                            .email("arjun.kapoor@mail.com").phone("9899999999")
                            .address("90 Howrah, Kolkata").bloodGroup("O+").build(),
                    Patient.builder().patientNumber("P-0011").firstName("Isha").lastName("Iyer")
                            .dateOfBirth(LocalDate.of(1993, 5, 18)).gender(Gender.FEMALE)
                            .email("isha.iyer@mail.com").phone("9700000000")
                            .address("11 Park Circus, Kolkata").bloodGroup("A+").build(),
                    Patient.builder().patientNumber("P-0012").firstName("Rahul").lastName("Joshi")
                            .dateOfBirth(LocalDate.of(1982, 10, 10)).gender(Gender.MALE)
                            .email("rahul.joshi@mail.com").phone("9711111111")
                            .address("22 Sealdah, Kolkata").bloodGroup("B+").build()
            ));
        }

        // ── Appointments ──
        if (appointmentRepository.count() == 0) {
            List<Patient> patients = patientRepository.findAll();
            List<Doctor> doctors = doctorRepository.findAll();
            if (patients.size() >= 5 && doctors.size() >= 3) {
                appointmentRepository.saveAll(List.of(
                        Appointment.builder().patient(patients.get(0)).doctor(doctors.get(0))
                                .appointmentDate(LocalDate.now().plusDays(1)).appointmentTime(LocalTime.of(10, 0))
                                .status(AppointmentStatus.SCHEDULED).reason("Routine check-up").build(),
                        Appointment.builder().patient(patients.get(1)).doctor(doctors.get(1))
                                .appointmentDate(LocalDate.now().plusDays(2)).appointmentTime(LocalTime.of(11, 30))
                                .status(AppointmentStatus.CONFIRMED).reason("Headache consultation").build(),
                        Appointment.builder().patient(patients.get(2)).doctor(doctors.get(2))
                                .appointmentDate(LocalDate.now().minusDays(1)).appointmentTime(LocalTime.of(9, 0))
                                .status(AppointmentStatus.COMPLETED).reason("Child vaccination").build(),
                        Appointment.builder().patient(patients.get(3)).doctor(doctors.get(3))
                                .appointmentDate(LocalDate.now().minusDays(3)).appointmentTime(LocalTime.of(14, 0))
                                .status(AppointmentStatus.COMPLETED).reason("Knee pain follow-up").build(),
                        Appointment.builder().patient(patients.get(4)).doctor(doctors.get(0))
                                .appointmentDate(LocalDate.now().plusDays(5)).appointmentTime(LocalTime.of(15, 30))
                                .status(AppointmentStatus.SCHEDULED).reason("ECG recommended").build(),
                        Appointment.builder().patient(patients.get(5)).doctor(doctors.get(4))
                                .appointmentDate(LocalDate.now().minusDays(2)).appointmentTime(LocalTime.of(16, 0))
                                .status(AppointmentStatus.CANCELLED).reason("Fever and cold").build(),
                        Appointment.builder().patient(patients.get(6)).doctor(doctors.get(1))
                                .appointmentDate(LocalDate.now().plusDays(3)).appointmentTime(LocalTime.of(12, 0))
                                .status(AppointmentStatus.CONFIRMED).reason("MRI review").build(),
                        Appointment.builder().patient(patients.get(7)).doctor(doctors.get(2))
                                .appointmentDate(LocalDate.now().minusDays(5)).appointmentTime(LocalTime.of(10, 30))
                                .status(AppointmentStatus.NO_SHOW).reason("Pediatric consultation").build(),
                        Appointment.builder().patient(patients.get(8)).doctor(doctors.get(3))
                                .appointmentDate(LocalDate.now().plusDays(7)).appointmentTime(LocalTime.of(13, 0))
                                .status(AppointmentStatus.SCHEDULED).reason("Fracture follow-up").build(),
                        Appointment.builder().patient(patients.get(9)).doctor(doctors.get(4))
                                .appointmentDate(LocalDate.now().minusDays(1)).appointmentTime(LocalTime.of(17, 0))
                                .status(AppointmentStatus.COMPLETED).reason("General health check").build()
                ));
            }
        }

        // ── Suppliers ──
        if (supplierRepository.count() == 0) {
            supplierRepository.saveAll(List.of(
                    Supplier.builder().name("MediSupply India").contactPerson("Rajesh Kumar")
                            .phone("9001112222").email("rajesh@medisupply.in")
                            .address("Mumbai, Maharashtra").build(),
                    Supplier.builder().name("PharmaCorp Ltd").contactPerson("Anita Desai")
                            .phone("9003334444").email("anita@pharmacorp.in")
                            .address("Delhi, New Delhi").build(),
                    Supplier.builder().name("HealthLine Distributors").contactPerson("Sunil Agarwal")
                            .phone("9005556666").email("sunil@healthline.in")
                            .address("Bangalore, Karnataka").build()
            ));
        }

        // ── Medicines + Inventory ──
        if (medicineRepository.count() == 0) {
            List<Supplier> suppliers = supplierRepository.findAll();
            if (!suppliers.isEmpty()) {
                Supplier s1 = suppliers.get(0);
                Supplier s2 = suppliers.size() > 1 ? suppliers.get(1) : s1;
                Supplier s3 = suppliers.size() > 2 ? suppliers.get(2) : s1;

                List<Medicine> meds = List.of(
                        Medicine.builder().supplier(s1).name("Paracetamol 500mg").manufacturer("Cipla")
                                .unitPrice(new BigDecimal("5.50")).build(),
                        Medicine.builder().supplier(s1).name("Amoxicillin 250mg").manufacturer("Sun Pharma")
                                .unitPrice(new BigDecimal("12.00")).build(),
                        Medicine.builder().supplier(s2).name("Omeprazole 20mg").manufacturer("Dr. Reddy's")
                                .unitPrice(new BigDecimal("8.75")).build(),
                        Medicine.builder().supplier(s2).name("Metformin 500mg").manufacturer("Lupin")
                                .unitPrice(new BigDecimal("15.00")).build(),
                        Medicine.builder().supplier(s3).name("Atorvastatin 10mg").manufacturer("Pfizer")
                                .unitPrice(new BigDecimal("22.50")).build(),
                        Medicine.builder().supplier(s3).name("Azithromycin 500mg").manufacturer("Glenmark")
                                .unitPrice(new BigDecimal("35.00")).build(),
                        Medicine.builder().supplier(s1).name("Cetirizine 10mg").manufacturer("Hetero")
                                .unitPrice(new BigDecimal("4.00")).build(),
                        Medicine.builder().supplier(s2).name("Ibuprofen 400mg").manufacturer("Mankind")
                                .unitPrice(new BigDecimal("6.50")).build()
                );
                List<Medicine> saved = medicineRepository.saveAll(meds);

                int[] stock = {500, 200, 150, 300, 80, 120, 400, 250};
                int[] reorder = {100, 50, 40, 80, 20, 30, 100, 60};
                LocalDate[] expiry = {
                        LocalDate.now().plusMonths(18), LocalDate.now().plusMonths(12),
                        LocalDate.now().plusMonths(24), LocalDate.now().plusMonths(15),
                        LocalDate.now().plusMonths(10), LocalDate.now().plusMonths(8),
                        LocalDate.now().plusMonths(20), LocalDate.now().plusMonths(14)
                };
                for (int i = 0; i < saved.size(); i++) {
                    inventoryRepository.save(Inventory.builder().medicine(saved.get(i))
                            .stockQuantity(stock[i]).reorderLevel(reorder[i]).expiryDate(expiry[i]).build());
                }
            }
        }

        // ── Bills + Payments ──
        if (billingRepository.count() == 0) {
            List<Patient> patients = patientRepository.findAll();
            List<Appointment> appointments = appointmentRepository.findAll();
            if (patients.size() >= 5) {
                Billing b1 = billingRepository.save(Billing.builder()
                        .patient(patients.get(2)).appointment(appointments.size() > 2 ? appointments.get(2) : null)
                        .consultationFee(new BigDecimal("800.00")).medicineCharges(new BigDecimal("350.00"))
                        .otherCharges(new BigDecimal("100.00")).status(BillingStatus.PAID).build());
                paymentRepository.save(Payment.builder().bill(b1).amount(new BigDecimal("1250.00"))
                        .paymentMethod(PaymentMethod.CASH).build());

                Billing b2 = billingRepository.save(Billing.builder()
                        .patient(patients.get(3)).appointment(appointments.size() > 3 ? appointments.get(3) : null)
                        .consultationFee(new BigDecimal("1500.00")).medicineCharges(new BigDecimal("500.00"))
                        .otherCharges(new BigDecimal("200.00")).status(BillingStatus.PAID).build());
                paymentRepository.save(Payment.builder().bill(b2).amount(new BigDecimal("2200.00"))
                        .paymentMethod(PaymentMethod.CARD).build());

                Billing b3 = billingRepository.save(Billing.builder()
                        .patient(patients.get(9)).appointment(appointments.size() > 9 ? appointments.get(9) : null)
                        .consultationFee(new BigDecimal("500.00")).medicineCharges(new BigDecimal("120.00"))
                        .otherCharges(new BigDecimal("50.00")).status(BillingStatus.PAID).build());
                paymentRepository.save(Payment.builder().bill(b3).amount(new BigDecimal("670.00"))
                        .paymentMethod(PaymentMethod.UPI).build());

                billingRepository.save(Billing.builder()
                        .patient(patients.get(0)).appointment(appointments.size() > 0 ? appointments.get(0) : null)
                        .consultationFee(new BigDecimal("1000.00")).medicineCharges(new BigDecimal("0.00"))
                        .otherCharges(new BigDecimal("0.00")).status(BillingStatus.PENDING).build());

                billingRepository.save(Billing.builder()
                        .patient(patients.get(4)).appointment(appointments.size() > 4 ? appointments.get(4) : null)
                        .consultationFee(new BigDecimal("1000.00")).medicineCharges(new BigDecimal("250.00"))
                        .otherCharges(new BigDecimal("0.00")).status(BillingStatus.PENDING).build());

                billingRepository.save(Billing.builder()
                        .patient(patients.get(6)).appointment(appointments.size() > 6 ? appointments.get(6) : null)
                        .consultationFee(new BigDecimal("1200.00")).medicineCharges(new BigDecimal("400.00"))
                        .otherCharges(new BigDecimal("150.00")).status(BillingStatus.PARTIALLY_PAID).build());
            }
        }
    }
}