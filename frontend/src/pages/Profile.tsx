import { useEffect, useState } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Avatar,
  Divider,
  Chip,
  CircularProgress,
  Alert,
  Paper,
} from "@mui/material";
import { motion } from "framer-motion";
import { useAuth } from "../contexts/AuthContext";
import { getPatients } from "../services/patientService";
import { getDoctors } from "../services/doctorService";
import { getDepartments } from "../services/departmentService";
import type { Patient } from "../types/patient";
import type { Doctor } from "../types/clinical";

import AccountBoxIcon from "@mui/icons-material/AccountBox";
import EmailIcon from "@mui/icons-material/Email";
import PhoneIcon from "@mui/icons-material/Phone";
import HomeIcon from "@mui/icons-material/Home";
import DateRangeIcon from "@mui/icons-material/DateRange";
import BadgeIcon from "@mui/icons-material/Badge";
import SecurityIcon from "@mui/icons-material/Security";
import MedicalServicesIcon from "@mui/icons-material/MedicalServices";

const ROLE_COLORS: Record<string, "primary" | "secondary" | "success" | "warning" | "error" | "info"> = {
  ADMIN: "error",
  DOCTOR: "primary",
  RECEPTIONIST: "success",
  PHARMACIST: "warning",
  PATIENT: "info",
};


export default function Profile() {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  
  // Profile details
  const [patientData, setPatientData] = useState<Patient | null>(null);
  const [doctorData, setDoctorData] = useState<Doctor | null>(null);
  const [departmentName, setDepartmentName] = useState("General Clinical");

  useEffect(() => {
    const loadProfileData = async () => {
      if (!user) return;
      try {
        setLoading(true);
        setError("");
        
        if (user.role === "PATIENT") {
          const patients = await getPatients();
          if (patients && patients.length > 0) {
            setPatientData(patients[0]);
          } else {
            setError("No patient profile was found for your account.");
          }
        } else if (user.role === "DOCTOR") {
          const doctors = await getDoctors();
          const doc = doctors.find(
            (d) => d.firstName.toLowerCase() === user.username.toLowerCase() || d.phone === "9876543211"
          ) || doctors[0];

          if (doc) {
            setDoctorData(doc);
            try {
              const depts = await getDepartments();
              const matchedDept = depts.find((dept) => dept.id === doc.departmentId);
              if (matchedDept) {
                setDepartmentName(matchedDept.name);
              }
            } catch {
              // Ignore department load failures, fallback is safe
            }
          } else {
            setError("No doctor profile was found for your account.");
          }
        }
      } catch (err) {
        console.error(err);
        setError("Failed to fetch profile information.");
      } finally {
        setLoading(false);
      }
    };

    void loadProfileData();
  }, [user]);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
        <CircularProgress size={50} />
      </Box>
    );
  }

  const roleColor = user ? ROLE_COLORS[user.role] || "primary" : "primary";

  // Calculate names and fallback details
  let displayName = user ? user.username.toUpperCase() : "User Account";
  let displayEmail = user ? `${user.username.toLowerCase()}@example.com` : "user@example.com";
  let displayPhone = "N/A";
  let displayAddress = "N/A";

  if (patientData) {
    displayName = `${patientData.firstName} ${patientData.lastName}`;
    displayEmail = patientData.email || displayEmail;
    displayPhone = patientData.phone || displayPhone;
    displayAddress = patientData.address || displayAddress;
  } else if (doctorData) {
    displayName = `Dr. ${doctorData.firstName} ${doctorData.lastName}`;
    displayEmail = `${doctorData.firstName.toLowerCase()}.${doctorData.lastName.toLowerCase()}@multicare.com`;
    displayPhone = doctorData.phone || displayPhone;
    displayAddress = "Multicare HMS Consulting Room " + doctorData.doctorCode;
  }

  const cardVariants = {
    hidden: { opacity: 0, y: 30 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.5 } }
  };

  return (
    <Box sx={{ p: 4, maxWidth: 1200, mx: "auto" }}>
      <Typography variant="h4" sx={{ fontWeight: 800, mb: 4, color: "primary.dark" }}>
        Account Profile
      </Typography>

      {error && <Alert severity="warning" sx={{ mb: 3 }}>{error}</Alert>}

      <Grid container spacing={4}>
        {/* Left Column: User Summary Card */}
        <Grid size={{ xs: 12, md: 4 }}>
          <motion.div initial="hidden" animate="visible" variants={cardVariants}>
            <Card
              sx={{
                borderRadius: 5,
                boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
                overflow: "hidden",
                textAlign: "center",
                bgcolor: "background.paper",
                border: "1px solid rgba(0,0,0,0.04)"
              }}
            >
              <Box
                sx={{
                  height: 120,
                  background: "linear-gradient(135deg, #1565C0 0%, #0D47A1 100%)",
                  position: "relative"
                }}
              />
              <CardContent sx={{ pt: 0, pb: 4, px: 3, position: "relative" }}>
                <Avatar
                  sx={{
                    width: 110,
                    height: 110,
                    mx: "auto",
                    mt: -7,
                    border: "5px solid white",
                    boxShadow: "0 4px 15px rgba(0,0,0,0.15)",
                    bgcolor: `${roleColor}.main`,
                    fontSize: "2.5rem",
                    fontWeight: 700
                  }}
                >
                  {displayName.substring(0, 2).toUpperCase()}
                </Avatar>

                <Typography variant="h5" sx={{ fontWeight: 700, mt: 2, color: "text.primary" }}>
                  {displayName}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  @{user?.username}
                </Typography>

                <Chip
                  label={user?.role}
                  color={roleColor}
                  sx={{
                    fontWeight: 700,
                    fontSize: "0.85rem",
                    px: 1,
                    textTransform: "uppercase",
                    letterSpacing: "1px"
                  }}
                />

                <Divider sx={{ my: 3 }} />

                <Box sx={{ display: "flex", flexDirection: "column", gap: 2, textAlign: "left" }}>
                  <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                    <EmailIcon color="action" fontSize="small" />
                    <Box>
                      <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Email Address</Typography>
                      <Typography variant="body2" sx={{ fontWeight: 500 }}>{displayEmail}</Typography>
                    </Box>
                  </Box>

                  <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                    <PhoneIcon color="action" fontSize="small" />
                    <Box>
                      <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Phone Number</Typography>
                      <Typography variant="body2" sx={{ fontWeight: 500 }}>{displayPhone}</Typography>
                    </Box>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </motion.div>
        </Grid>

        {/* Right Column: Information Cards */}
        <Grid size={{ xs: 12, md: 8 }}>
          <motion.div initial="hidden" animate="visible" variants={cardVariants}>
            <Card
              sx={{
                borderRadius: 5,
                boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
                p: 2,
                border: "1px solid rgba(0,0,0,0.04)"
              }}
            >
              <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 700, mb: 3, display: "flex", alignItems: "center", gap: 1 }}>
                  <AccountBoxIcon color="primary" /> Profile Information Details
                </Typography>

                {/* Patient Role Specifics */}
                {patientData && (
                  <Grid container spacing={3}>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Patient Number</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600, color: "primary.main" }}>{patientData.patientNumber}</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Blood Group</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600 }}>{patientData.bloodGroup || "N/A"}</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <DateRangeIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Date of Birth</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600 }}>{patientData.dateOfBirth?.toString() || "N/A"}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <BadgeIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Gender</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600, textTransform: "capitalize" }}>{patientData.gender?.toLowerCase() || "N/A"}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <HomeIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Permanent Address</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600 }}>{displayAddress}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                  </Grid>
                )}

                {/* Doctor Role Specifics */}
                {doctorData && (
                  <Grid container spacing={3}>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Doctor Code</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600, color: "primary.main" }}>{doctorData.doctorCode}</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Specialization</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600 }}>{doctorData.specialization}</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <MedicalServicesIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Department Name</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600 }}>{departmentName}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <BadgeIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>License Registration Number</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600 }}>{doctorData.licenseNumber}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Consultation Fee</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600 }}>₹{doctorData.consultationFee}</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Clinical Experience</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600 }}>{doctorData.yearsExperience} Years</Typography>
                      </Paper>
                    </Grid>
                  </Grid>
                )}

                {/* System/Staff Roles fallback (Admin, Pharmacist, Receptionist) */}
                {!patientData && !doctorData && (
                  <Grid container spacing={3}>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3, display: "flex", alignItems: "center", gap: 1.5 }}>
                        <SecurityIcon color="action" />
                        <Box>
                          <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Access Privilege Level</Typography>
                          <Typography variant="body1" sx={{ fontWeight: 600, color: `${roleColor}.main` }}>{user?.role}</Typography>
                        </Box>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12, sm: 6 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Account Status</Typography>
                        <Typography variant="body1" sx={{ fontWeight: 600, color: "success.main" }}>ACTIVE</Typography>
                      </Paper>
                    </Grid>
                    <Grid size={{ xs: 12 }}>
                      <Paper variant="outlined" sx={{ p: 2, borderRadius: 3 }}>
                        <Typography variant="caption" color="text.secondary" sx={{ display: "block" }}>Permissions and Description</Typography>
                        <Typography variant="body2" sx={{ mt: 1, color: "text.primary" }}>
                          This account is a system-registered hospital staff user credential with role-based access rules. You have administrative and operational clearance for medical dashboard views, scheduling slots, and related operational HMS modules.
                        </Typography>
                      </Paper>
                    </Grid>
                  </Grid>
                )}
              </CardContent>
            </Card>
          </motion.div>
        </Grid>
      </Grid>
    </Box>
  );
}
