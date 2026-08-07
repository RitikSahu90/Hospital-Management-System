import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
  Box,
  Typography,
  Alert
} from '@mui/material';
import apiClient from '../../services/apiClient';

interface StaffRegistrationModalProps {
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

export default function StaffRegistrationModal({ open, onClose, onSuccess }: StaffRegistrationModalProps) {
  const [role, setRole] = useState('DOCTOR');
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    doctorCode: '',
    licenseNumber: '',
    specialization: '',
    phone: '',
    consultationFee: '',
    yearsExperience: '',
    departmentId: '1' // Defaulting to first department
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    setLoading(true);
    setError('');
    try {
      const payload: any = {
        role,
        username: formData.username,
        email: formData.email,
        firstName: formData.firstName,
        lastName: formData.lastName,
      };

      if (role === 'DOCTOR') {
        payload.departmentId = parseInt(formData.departmentId);
        payload.doctorCode = formData.doctorCode;
        payload.licenseNumber = formData.licenseNumber;
        payload.specialization = formData.specialization;
        payload.phone = formData.phone;
        payload.consultationFee = parseFloat(formData.consultationFee || '0');
        payload.yearsExperience = parseInt(formData.yearsExperience || '0');
      }

      await apiClient.post('/api/admin/staff/register', payload);
      onSuccess();
      onClose();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to register staff. Please check fields.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Register New Staff</DialogTitle>
      <DialogContent>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        
        <Box sx={{ display: 'grid', gap: 2, pt: 1 }}>
          <TextField
            select
            label="Role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            fullWidth
          >
            <MenuItem value="DOCTOR">Doctor</MenuItem>
            <MenuItem value="RECEPTIONIST">Receptionist</MenuItem>
            <MenuItem value="PHARMACIST">Pharmacist</MenuItem>
          </TextField>

          <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
            <TextField label="Username" name="username" value={formData.username} onChange={handleChange} fullWidth />
            <TextField label="Email" name="email" type="email" value={formData.email} onChange={handleChange} fullWidth />
          </Box>
          <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
            <TextField label="First Name" name="firstName" value={formData.firstName} onChange={handleChange} fullWidth />
            <TextField label="Last Name" name="lastName" value={formData.lastName} onChange={handleChange} fullWidth />
          </Box>

          {role === 'DOCTOR' && (
            <>
              <Typography variant="subtitle2" sx={{ mt: 2, mb: 1, fontWeight: 'bold' }}>Doctor Specific Details</Typography>
              <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
                <TextField label="Doctor Code" name="doctorCode" value={formData.doctorCode} onChange={handleChange} fullWidth />
                <TextField label="License Number" name="licenseNumber" value={formData.licenseNumber} onChange={handleChange} fullWidth />
              </Box>
              <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
                <TextField label="Specialization" name="specialization" value={formData.specialization} onChange={handleChange} fullWidth />
                <TextField label="Phone" name="phone" value={formData.phone} onChange={handleChange} fullWidth />
              </Box>
              <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
                <TextField label="Consultation Fee" name="consultationFee" type="number" value={formData.consultationFee} onChange={handleChange} fullWidth />
                <TextField label="Years Experience" name="yearsExperience" type="number" value={formData.yearsExperience} onChange={handleChange} fullWidth />
              </Box>
            </>
          )}

          <Typography variant="caption" color="textSecondary" sx={{ mt: 2 }}>
            Default password for the new staff account will be <b>pass123</b>.
          </Typography>
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="inherit">Cancel</Button>
        <Button onClick={handleSubmit} variant="contained" color="primary" disabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
