import { useEffect, useMemo, useState } from "react";
import {
  Alert,
  Autocomplete,
  Box,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  InputAdornment,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Avatar,
} from "@mui/material";
import {
  Add as AddIcon,
  Search as SearchIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  People as PeopleIcon,
  Close as CloseIcon,
  MedicalServices as StethoscopeIcon,
} from "@mui/icons-material";
import { getDepartments, createDepartment, updateDepartment, deleteDepartment } from "../services/departmentService";
import { getDoctors } from "../services/doctorService";
import { useAuth } from "../contexts/AuthContext";
import type { Department, DepartmentRequest } from "../services/departmentService";
import type { Doctor } from "../types/clinical";

const initialForm = { name: "", code: "", description: "", status: "ACTIVE" };

export default function Departments() {
  const { user } = useAuth();
  const canWrite = user?.role === "ADMIN" || user?.role === "RECEPTIONIST";
  const canDelete = user?.role === "ADMIN";

  const [departments, setDepartments] = useState<Department[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [search, setSearch] = useState("");
  
  // Pagination
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  // Dialog forms
  const [formOpen, setFormOpen] = useState(false);
  const [editingDept, setEditingDept] = useState<Department | null>(null);
  const [form, setForm] = useState(initialForm);

  // Doctors Dialog
  const [doctorsOpen, setDoctorsOpen] = useState(false);
  const [selectedDept, setSelectedDept] = useState<Department | null>(null);

  const loadData = async () => {
    try {
      setLoading(true);
      setError("");
      const [deptData, docData] = await Promise.all([
        getDepartments(),
        getDoctors(),
      ]);
      setDepartments(deptData);
      setDoctors(docData);
    } catch (err) {
      setError("Unable to load departments or doctors from the database.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadData();
  }, []);

  const handleOpenForm = (dept?: Department) => {
    setError("");
    setSuccess("");
    if (dept) {
      setEditingDept(dept);
      setForm({
        name: dept.name,
        code: dept.code,
        description: dept.description || "",
        status: dept.status || "ACTIVE",
      });
    } else {
      setEditingDept(null);
      setForm(initialForm);
    }
    setFormOpen(true);
  };

  const handleSave = async () => {
    if (!form.name.trim() || !form.code.trim()) {
      setError("Name and Code are required.");
      return;
    }
    try {
      setSaving(true);
      setError("");
      const request: DepartmentRequest = {
        name: form.name.trim(),
        code: form.code.trim().toUpperCase(),
        description: form.description.trim(),
        status: form.status as "ACTIVE" | "INACTIVE",
      };

      let savedDept: Department;
      if (editingDept) {
        savedDept = await updateDepartment(editingDept.id, request);
        setDepartments((current) =>
          current.map((d) => (d.id === savedDept.id ? savedDept : d))
        );
        setSuccess(`Department "${form.name}" updated successfully.`);
      } else {
        savedDept = await createDepartment(request);
        setDepartments((current) => [savedDept, ...current]);
        setSuccess(`Department "${form.name}" created successfully.`);
      }
      setFormOpen(false);
    } catch (err: any) {
      setError(err.response?.data?.error || "Failed to save department.");
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this department?")) return;
    try {
      setError("");
      setSuccess("");
      await deleteDepartment(id);
      setDepartments((current) => current.filter((d) => d.id !== id));
      setSuccess("Department deleted successfully.");
    } catch {
      setError("Failed to delete department. Verify you have permissions.");
    }
  };

  const handleViewDoctors = (dept: Department) => {
    setSelectedDept(dept);
    setDoctorsOpen(true);
  };

  // Filter departments by search
  const filteredDepartments = useMemo(() => {
    const keyword = search.toLowerCase();
    return departments.filter(
      (d) =>
        d.name.toLowerCase().includes(keyword) ||
        d.code.toLowerCase().includes(keyword) ||
        (d.description && d.description.toLowerCase().includes(keyword))
    );
  }, [departments, search]);

  const pagedDepartments = useMemo(() => {
    return filteredDepartments.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);
  }, [filteredDepartments, page, rowsPerPage]);

  // Get doctors list for selected department
  const selectedDeptDoctors = useMemo(() => {
    if (!selectedDept) return [];
    return doctors.filter((doc) => doc.departmentId === selectedDept.id);
  }, [selectedDept, doctors]);

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3, flexWrap: "wrap", gap: 2 }}>
        <Typography variant="h4" sx={{ fontWeight: "bold" }}>
          Departments
        </Typography>
        <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
          <TextField
            size="small"
            placeholder="Search departments..."
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              setPage(0);
            }}
            sx={{ width: 250 }}
            slotProps={{
              input: {
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon fontSize="small" color="action" />
                  </InputAdornment>
                ),
              },
            }}
          />
          {canWrite && (
            <Button variant="contained" startIcon={<AddIcon />} onClick={() => handleOpenForm()}>
              Add Department
            </Button>
          )}
        </Box>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box>
      ) : filteredDepartments.length === 0 ? (
        <Paper sx={{ p: 5, textAlign: "center" }}>
          <Typography color="text.secondary">
            {search ? "No departments match your search." : "No departments found."}
          </Typography>
        </Paper>
      ) : (
        <Paper sx={{ overflowX: "auto", borderRadius: 3, boxShadow: "0 4px 12px rgba(0,0,0,0.05)" }}>
          <TableContainer>
            <Table>
              <TableHead sx={{ bgcolor: "grey.50" }}>
                <TableRow>
                  <TableCell sx={{ fontWeight: "bold" }}>Name</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Code</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Description</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Doctors</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }}>Status</TableCell>
                  <TableCell sx={{ fontWeight: "bold" }} align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {pagedDepartments.map((dept) => {
                  const deptDocCount = doctors.filter((doc) => doc.departmentId === dept.id).length;
                  return (
                    <TableRow key={dept.id} hover>
                      <TableCell sx={{ fontWeight: "medium", color: "primary.main" }}>{dept.name}</TableCell>
                      <TableCell sx={{ fontWeight: "bold", color: "text.secondary" }}>{dept.code}</TableCell>
                      <TableCell>{dept.description || "—"}</TableCell>
                      <TableCell>
                        <Button
                          variant="outlined"
                          size="small"
                          startIcon={<PeopleIcon />}
                          onClick={() => handleViewDoctors(dept)}
                          sx={{ textTransform: "none", borderRadius: 2 }}
                        >
                          View ({deptDocCount})
                        </Button>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={dept.status}
                          size="small"
                          color={dept.status === "ACTIVE" ? "success" : "default"}
                          variant="outlined"
                          sx={{ fontWeight: "bold" }}
                        />
                      </TableCell>
                      <TableCell align="right">
                        <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 1 }}>
                          {canWrite && (
                            <IconButton size="small" color="primary" onClick={() => handleOpenForm(dept)}>
                              <EditIcon fontSize="small" />
                            </IconButton>
                          )}
                          {canDelete && (
                            <IconButton size="small" color="error" onClick={() => void handleDelete(dept.id)}>
                              <DeleteIcon fontSize="small" />
                            </IconButton>
                          )}
                        </Box>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            component="div"
            count={filteredDepartments.length}
            page={page}
            onPageChange={(_, newPage) => setPage(newPage)}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={(e) => {
              setRowsPerPage(parseInt(e.target.value, 10));
              setPage(0);
            }}
            rowsPerPageOptions={[5, 10, 25]}
          />
        </Paper>
      )}

      {/* View Doctors Dialog */}
      <Dialog open={doctorsOpen} onClose={() => setDoctorsOpen(false)} fullWidth maxWidth="xs">
        <DialogTitle sx={{ fontWeight: "bold", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          Doctors in {selectedDept?.name}
          <IconButton size="small" onClick={() => setDoctorsOpen(false)}>
            <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent dividers>
          {selectedDeptDoctors.length === 0 ? (
            <Box sx={{ py: 3, textAlign: "center" }}>
              <Typography color="text.secondary">No doctors registered in this department.</Typography>
            </Box>
          ) : (
            <List>
              {selectedDeptDoctors.map((doc) => (
                <ListItem key={doc.id} disablePadding sx={{ py: 1 }}>
                  <ListItemAvatar>
                    <Avatar sx={{ bgcolor: "primary.light" }}>
                      <StethoscopeIcon />
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText
                    primary={<Typography variant="subtitle2" sx={{ fontWeight: "bold" }}>{`Dr. ${doc.firstName} ${doc.lastName}`}</Typography>}
                    secondary={doc.specialization}
                  />
                  <Chip
                    label={doc.status}
                    size="small"
                    color={doc.status === "ACTIVE" ? "success" : doc.status === "ON_LEAVE" ? "warning" : "default"}
                    sx={{ fontWeight: "bold", fontSize: 11 }}
                  />
                </ListItem>
              ))}
            </List>
          )}
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setDoctorsOpen(false)} fullWidth variant="outlined">
            Close
          </Button>
        </DialogActions>
      </Dialog>

      {/* Add / Edit Form Dialog */}
      <Dialog open={formOpen} onClose={() => !saving && setFormOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle sx={{ fontWeight: "bold" }}>
          {editingDept ? "Edit Department" : "Add Department"}
        </DialogTitle>
        <DialogContent dividers sx={{ display: "grid", gap: 2, pt: 2 }}>
          <TextField
            required
            label="Department Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            fullWidth
            size="small"
          />
          <TextField
            required
            label="Department Code"
            value={form.code}
            onChange={(e) => setForm({ ...form, code: e.target.value })}
            placeholder="e.g. CARD"
            fullWidth
            size="small"
          />
          <TextField
            label="Description"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            multiline
            rows={3}
            fullWidth
            size="small"
          />
          <Autocomplete
            options={["ACTIVE", "INACTIVE"]}
            value={form.status}
            onChange={(_, newValue) => setForm({ ...form, status: newValue || "ACTIVE" })}
            renderInput={(params) => <TextField {...params} label="Status" size="small" />}
          />
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button onClick={() => setFormOpen(false)} disabled={saving}>
            Cancel
          </Button>
          <Button variant="contained" onClick={() => void handleSave()} disabled={saving}>
            {saving ? "Saving..." : "Save"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}