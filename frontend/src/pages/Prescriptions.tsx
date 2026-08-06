import { useEffect, useState } from "react";
import {
  Alert,
  Autocomplete,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  Grid,
  IconButton,
  Paper,
  Step,
  StepLabel,
  Stepper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon, Receipt as ReceiptIcon, Payment as PaymentIcon, Print as PrintIcon, UploadFile as UploadIcon, Download as DownloadIcon } from "@mui/icons-material";
import { useAuth } from "../contexts/AuthContext";
import { getPatients } from "../services/patientService";
import { getDoctors } from "../services/doctorService";
import { getMedicines } from "../services/medicineService";
import {
  createPrescription,
  deletePrescription,
  getPrescriptions,
} from "../services/prescriptionService";
import { createBilling } from "../services/billingService";
import { createPayment } from "../services/paymentService";
import { uploadDocument, getPatientDocuments } from "../services/documentService";
import type { DocumentMetadataResponse } from "../services/documentService";
import type { Patient } from "../types/patient";
import type { Doctor, Medicine, Prescription } from "../types/clinical";

export default function Prescriptions() {
  const { user } = useAuth();
  const [prescriptions, setPrescriptions] = useState<Prescription[]>([]);
  const [patients, setPatients] = useState<Patient[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [medicines, setMedicines] = useState<Medicine[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // Pagination
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);

  // Stepper Wizard State
  const [open, setOpen] = useState(false);
  const [activeStep, setActiveStep] = useState(0);

  // Form State
  // Step 1: Patient Search & Prescription DETAILS
  const [patientNameInput, setPatientNameInput] = useState("");
  const [patientMobileInput, setPatientMobileInput] = useState("");
  const [patientSearchStatus, setPatientSearchStatus] = useState<"IDLE" | "SUCCESS" | "NOT_FOUND" | "MULTIPLE">("IDLE");
  const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);

  const [selectedDoctor, setSelectedDoctor] = useState<Doctor | null>(null);
  const [doctorSearchId, setDoctorSearchId] = useState("");
  const [medicalRecordId, setMedicalRecordId] = useState("1");
  const [notes, setNotes] = useState("");
  const [prescriptionItems, setPrescriptionItems] = useState<
    { medicine: Medicine | null; quantity: number; dosage: string; durationDays: number }[]
  >([]);

  // Step 2: Bill Generation details
  const [consultationFee, setConsultationFee] = useState("0");
  const [medicineCharges, setMedicineCharges] = useState("0");
  const [otherCharges, setOtherCharges] = useState("0");
  const [generatedBill, setGeneratedBill] = useState<any>(null);

  // Step 3: Record Payment details
  const [paymentMethod, setPaymentMethod] = useState<"CASH" | "CARD" | "UPI" | "INSURANCE">("CASH");
  const [paymentAmount, setPaymentAmount] = useState("0");

  const isPharmacist = user?.role === "PHARMACIST";
  const canWrite = Boolean(user && ["ADMIN", "DOCTOR", "PHARMACIST"].includes(user.role));
  const canDelete = Boolean(user && ["ADMIN", "DOCTOR"].includes(user.role));

  // S3 Prescription documents state
  const [s3Documents, setS3Documents] = useState<DocumentMetadataResponse[]>([]);
  const [s3Loading, setS3Loading] = useState(false);
  const [s3Uploading, setS3Uploading] = useState(false);
  const [s3File, setS3File] = useState<File | null>(null);
  const [s3DocName, setS3DocName] = useState("");
  const [s3SelectedPatient, setS3SelectedPatient] = useState<Patient | null>(null);

  const loadData = async () => {
    try {
      setLoading(true);
      setError("");
      const [presData, patData, docData, medData] = await Promise.all([
        getPrescriptions(),
        getPatients(),
        getDoctors(),
        getMedicines(),
      ]);
      setPrescriptions(presData);
      setPatients(patData);
      setDoctors(docData);
      setMedicines(medData);

      if (user?.role === "PATIENT") {
        setS3Loading(true);
        const docs = await getPatientDocuments("PRESCRIPTION");
        setS3Documents(docs);
      }
    } catch (err: any) {
      setError("Failed to load records.");
    } finally {
      setLoading(false);
      setS3Loading(false);
    }
  };

  const handleS3Upload = async () => {
    if (!s3SelectedPatient || !s3File || !s3DocName.trim()) {
      setError("Please select a patient, specify document name, and select a file.");
      return;
    }
    try {
      setS3Uploading(true);
      setError("");
      setSuccess("");
      await uploadDocument(s3SelectedPatient.id, "PRESCRIPTION", s3DocName.trim(), s3File);
      setSuccess(`Prescription document "${s3DocName}" successfully uploaded to S3!`);
      setS3DocName("");
      setS3File(null);
      
      // Reload if we're a patient (or just to be safe)
      if (user?.role === "PATIENT") {
        const docs = await getPatientDocuments("PRESCRIPTION");
        setS3Documents(docs);
      }
    } catch (err) {
      setError("Failed to upload prescription document to S3.");
    } finally {
      setS3Uploading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, [user]);

  const handleOpenWizard = () => {
    setPatientNameInput("");
    setPatientMobileInput("");
    setPatientSearchStatus("IDLE");
    setSelectedPatient(null);
    setSelectedDoctor(null);
    setDoctorSearchId("");
    setMedicalRecordId("1");
    setNotes("");
    setPrescriptionItems([]);
    setConsultationFee("0");
    setMedicineCharges("0");
    setOtherCharges("0");
    setGeneratedBill(null);
    setPaymentAmount("0");
    setPaymentMethod("CASH");
    setActiveStep(0);
    setOpen(true);
  };

  const handleSearchPatient = () => {
    if (!patientNameInput && !patientMobileInput) {
      setError("Please enter a name or mobile number to search.");
      setPatientSearchStatus("IDLE");
      setSelectedPatient(null);
      return;
    }
    setError("");

    const results = patients.filter((p) => {
      const fullName = `${p.firstName} ${p.lastName}`.toLowerCase();
      const matchName = !patientNameInput || fullName.includes(patientNameInput.toLowerCase());
      const matchMobile = !patientMobileInput || p.phone.includes(patientMobileInput);
      return matchName && matchMobile;
    });

    if (results.length === 1) {
      setSelectedPatient(results[0]);
      setPatientSearchStatus("SUCCESS");
    } else if (results.length > 1) {
      setSelectedPatient(null);
      setPatientSearchStatus("MULTIPLE");
    } else {
      setSelectedPatient(null);
      setPatientSearchStatus("NOT_FOUND");
    }
  };

  // Add Item to Prescription
  const handleAddItem = () => {
    setPrescriptionItems((prev) => [
      ...prev,
      { medicine: null, quantity: 1, dosage: "1-0-1", durationDays: 5 },
    ]);
  };

  const handleRemoveItem = (index: number) => {
    setPrescriptionItems((prev) => prev.filter((_, i) => i !== index));
  };

  const handleItemChange = (index: number, key: string, value: any) => {
    setPrescriptionItems((prev) => {
      const copy = [...prev];
      copy[index] = { ...copy[index], [key]: value };
      return copy;
    });
  };

  // Triggered when doctorSearchId changes manually
  const handleDoctorSearchChange = (val: string) => {
    setDoctorSearchId(val);
    const docId = Number(val);
    if (docId) {
      const doc = doctors.find((d) => d.id === docId);
      if (doc) {
        setSelectedDoctor(doc);
        setConsultationFee(String(doc.consultationFee ?? 0));
      } else {
        setSelectedDoctor(null);
      }
    } else {
      setSelectedDoctor(null);
    }
  };

  // Triggered when doctor is chosen via Autocomplete
  const handleDoctorAutocompleteChange = (doc: Doctor | null) => {
    setSelectedDoctor(doc);
    if (doc) {
      setDoctorSearchId(String(doc.id));
      setConsultationFee(String(doc.consultationFee ?? 0));
    } else {
      setDoctorSearchId("");
      setConsultationFee("0");
    }
  };

  // Calculate medicine charges automatically based on selected medicines
  useEffect(() => {
    let sum = 0;
    prescriptionItems.forEach((item) => {
      if (item.medicine) {
        sum += (item.medicine.unitPrice ?? 0) * (item.quantity ?? 1);
      }
    });
    setMedicineCharges(String(sum));
  }, [prescriptionItems]);

  // Step 1: Submit Prescription
  const handleStep1Submit = async () => {
    if (!selectedPatient) {
      setError("Please search and select a Patient.");
      return;
    }
    if (!selectedDoctor) {
      setError("Please select/enter a valid Doctor.");
      return;
    }
    if (prescriptionItems.length === 0) {
      setError("Please add at least one medicine item.");
      return;
    }
    if (prescriptionItems.some((item) => !item.medicine)) {
      setError("Please complete all medicine selection fields.");
      return;
    }

    try {
      setLoading(true);
      setError("");
      const requestItems = prescriptionItems.map((item) => ({
        medicineId: item.medicine!.id,
        dosage: item.dosage,
        durationDays: item.durationDays,
        quantity: item.quantity,
      }));

      const body = {
        patientId: selectedPatient.id,
        doctorId: selectedDoctor.id,
        medicalRecordId: Number(medicalRecordId),
        notes: notes,
        items: requestItems,
      };

      await createPrescription(body);
      setSuccess("Prescription recorded successfully. Moving to Bill Generation...");
      
      // Auto transition to Step 2
      setTimeout(() => {
        setSuccess("");
        setActiveStep(1);
      }, 1500);

      void loadData();
    } catch (err: any) {
      setError(err.response?.data?.error || "Failed to submit prescription.");
    } finally {
      setLoading(false);
    }
  };

  // Step 2: Generate Bill
  const handleStep2Submit = async () => {
    if (!selectedPatient) {
      setError("Missing Patient context.");
      return;
    }
    try {
      setLoading(true);
      setError("");
      const body = {
        patientId: selectedPatient.id,
        appointmentId: undefined,
        consultationFee: Number(consultationFee),
        medicineCharges: Number(medicineCharges),
        otherCharges: Number(otherCharges),
      };

      const bill = await createBilling(body);
      setGeneratedBill(bill);
      setPaymentAmount(String(bill.dueAmount));
      setSuccess(`Bill generated successfully! Invoice #${bill.id}`);
    } catch (err: any) {
      setError("Failed to generate bill.");
    } finally {
      setLoading(false);
    }
  };

  // Step 3: Record Payment
  const handleStep3Submit = async () => {
    if (!generatedBill) {
      setError("Missing Bill context.");
      return;
    }
    try {
      setLoading(true);
      setError("");
      await createPayment(generatedBill.id, {
        amount: Number(paymentAmount),
        paymentMethod: paymentMethod,
      });
      setSuccess("Payment recorded successfully!");
      setTimeout(() => {
        setSuccess("");
        setOpen(false);
      }, 1500);
    } catch (err: any) {
      setError("Payment processing failed.");
    } finally {
      setLoading(false);
    }
  };

  // Print Invoice layout logic
  const handlePrintInvoice = () => {
    if (!generatedBill) return;
    const printWindow = window.open("", "_blank");
    if (!printWindow) return;
    printWindow.document.write(`
      <html>
        <head>
          <title>Invoice Bill #${generatedBill.id}</title>
          <style>
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 40px; color: #333; line-height: 1.5; }
            .header { text-align: center; margin-bottom: 40px; border-bottom: 2px solid #0D47A1; padding-bottom: 20px; }
            .header h1 { margin: 0; color: #0D47A1; font-size: 28px; }
            .header p { margin: 5px 0 0 0; color: #666; font-size: 14px; text-transform: uppercase; letter-spacing: 1px; }
            .info-sec { display: flex; justify-content: space-between; margin-bottom: 30px; }
            .info-col { width: 48%; }
            .info-col h3 { border-bottom: 1px solid #ddd; padding-bottom: 5px; color: #0D47A1; margin-bottom: 10px; }
            .info-col p { margin: 6px 0; }
            .table { width: 100%; border-collapse: collapse; margin-top: 30px; }
            .table th, .table td { border: 1px solid #ddd; padding: 12px 15px; text-align: left; }
            .table th { background-color: #0D47A1; color: white; }
            .total-row { font-weight: bold; background-color: #e3f2fd; font-size: 18px; }
            .footer { margin-top: 60px; text-align: center; font-size: 12px; color: #777; border-top: 1px solid #eee; padding-top: 20px; }
            @media print {
              body { margin: 0; }
            }
          </style>
        </head>
        <body>
          <div class="header">
            <h1>MULTICARE HMS</h1>
            <p>Official Patient Invoice Receipt</p>
          </div>
          <div class="info-sec">
            <div class="info-col">
              <h3>Patient Details</h3>
              <p><strong>Name:</strong> ${selectedPatient?.firstName} ${selectedPatient?.lastName}</p>
              <p><strong>Phone:</strong> ${selectedPatient?.phone || "N/A"}</p>
            </div>
            <div class="info-col" style="text-align: right;">
              <h3>Invoice Info</h3>
              <p><strong>Invoice ID:</strong> #${generatedBill.id}</p>
              <p><strong>Date:</strong> ${new Date().toLocaleDateString("en-IN")}</p>
              <p><strong>Status:</strong> ${generatedBill.status}</p>
            </div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th>Description</th>
                <th style="text-align: right;">Amount (INR)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Doctor Consultation Fee (${selectedDoctor ? `Dr. ${selectedDoctor.firstName} ${selectedDoctor.lastName}` : "General"})</td>
                <td style="text-align: right;">₹${Number(consultationFee).toFixed(2)}</td>
              </tr>
              <tr>
                <td>Pharmacy Medicine Charges</td>
                <td style="text-align: right;">₹${Number(medicineCharges).toFixed(2)}</td>
              </tr>
              <tr>
                <td>Other Miscellaneous Charges</td>
                <td style="text-align: right;">₹${Number(otherCharges).toFixed(2)}</td>
              </tr>
              <tr class="total-row">
                <td>Total Bill Amount</td>
                <td style="text-align: right;">₹${generatedBill.totalAmount.toFixed(2)}</td>
              </tr>
            </tbody>
          </table>
          <div class="footer">
            <p>Thank you for choosing Multicare HMS. This is a system-generated electronic receipt.</p>
          </div>
          <script>
            window.onload = function() {
              window.print();
              window.close();
            };
          </script>
        </body>
      </html>
    `);
    printWindow.document.close();
  };

  const handleDownloadTxt = (pres: Prescription) => {
    const pat = patients.find((p) => p.id === pres.patientId);
    const doc = doctors.find((d) => d.id === pres.doctorId);
    
    let txt = `MULTICARE HMS - PRESCRIPTION DETAILS\n`;
    txt += `====================================\n\n`;
    txt += `Prescription ID: #${pres.id}\n`;
    txt += `Date: ${new Date().toLocaleDateString("en-IN")}\n\n`;
    txt += `Patient Name: ${pat ? `${pat.firstName} ${pat.lastName}` : `ID: ${pres.patientId}`}\n`;
    txt += `Patient Phone: ${pat?.phone || "N/A"}\n\n`;
    txt += `Doctor Name: ${doc ? `Dr. ${doc.firstName} ${doc.lastName} (${doc.specialization})` : `ID: ${pres.doctorId}`}\n\n`;
    txt += `Prescribed Medicines:\n`;
    txt += `---------------------\n`;
    
    pres.items.forEach((item, idx) => {
      const med = medicines.find((m) => m.id === item.medicineId);
      txt += `${idx + 1}. ${med ? med.name : `Medicine ID: ${item.medicineId}`} - Dosage: ${item.dosage} for ${item.durationDays} days (Qty: ${item.quantity})\n`;
    });
    
    if (pres.notes) {
      txt += `\nNotes:\n${pres.notes}\n`;
    }
    
    const blob = new Blob([txt], { type: "text/plain;charset=utf-8" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `Prescription_${pres.id}.txt`;
    link.click();
    URL.revokeObjectURL(url);
  };

  const handlePrintPrescription = (pres: Prescription) => {
    const pat = patients.find((p) => p.id === pres.patientId);
    const doc = doctors.find((d) => d.id === pres.doctorId);
    
    const printWindow = window.open("", "_blank");
    if (!printWindow) return;
    
    const medicineRows = pres.items.map((item, idx) => {
      const med = medicines.find((m) => m.id === item.medicineId);
      return `
        <tr>
          <td>${idx + 1}</td>
          <td><strong>${med ? med.name : `Medicine ID: ${item.medicineId}`}</strong>${med?.manufacturer ? `<br/><small style="color:#666">${med.manufacturer}</small>` : ""}</td>
          <td>${item.dosage}</td>
          <td style="text-align: center;">${item.durationDays} Days</td>
          <td style="text-align: right;">${item.quantity}</td>
        </tr>
      `;
    }).join("");

    printWindow.document.write(`
      <html>
        <head>
          <title>Prescription #${pres.id}</title>
          <style>
            body { font-family: 'Segoe UI', sans-serif; margin: 40px; color: #333; line-height: 1.5; }
            .header { display: flex; justify-content: space-between; border-bottom: 2px solid #0D47A1; padding-bottom: 20px; margin-bottom: 30px; }
            .hospital-info h1 { margin: 0; color: #0D47A1; font-size: 28px; }
            .hospital-info p { margin: 5px 0 0 0; color: #666; font-size: 14px; text-transform: uppercase; }
            .doctor-info { text-align: right; }
            .doctor-info h2 { margin: 0; color: #333; font-size: 20px; }
            .doctor-info p { margin: 3px 0 0 0; color: #666; }
            .patient-sec { background-color: #f8f9fa; padding: 15px; border-radius: 6px; margin-bottom: 30px; display: flex; justify-content: space-between; }
            .patient-sec p { margin: 5px 0; }
            .rx-title { font-size: 24px; color: #0D47A1; font-weight: bold; margin-bottom: 20px; }
            .table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            .table th, .table td { border: 1px solid #ddd; padding: 12px 15px; text-align: left; }
            .table th { background-color: #0D47A1; color: white; }
            .notes-sec { margin-top: 40px; border-top: 1px solid #eee; padding-top: 20px; }
            .footer { margin-top: 80px; display: flex; justify-content: space-between; font-size: 12px; color: #777; border-top: 1px solid #eee; padding-top: 20px; }
            @media print {
              body { margin: 0; }
            }
          </style>
        </head>
        <body>
          <div class="header">
            <div class="hospital-info">
              <h1>MULTICARE HMS</h1>
              <p>Prescription Document</p>
            </div>
            <div class="doctor-info">
              <h2>${doc ? 'Dr. ' + doc.firstName + ' ' + doc.lastName : 'Doctor ID: ' + pres.doctorId}</h2>
              <p>${doc?.specialization || "General Medicine"}</p>
              <p>License: ${doc?.licenseNumber || "N/A"}</p>
            </div>
          </div>
          
          <div class="patient-sec">
            <div>
              <p><strong>Patient Name:</strong> ${pat ? pat.firstName + ' ' + pat.lastName : 'ID: ' + pres.patientId}</p>
              <p><strong>Mobile:</strong> ${pat?.phone || "N/A"}</p>
            </div>
            <div style="text-align: right;">
              <p><strong>Prescription ID:</strong> #${pres.id}</p>
              <p><strong>Date:</strong> ${new Date().toLocaleDateString("en-IN")}</p>
            </div>
          </div>
          
          <div class="rx-title">Rx</div>
          
          <table class="table">
            <thead>
              <tr>
                <th style="width: 50px;">#</th>
                <th>Medicine Name</th>
                <th>Dosage Instructions</th>
                <th style="text-align: center; width: 100px;">Duration</th>
                <th style="text-align: right; width: 80px;">Qty</th>
              </tr>
            </thead>
            <tbody>
              ${medicineRows}
            </tbody>
          </table>
          
          ${pres.notes ? `
            <div class="notes-sec">
              <h3>Notes / Directions:</h3>
              <p>${pres.notes}</p>
            </div>
          ` : ""}
          
          <div class="footer">
            <p>Generated electronically by Multicare HMS</p>
            <div style="text-align: right; width: 200px;">
              <div style="border-bottom: 1px solid #333; height: 40px;"></div>
              <p style="margin-top: 5px;">Doctor's Signature</p>
            </div>
          </div>
          
          <script>
            window.onload = function() {
              window.print();
              window.close();
            };
          </script>
        </body>
      </html>
    `);
    printWindow.document.close();
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this prescription?")) return;
    try {
      setError("");
      await deletePrescription(id);
      setPrescriptions((prev) => prev.filter((p) => p.id !== id));
      setSuccess("Prescription deleted successfully.");
      setTimeout(() => setSuccess(""), 3000);
    } catch (err: any) {
      setError("Failed to delete prescription.");
    }
  };

  // Quick Action: Process Bill for an existing prescription
  const handleQuickProcessBill = (pres: Prescription) => {
    const pat = patients.find((p) => p.id === pres.patientId) || null;
    const doc = doctors.find((d) => d.id === pres.doctorId) || null;
    setSelectedPatient(pat);
    setSelectedDoctor(doc);
    if (doc) {
      setDoctorSearchId(String(doc.id));
      setConsultationFee(String(doc.consultationFee ?? 0));
    }
    setMedicalRecordId(String(pres.medicalRecordId));
    setNotes(pres.notes || "");
    
    // Map items
    const items = pres.items.map((item) => {
      const med = medicines.find((m) => m.id === item.medicineId) || null;
      return {
        medicine: med,
        quantity: item.quantity,
        dosage: item.dosage,
        durationDays: item.durationDays,
      };
    });
    setPrescriptionItems(items);
    setOtherCharges("0");
    setGeneratedBill(null);
    setActiveStep(1); // Go straight to step 2!
    setOpen(true);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>
        <Typography variant="h4" sx={{ fontWeight: "bold" }}>
          {isPharmacist ? "Prescriptions Workspace" : "Prescriptions"}
        </Typography>
        {canWrite && (
          <Button variant="contained" startIcon={<AddIcon />} onClick={handleOpenWizard}>
            Add Prescription Wizard
          </Button>
        )}
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

      <Card sx={{ borderRadius: 3, boxShadow: "0 4px 12px rgba(0,0,0,0.05)" }}>
        <CardContent sx={{ p: 0 }}>
          <TableContainer>
            <Table>
              <TableHead sx={{ bgcolor: "grey.50" }}>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>ID</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Patient</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Doctor</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Medical Record</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Notes</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {prescriptions
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map((pres) => {
                    const pat = patients.find((p) => p.id === pres.patientId);
                    const doc = doctors.find((d) => d.id === pres.doctorId);
                    return (
                      <TableRow key={pres.id} hover>
                        <TableCell>#{pres.id}</TableCell>
                        <TableCell>
                          {pat ? `${pat.firstName} ${pat.lastName}` : `Patient ID: ${pres.patientId}`}
                          {pat?.phone && (
                            <Typography variant="caption" sx={{ display: "block" }} color="text.secondary">
                              {pat.phone}
                            </Typography>
                          )}
                        </TableCell>
                        <TableCell>
                          {doc ? `Dr. ${doc.firstName} ${doc.lastName}` : `Doctor ID: ${pres.doctorId}`}
                        </TableCell>
                        <TableCell>#{pres.medicalRecordId}</TableCell>
                        <TableCell>{pres.notes || "-"}</TableCell>
                        <TableCell align="right">
                          <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 1 }}>
                            <Button
                              size="small"
                              variant="outlined"
                              onClick={() => handleDownloadTxt(pres)}
                            >
                              TXT
                            </Button>
                            <Button
                              size="small"
                              variant="outlined"
                              startIcon={<PrintIcon />}
                              onClick={() => handlePrintPrescription(pres)}
                            >
                              Print PDF
                            </Button>
                            {isPharmacist && (
                              <Button
                                size="small"
                                variant="outlined"
                                startIcon={<ReceiptIcon />}
                                onClick={() => handleQuickProcessBill(pres)}
                              >
                                Process Bill
                              </Button>
                            )}
                            {canDelete && (
                              <IconButton color="error" size="small" onClick={() => void handleDelete(pres.id)}>
                                <DeleteIcon fontSize="small" />
                              </IconButton>
                            )}
                          </Box>
                        </TableCell>
                      </TableRow>
                    );
                  })}
                {prescriptions.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={6} align="center" sx={{ py: 3 }}>
                      {loading ? <CircularProgress size={24} /> : "No prescriptions found."}
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[5, 10, 25]}
            component="div"
            count={prescriptions.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={(_, newPage) => setPage(newPage)}
            onRowsPerPageChange={(event) => {
              setRowsPerPage(parseInt(event.target.value, 10));
              setPage(0);
            }}
          />
        </CardContent>
      </Card>

      {/* S3 File Workspaces */}
      <Grid container spacing={3} sx={{ mt: 3 }}>
        {isPharmacist && (
          <Grid size={{ xs: 12, md: 5 }}>
            <Card sx={{ borderRadius: 3, boxShadow: "0 4px 12px rgba(0,0,0,0.05)" }}>
              <CardContent sx={{ p: 3, display: "grid", gap: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: "bold" }}>
                  Upload PDF Prescription to S3
                </Typography>
                <Autocomplete
                  options={patients}
                  getOptionLabel={(option) => `${option.firstName} ${option.lastName} (${option.phone})`}
                  value={s3SelectedPatient}
                  onChange={(_, newValue) => setS3SelectedPatient(newValue)}
                  renderInput={(params) => <TextField {...params} label="Select Patient" required size="small" />}
                />
                <TextField
                  label="Document Name"
                  value={s3DocName}
                  onChange={(e) => setS3DocName(e.target.value)}
                  placeholder="e.g. Cardiologist Prescription"
                  required
                  size="small"
                />
                <Button
                  variant="outlined"
                  component="label"
                  startIcon={<UploadIcon />}
                  sx={{ py: 1 }}
                >
                  {s3File ? s3File.name : "Choose PDF File"}
                  <input
                    type="file"
                    accept="application/pdf"
                    hidden
                    onChange={(e) => setS3File(e.target.files?.[0] || null)}
                  />
                </Button>
                <Button
                  variant="contained"
                  onClick={() => void handleS3Upload()}
                  disabled={s3Uploading || !s3SelectedPatient || !s3File || !s3DocName.trim()}
                  startIcon={s3Uploading ? <CircularProgress size={20} color="inherit" /> : <UploadIcon />}
                >
                  {s3Uploading ? "Uploading..." : "Upload to S3"}
                </Button>
              </CardContent>
            </Card>
          </Grid>
        )}

        {(user?.role === "PATIENT" || s3Documents.length > 0) && (
          <Grid size={{ xs: 12, md: isPharmacist ? 7 : 12 }}>
            <Card sx={{ borderRadius: 3, boxShadow: "0 4px 12px rgba(0,0,0,0.05)" }}>
              <CardContent sx={{ p: 3 }}>
                <Typography variant="h6" sx={{ fontWeight: "bold", mb: 2 }}>
                  Digital Prescription PDF Documents (S3)
                </Typography>
                {s3Loading ? (
                  <Box sx={{ display: "flex", justifyContent: "center", py: 4 }}><CircularProgress /></Box>
                ) : s3Documents.length === 0 ? (
                  <Typography color="text.secondary">No digital prescription documents uploaded to S3.</Typography>
                ) : (
                  <Box sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                    {s3Documents.map((doc) => (
                      <Paper key={doc.id} variant="outlined" sx={{ p: 2, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <Box>
                          <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>{doc.documentName}</Typography>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>
                            Uploaded by {doc.uploadedBy} on {new Date(doc.timestamp).toLocaleString("en-IN")}
                          </Typography>
                        </Box>
                        <Button
                          variant="outlined"
                          size="small"
                          startIcon={<DownloadIcon />}
                          onClick={() => window.open(doc.downloadUrl, "_blank", "noopener,noreferrer")}
                        >
                          Download PDF
                        </Button>
                      </Paper>
                    ))}
                  </Box>
                )}
              </CardContent>
            </Card>
          </Grid>
        )}
      </Grid>

      {/* Stepper Wizard Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)} fullWidth maxWidth="md">
        <DialogTitle sx={{ fontWeight: "bold" }}>
          Prescription & Integrated Billing Wizard
        </DialogTitle>
        <DialogContent dividers>
          <Box sx={{ mb: 3 }}>
            <Stepper activeStep={activeStep}>
              <Step>
                <StepLabel>Upload Prescription</StepLabel>
              </Step>
              <Step>
                <StepLabel>Generate Bill</StepLabel>
              </Step>
              <Step>
                <StepLabel>Record Payment</StepLabel>
              </Step>
            </Stepper>
          </Box>

          {activeStep === 0 && (
            <Box sx={{ display: "grid", gap: 2.5 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>Step 1: Patient Search</Typography>
              
              {/* Split Patient Search inputs */}
              <Grid container spacing={2} sx={{ alignItems: "center" }}>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <TextField
                    label="Patient Name"
                    value={patientNameInput}
                    onChange={(e) => setPatientNameInput(e.target.value)}
                    fullWidth
                    helperText="First or last name"
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <TextField
                    label="Mobile Number"
                    value={patientMobileInput}
                    onChange={(e) => setPatientMobileInput(e.target.value)}
                    fullWidth
                    helperText="Patient phone number"
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 4 }}>
                  <Button
                    variant="contained"
                    fullWidth
                    onClick={handleSearchPatient}
                    sx={{ height: 56, mb: 2 }}
                  >
                    Search Patient
                  </Button>
                </Grid>
              </Grid>

              {patientSearchStatus === "SUCCESS" && selectedPatient && (
                <Alert severity="success" sx={{ borderRadius: 2 }}>
                  ✓ Patient Found: <strong>{selectedPatient.firstName} {selectedPatient.lastName}</strong> (ID: #{selectedPatient.id}, Mobile: {selectedPatient.phone})
                </Alert>
              )}
              {patientSearchStatus === "NOT_FOUND" && (
                <Alert severity="error" sx={{ borderRadius: 2 }}>
                  ✗ No patient found matching these details. Please verify name and mobile number.
                </Alert>
              )}
              {patientSearchStatus === "MULTIPLE" && (
                <Alert severity="warning" sx={{ borderRadius: 2 }}>
                  ⚠ Multiple matching patients found. Please refine name or mobile number.
                </Alert>
              )}

              {/* Show other details only after a patient is found and selected */}
              {selectedPatient && (
                <>
                  <Divider sx={{ my: 1 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>Step 1.2: Prescription Details</Typography>
                  <Grid container spacing={2}>
                    <Grid size={{ xs: 12, sm: 4 }}>
                      {/* Doctor ID lookup field */}
                      <TextField
                        label="Doctor ID Lookup"
                        type="number"
                        value={doctorSearchId}
                        onChange={(e) => handleDoctorSearchChange(e.target.value)}
                        fullWidth
                        helperText={selectedDoctor ? `Dr. ${selectedDoctor.firstName} ${selectedDoctor.lastName}` : "Type Doctor ID"}
                      />
                    </Grid>
                    
                    <Grid size={{ xs: 12, sm: 8 }}>
                      {/* Doctor Autocomplete */}
                      <Autocomplete
                        options={doctors}
                        getOptionLabel={(option) => `Dr. ${option.firstName} ${option.lastName} (${option.specialization})`}
                        value={selectedDoctor}
                        onChange={(_, newVal) => handleDoctorAutocompleteChange(newVal)}
                        renderInput={(params) => <TextField {...params} label="Doctor Selection" />}
                      />
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                      <TextField
                        label="Medical Record ID"
                        type="number"
                        value={medicalRecordId}
                        onChange={(e) => setMedicalRecordId(e.target.value)}
                        fullWidth
                      />
                    </Grid>

                    <Grid size={{ xs: 12, sm: 6 }}>
                      <TextField
                        label="Notes"
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                        fullWidth
                      />
                    </Grid>
                  </Grid>

                  <Divider sx={{ my: 1 }} />
                  
                  <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>Medicine Items</Typography>
                    <Button variant="outlined" startIcon={<AddIcon />} onClick={handleAddItem} size="small">
                      Add Medicine
                    </Button>
                  </Box>

                  {prescriptionItems.map((item, idx) => (
                    <Paper key={idx} variant="outlined" sx={{ p: 2, display: "grid", gap: 2, position: "relative" }}>
                      <Grid container spacing={2} sx={{ alignItems: "center" }}>
                        <Grid size={{ xs: 12, sm: 4 }}>
                          <Autocomplete
                            options={medicines}
                            getOptionLabel={(option) => `${option.name} (${option.manufacturer || "Generic"})`}
                            value={item.medicine}
                            onChange={(_, newVal) => handleItemChange(idx, "medicine", newVal)}
                            renderInput={(params) => <TextField {...params} label="Select Medicine" size="small" />}
                          />
                        </Grid>
                        <Grid size={{ xs: 6, sm: 2 }}>
                          <TextField
                            label="Qty"
                            type="number"
                            size="small"
                            value={item.quantity}
                            onChange={(e) => handleItemChange(idx, "quantity", Number(e.target.value))}
                            fullWidth
                            helperText={item.medicine ? `Stock: ${item.medicine.stockQuantity}` : ""}
                          />
                        </Grid>
                        <Grid size={{ xs: 6, sm: 3 }}>
                          <TextField
                            label="Dosage"
                            size="small"
                            value={item.dosage}
                            onChange={(e) => handleItemChange(idx, "dosage", e.target.value)}
                            fullWidth
                          />
                        </Grid>
                        <Grid size={{ xs: 6, sm: 2 }}>
                          <TextField
                            label="Days"
                            type="number"
                            size="small"
                            value={item.durationDays}
                            onChange={(e) => handleItemChange(idx, "durationDays", Number(e.target.value))}
                            fullWidth
                          />
                        </Grid>
                        <Grid size={{ xs: 6, sm: 1 }} sx={{ textAlign: "right" }}>
                          <IconButton color="error" onClick={() => handleRemoveItem(idx)}>
                            <DeleteIcon />
                          </IconButton>
                        </Grid>
                      </Grid>
                      {item.medicine && (
                        <Typography variant="caption" color="text.secondary">
                          Price per unit: ₹{item.medicine.unitPrice} | Total: ₹{(item.medicine.unitPrice * item.quantity).toFixed(2)}
                        </Typography>
                      )}
                    </Paper>
                  ))}
                </>
              )}
            </Box>
          )}

          {activeStep === 1 && (
            <Box sx={{ display: "grid", gap: 2.5 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>Step 2: Generate Integrated Bill</Typography>
              
              {selectedPatient && (
                <Box sx={{ p: 2, bgcolor: "grey.50", borderRadius: 2 }}>
                  <Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>Billing For:</Typography>
                  <Typography variant="body2">{selectedPatient.firstName} {selectedPatient.lastName}</Typography>
                  <Typography variant="body2" color="text.secondary">{selectedPatient.phone}</Typography>
                </Box>
              )}

              {/* Conditional rendering for Bill Inputs vs Invoice Receipt Preview */}
              {!generatedBill ? (
                <>
                  <Grid container spacing={2}>
                    <Grid size={{ xs: 12, sm: 4 }}>
                      <TextField
                        label="Doctor Consultation Fee"
                        type="number"
                        value={consultationFee}
                        onChange={(e) => setConsultationFee(e.target.value)}
                        fullWidth
                        helperText={selectedDoctor ? `Consultation fee for Dr. ${selectedDoctor.firstName}` : ""}
                      />
                    </Grid>
                    <Grid size={{ xs: 12, sm: 4 }}>
                      <TextField
                        label="Medicine Charges"
                        type="number"
                        value={medicineCharges}
                        onChange={(e) => setMedicineCharges(e.target.value)}
                        fullWidth
                        helperText="Auto-calculated from prescription selection"
                      />
                    </Grid>
                    <Grid size={{ xs: 12, sm: 4 }}>
                      <TextField
                        label="Other Charges"
                        type="number"
                        value={otherCharges}
                        onChange={(e) => setOtherCharges(e.target.value)}
                        fullWidth
                      />
                    </Grid>
                  </Grid>

                  <Divider sx={{ my: 1 }} />

                  <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", p: 2, bgcolor: "primary.50", borderRadius: 2 }}>
                    <Typography variant="subtitle1" sx={{ fontWeight: "bold", color: "primary.900" }}>Total Amount Due</Typography>
                    <Typography variant="h5" sx={{ fontWeight: "bold", color: "primary.900" }}>
                      ₹{(Number(consultationFee) + Number(medicineCharges) + Number(otherCharges)).toLocaleString("en-IN", { minimumFractionDigits: 2 })}
                    </Typography>
                  </Box>
                </>
              ) : (
                /* INVOICE BILL PREVIEW CARD */
                <Paper variant="outlined" sx={{ p: 3, border: "2px solid #0D47A1", borderRadius: 3, bgcolor: "background.paper" }}>
                  <Box sx={{ textAlign: "center", mb: 3 }}>
                    <Typography variant="h5" color="primary" sx={{ fontWeight: "bold", letterSpacing: 1 }}>
                      INVOICE RECEIPT
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      MULTICARE HOSPITAL MANAGEMENT SYSTEM
                    </Typography>
                  </Box>
                  
                  <Divider sx={{ mb: 2 }} />

                  <Grid container spacing={2} sx={{ mb: 2 }}>
                    <Grid size={{ xs: 6 }}>
                      <Typography variant="caption" color="text.secondary">PATIENT DETAILS</Typography>
                      <Typography variant="body2" sx={{ fontWeight: "bold" }}>
                        {selectedPatient?.firstName} {selectedPatient?.lastName}
                      </Typography>
                      <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>
                        Phone: {selectedPatient?.phone || "N/A"}
                      </Typography>
                    </Grid>
                    <Grid size={{ xs: 6 }} sx={{ textAlign: "right" }}>
                      <Typography variant="caption" color="text.secondary">INVOICE INFO</Typography>
                      <Typography variant="body2" sx={{ fontWeight: "bold" }}>
                        Invoice ID: #{generatedBill.id}
                      </Typography>
                      <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>
                        Date: {new Date().toLocaleDateString("en-IN")}
                      </Typography>
                    </Grid>
                  </Grid>

                  <Divider sx={{ mb: 2 }} />

                  <TableContainer component={Paper} variant="outlined" sx={{ mb: 2, borderRadius: 2 }}>
                    <Table size="small">
                      <TableHead sx={{ bgcolor: "grey.100" }}>
                        <TableRow>
                          <TableCell sx={{ fontWeight: "bold" }}>Description</TableCell>
                          <TableCell align="right" sx={{ fontWeight: "bold" }}>Amount (₹)</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        <TableRow>
                          <TableCell>Doctor Consultation Fee</TableCell>
                          <TableCell align="right">₹{Number(consultationFee).toFixed(2)}</TableCell>
                        </TableRow>
                        <TableRow>
                          <TableCell>Pharmacy Medicine Charges</TableCell>
                          <TableCell align="right">₹{Number(medicineCharges).toFixed(2)}</TableCell>
                        </TableRow>
                        <TableRow>
                          <TableCell>Other Charges</TableCell>
                          <TableCell align="right">₹{Number(otherCharges).toFixed(2)}</TableCell>
                        </TableRow>
                        <TableRow sx={{ bgcolor: "primary.50" }}>
                          <TableCell sx={{ fontWeight: "bold" }}>Total Amount</TableCell>
                          <TableCell align="right" sx={{ fontWeight: "bold", color: "primary.950" }}>
                            ₹{generatedBill.totalAmount.toFixed(2)}
                          </TableCell>
                        </TableRow>
                      </TableBody>
                    </Table>
                  </TableContainer>

                  <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 2, mt: 2 }}>
                    <Button
                      variant="contained"
                      color="secondary"
                      startIcon={<PrintIcon />}
                      onClick={handlePrintInvoice}
                    >
                      Print Invoice Bill
                    </Button>
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => setActiveStep(2)}
                    >
                      Proceed to Payment
                    </Button>
                  </Box>
                </Paper>
              )}
            </Box>
          )}

          {activeStep === 2 && (
            <Box sx={{ display: "grid", gap: 2.5 }}>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>Step 3: Record Payment</Typography>
              
              {generatedBill && (
                <Box sx={{ p: 2, bgcolor: "success.50", borderRadius: 2, border: "1px solid", borderColor: "success.200" }}>
                  <Typography variant="subtitle2" sx={{ fontWeight: "bold", color: "success.900" }}>Bill Invoice Selected</Typography>
                  <Typography variant="body2" color="success.900">Invoice ID: #{generatedBill.id}</Typography>
                  <Typography variant="body2" color="success.900">Total Bill Amount: ₹{generatedBill.totalAmount}</Typography>
                  <Typography variant="body2" color="success.900">Remaining Balance: ₹{generatedBill.dueAmount}</Typography>
                </Box>
              )}

              <Grid container spacing={2}>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <Autocomplete
                    options={["CASH", "CARD", "UPI", "INSURANCE"] as const}
                    value={paymentMethod}
                    onChange={(_, newVal) => { if (newVal) setPaymentMethod(newVal); }}
                    renderInput={(params) => <TextField {...params} label="Payment Method" required />}
                  />
                </Grid>
                <Grid size={{ xs: 12, sm: 6 }}>
                  <TextField
                    label="Payment Amount"
                    type="number"
                    value={paymentAmount}
                    onChange={(e) => setPaymentAmount(e.target.value)}
                    fullWidth
                    required
                  />
                </Grid>
              </Grid>
            </Box>
          )}
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setOpen(false)} color="inherit">
            Cancel
          </Button>
          <Box sx={{ flexGrow: 1 }} />
          {activeStep === 0 && (
            <Button variant="contained" onClick={handleStep1Submit} disabled={loading} startIcon={loading ? <CircularProgress size={16} /> : <ReceiptIcon />}>
              Submit & Generate Bill
            </Button>
          )}
          {activeStep === 1 && !generatedBill && (
            <Button variant="contained" onClick={handleStep2Submit} disabled={loading} startIcon={loading ? <CircularProgress size={16} /> : <ReceiptIcon />}>
              Generate Bill
            </Button>
          )}
          {activeStep === 2 && (
            <Button variant="contained" onClick={handleStep3Submit} color="success" disabled={loading} startIcon={loading ? <CircularProgress size={16} /> : <PaymentIcon />}>
              Record Payment
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </Box>
  );
}
