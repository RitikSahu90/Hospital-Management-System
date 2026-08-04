import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import {
  Alert,
  Autocomplete,
  Avatar,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Chip,
  Divider,
  Grid,
  IconButton,
  InputAdornment,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import { motion, AnimatePresence } from "framer-motion";
import {
  DownloadOutlined as DownloadIcon,
  UploadFileOutlined,
  Search as SearchIcon,
  Close as CloseIcon,
  InsertDriveFile as FileIcon,
  PictureAsPdf as PdfIcon,
  CheckCircle as CheckIcon,
  ContactPage as ContactIcon,
} from "@mui/icons-material";
import { getPatientReportDownloadUrl, getPatientReports, uploadPatientReport } from "../services/reportService";
import { getPatients } from "../services/patientService";
import type { Patient } from "../types/patient";
import type { PatientReport } from "../services/reportService";

const calculateAge = (dobString?: string): string => {
  if (!dobString) return "N/A";
  const dob = new Date(dobString);
  const diffMs = Date.now() - dob.getTime();
  const ageDate = new Date(diffMs);
  return Math.abs(ageDate.getUTCFullYear() - 1970).toString();
};

export default function Laboratory() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
  const [title, setTitle] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [reports, setReports] = useState<PatientReport[]>([]);
  const [loadingPatients, setLoadingPatients] = useState(false);
  const [loadingReports, setLoadingReports] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [reportSearch, setReportSearch] = useState("");
  const [downloadingReportId, setDownloadingReportId] = useState<number | null>(null);
  const [isDragOver, setIsDragOver] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const fetchPatientsList = async () => {
      try {
        setLoadingPatients(true);
        setError("");
        const data = await getPatients();
        setPatients(data);
      } catch (err) {
        console.error(err);
        setError("Unable to load patient records. Please verify API service connectivity.");
      } finally {
        setLoadingPatients(false);
      }
    };
    void fetchPatientsList();
  }, []);

  const handlePatientChange = useCallback(async (patient: Patient | null) => {
    setSelectedPatient(patient);
    setReports([]);
    setReportSearch("");
    setTitle("");
    setFile(null);
    setSuccess("");
    setError("");
    setDownloadingReportId(null);

    if (!patient) return;

    try {
      setLoadingReports(true);
      const data = await getPatientReports(patient.id);
      setReports(data);
    } catch {
      setError("Unable to load laboratory reports for this patient.");
    } finally {
      setLoadingReports(false);
    }
  }, []);

  const handleUpload = useCallback(async () => {
    if (!selectedPatient) return;
    if (!title.trim() || !file) {
      setError("Please specify a report title and choose a file to upload.");
      return;
    }

    try {
      setUploading(true);
      setError("");
      setSuccess("");
      const report = await uploadPatientReport(selectedPatient.id, title.trim(), file);
      setReports((current) => [report, ...current]);
      setSuccess(`Report "${title}" uploaded successfully!`);
      setTitle("");
      setFile(null);
    } catch {
      setError("Unable to upload report. Check file requirements (PDF/Image, max 10MB).");
    } finally {
      setUploading(false);
    }
  }, [selectedPatient, title, file]);

  const handleDownload = useCallback(async (report: PatientReport) => {
    try {
      setError("");
      setDownloadingReportId(report.id);
      const url = await getPatientReportDownloadUrl(report.patientId, report.id);
      window.open(url, "_blank", "noopener,noreferrer");
    } catch {
      setError("Unable to retrieve report download link from S3 storage.");
    } finally {
      setDownloadingReportId(null);
    }
  }, []);

  const filteredReports = useMemo(() => {
    const keyword = reportSearch.toLowerCase();
    return reports.filter((r) => r.title.toLowerCase().includes(keyword));
  }, [reports, reportSearch]);

  const reportCount = useMemo(() => reports.length, [reports]);
  const latestReportDate = useMemo(() => {
    if (reports.length === 0) return null;
    return new Date(reports[0].createdAt).toLocaleDateString("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  }, [reports]);

  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragOver(true);
  }, []);

  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragOver(false);
  }, []);

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    setIsDragOver(false);
    const droppedFile = e.dataTransfer.files?.[0];
    if (droppedFile) {
      const isValidType = droppedFile.type === "application/pdf" || droppedFile.type === "image/jpeg" || droppedFile.type === "image/png";
      if (isValidType) {
        setFile(droppedFile);
        setError("");
      } else {
        setError("Invalid file type. Please upload PDF, JPEG, or PNG files only.");
      }
    }
  }, []);

  const handleFileChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.target.files?.[0] ?? null;
    setFile(selected);
    setError("");
  }, []);

  const clearFile = useCallback(() => {
    setFile(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  }, []);

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 800, color: "primary.dark", mb: 1 }}>
          Laboratory & Diagnostics
        </Typography>
        <Typography color="text.secondary">
          Select a patient profile to review diagnostic history, upload report scan files, and retrieve stored records.
        </Typography>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 3, borderRadius: 3 }}>
          {error}
        </Alert>
      )}

      {success && (
        <Alert severity="success" sx={{ mb: 3, borderRadius: 3 }} icon={<CheckIcon />}>
          {success}
        </Alert>
      )}

      {selectedPatient && (
        <Grid container spacing={2} sx={{ mb: 3 }}>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Card sx={{ borderRadius: 3, bgcolor: "primary.50", border: "1px solid", borderColor: "primary.light" }}>
              <CardContent sx={{ p: 2, display: "flex", alignItems: "center", gap: 2 }}>
                <Avatar sx={{ bgcolor: "primary.main", width: 44, height: 44, fontWeight: 700, fontSize: 16 }}>
                  {selectedPatient.firstName[0]}
                  {selectedPatient.lastName[0]}
                </Avatar>
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    Total Reports
                  </Typography>
                  <Typography variant="h5" sx={{ fontWeight: 800, color: "primary.dark", lineHeight: 1.2 }}>
                    {reportCount}
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Card sx={{ borderRadius: 3, bgcolor: "primary.50", border: "1px solid", borderColor: "primary.light" }}>
              <CardContent sx={{ p: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Latest Report
                </Typography>
                <Typography variant="h6" sx={{ fontWeight: 700, color: "primary.dark" }}>
                  {latestReportDate ?? "N/A"}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
          <Grid size={{ xs: 12, sm: 4 }}>
            <Card sx={{ borderRadius: 3, bgcolor: "primary.50", border: "1px solid", borderColor: "primary.light" }}>
              <CardContent sx={{ p: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Patient ID
                </Typography>
                <Typography variant="h6" sx={{ fontWeight: 700, color: "primary.dark" }}>
                  {selectedPatient.patientNumber}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      )}

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 5 }}>
          <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
            <Card sx={{ borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.06)" }}>
              <CardContent sx={{ p: 3 }}>
                <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                  Search Patient
                </Typography>
                <Autocomplete
                  options={patients}
                  loading={loadingPatients}
                  getOptionLabel={(option) =>
                    `${option.firstName} ${option.lastName} (${option.patientNumber})`
                  }
                  onChange={(_, val) => void handlePatientChange(val)}
                  value={selectedPatient}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      label="Select or Type Patient Name/Number"
                      variant="outlined"
                      slotProps={{
                        input: {
                          startAdornment: (
                            <InputAdornment position="start">
                              <SearchIcon color="action" />
                            </InputAdornment>
                          ),
                          endAdornment: (
                            <>
                              {loadingPatients ? <CircularProgress color="inherit" size={20} /> : null}
                              {params.slotProps?.input?.endAdornment}
                            </>
                          ),
                        },
                      }}
                    />
                  )}
                />
              </CardContent>
            </Card>

            <AnimatePresence mode="wait">
              {selectedPatient && (
                <Box
                  component={motion.div}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: 10 }}
                  transition={{ duration: 0.2 }}
                >
                  <Card
                    sx={{
                      borderRadius: 4,
                      boxShadow: "0 4px 20px rgba(0,0,0,0.06)",
                      border: "1px solid",
                      borderColor: "primary.light",
                      bgcolor: "primary.50",
                    }}
                  >
                    <CardContent sx={{ p: 3 }}>
                      <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 2 }}>
                        <Avatar
                          sx={{
                            bgcolor: "primary.main",
                            width: 56,
                            height: 56,
                            fontWeight: 700,
                          }}
                        >
                          {selectedPatient.firstName[0]}
                          {selectedPatient.lastName[0]}
                        </Avatar>
                        <Box>
                          <Typography variant="h6" sx={{ fontWeight: 800 }}>
                            {selectedPatient.firstName} {selectedPatient.lastName}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            ID: {selectedPatient.patientNumber}
                          </Typography>
                        </Box>
                      </Box>
                      <Divider sx={{ my: 1.5 }} />
                       <Grid container spacing={2}>
                        <Grid size={{ xs: 6 }}>
                          <Typography variant="caption" color="text.secondary">
                            Age / Gender:
                          </Typography>
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {calculateAge(selectedPatient.dateOfBirth)} yrs / {selectedPatient.gender}
                          </Typography>
                        </Grid>
                        <Grid size={{ xs: 6 }}>
                          <Typography variant="caption" color="text.secondary">
                            Blood Group:
                          </Typography>
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {selectedPatient.bloodGroup || "Not specified"}
                          </Typography>
                        </Grid>
                        <Grid size={{ xs: 12 }}>
                          <Typography variant="caption" color="text.secondary">
                            Contact Phone:
                          </Typography>
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            {selectedPatient.phone}
                          </Typography>
                        </Grid>
                      </Grid>
                    </CardContent>
                  </Card>
                </Box>
              )}
            </AnimatePresence>

            <AnimatePresence>
              {selectedPatient && (
                <Box
                  component={motion.div}
                  initial={{ opacity: 0, scale: 0.95 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0.95 }}
                  transition={{ duration: 0.2 }}
                >
                  <Card sx={{ borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.06)" }}>
                    <CardContent sx={{ p: 3 }}>
                      <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                        Upload Diagnostics Report
                      </Typography>

                      <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5 }}>
                        <TextField
                          label="Report Title (e.g. Blood Panel, Chest X-Ray)"
                          value={title}
                          onChange={(e) => setTitle(e.target.value)}
                          fullWidth
                          variant="outlined"
                        />

                        <Paper
                          variant="outlined"
                          sx={{
                            p: 3,
                            textAlign: "center",
                            borderStyle: "dashed",
                            borderWidth: 2,
                            borderColor: isDragOver ? "primary.main" : file ? "primary.main" : "divider",
                            bgcolor: isDragOver ? "primary.50" : file ? "action.hover" : "background.paper",
                            cursor: "pointer",
                            transition: "0.2s",
                            "&:hover": {
                              borderColor: "primary.main",
                              bgcolor: "action.hover",
                            },
                          }}
                          onDragOver={handleDragOver}
                          onDragLeave={handleDragLeave}
                          onDrop={handleDrop}
                          component="label"
                        >
                          <input
                            ref={fileInputRef}
                            hidden
                            type="file"
                            accept="application/pdf,image/jpeg,image/png"
                            onChange={handleFileChange}
                          />
                          {file ? (
                            <Box sx={{ display: "flex", alignItems: "center", justifyContent: "center", gap: 1 }}>
                              {file.name.endsWith(".pdf") ? (
                                <PdfIcon color="error" />
                              ) : (
                                <FileIcon color="primary" />
                              )}
                              <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                {file.name}
                              </Typography>
                              <IconButton
                                size="small"
                                onClick={(e) => {
                                  e.preventDefault();
                                  clearFile();
                                }}
                              >
                                <CloseIcon fontSize="small" />
                              </IconButton>
                            </Box>
                          ) : (
                            <Box>
                              <UploadFileOutlined sx={{ fontSize: 36, color: "text.secondary", mb: 1 }} />
                              <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                Drag & Drop or Choose File
                              </Typography>
                              <Typography variant="caption" color="text.secondary">
                                PDF, JPEG, or PNG (Max size: 10MB)
                              </Typography>
                            </Box>
                          )}
                        </Paper>

                        <Button
                          variant="contained"
                          fullWidth
                          size="large"
                          onClick={() => void handleUpload()}
                          disabled={uploading || !title.trim() || !file}
                          sx={{ py: 1.5, borderRadius: 2 }}
                        >
                          {uploading ? <CircularProgress size={24} color="inherit" /> : "Upload Report"}
                        </Button>
                      </Box>
                    </CardContent>
                  </Card>
                </Box>
              )}
            </AnimatePresence>
          </Box>
        </Grid>

        <Grid size={{ xs: 12, md: 7 }}>
          <Card sx={{ borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.06)", height: "100%" }}>
            <CardContent sx={{ p: 3, display: "flex", flexDirection: "column", height: "100%" }}>
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  flexWrap: "wrap",
                  gap: 1.5,
                  mb: 3,
                }}
              >
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                  Diagnostic Records History
                </Typography>

                {selectedPatient && reportCount > 0 && (
                  <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                    <Chip label={`${reportCount} report${reportCount !== 1 ? "s" : ""}`} size="small" color="primary" variant="outlined" />
                    <TextField
                      placeholder="Filter records..."
                      size="small"
                      value={reportSearch}
                      onChange={(e) => setReportSearch(e.target.value)}
                       slotProps={{
                        input: {
                          startAdornment: (
                            <InputAdornment position="start">
                              <SearchIcon fontSize="small" color="action" />
                            </InputAdornment>
                          ),
                        },
                      }}
                      sx={{ width: 200 }}
                    />
                  </Box>
                )}
              </Box>

              <Box sx={{ flexGrow: 1, minHeight: 300 }}>
                {loadingReports ? (
                  <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: 300 }}>
                    <CircularProgress />
                  </Box>
                ) : !selectedPatient ? (
                  <Box
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "center",
                      alignItems: "center",
                      height: 350,
                      color: "text.secondary",
                      textAlign: "center",
                      p: 3,
                    }}
                  >
                    <ContactIcon sx={{ fontSize: 64, color: "action.disabled", mb: 2 }} />
                    <Typography variant="subtitle1" sx={{ fontWeight: 600, mb: 0.5 }}>
                      No Patient Selected
                    </Typography>
                    <Typography variant="body2">
                      Please select a patient from the dropdown list on the left to display or manage their clinical files.
                    </Typography>
                  </Box>
                ) : filteredReports.length === 0 ? (
                  <Box
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "center",
                      alignItems: "center",
                      height: 300,
                      color: "text.secondary",
                      textAlign: "center",
                      p: 3,
                    }}
                  >
                    <FileIcon sx={{ fontSize: 48, color: "action.disabled", mb: 1.5 }} />
                    <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                      No Records Found
                    </Typography>
                    <Typography variant="body2">
                      {reports.length === 0
                        ? "This patient has no laboratory reports on file."
                        : "No reports match your current filter search keyword."}
                    </Typography>
                  </Box>
                ) : (
                  <List sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                    <AnimatePresence>
                      {filteredReports.map((report) => (
                        <ListItem
                          key={report.id}
                          component={motion.div}
                          layout
                          initial={{ opacity: 0, y: 10 }}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: -10 }}
                          sx={{
                            p: 2,
                            borderRadius: 3,
                            border: "1px solid",
                            borderColor: "divider",
                            bgcolor: "background.paper",
                            transition: "box-shadow 0.2s",
                            "&:hover": {
                              boxShadow: "0 4px 12px rgba(0,0,0,0.05)",
                            },
                          }}
                        >
                          <ListItemIcon sx={{ minWidth: 44 }}>
                            <Avatar sx={{ bgcolor: "error.light", color: "error.contrastText" }}>
                              <PdfIcon />
                            </Avatar>
                          </ListItemIcon>
                          <ListItemText
                            primary={report.title}
                            secondary={`Uploaded: ${new Date(report.createdAt).toLocaleDateString("en-IN", {
                              day: "2-digit",
                              month: "short",
                              year: "numeric",
                              hour: "2-digit",
                              minute: "2-digit",
                            })}`}
                            slotProps={{
                              primary: { sx: { fontWeight: 700, color: "text.primary" } },
                              secondary: { sx: { fontSize: 12 } },
                            }}
                          />
                          <Button
                            variant="outlined"
                            size="small"
                            startIcon={downloadingReportId === report.id ? <CircularProgress size={16} color="inherit" /> : <DownloadIcon />}
                            onClick={() => void handleDownload(report)}
                            disabled={downloadingReportId === report.id}
                            sx={{ borderRadius: 2 }}
                          >
                            {downloadingReportId === report.id ? "Downloading..." : "Download"}
                          </Button>
                        </ListItem>
                      ))}
                    </AnimatePresence>
                  </List>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
}