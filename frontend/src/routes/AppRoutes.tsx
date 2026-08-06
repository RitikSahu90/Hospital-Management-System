import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Patients from "../pages/Patients";
import Doctors from "../pages/Doctors";
import Appointments from "../pages/Appointments";
import Billing from "../pages/Billing";
import Pharmacy from "../pages/Pharmacy";
import Prescriptions from "../pages/Prescriptions";
import Laboratory from "../pages/Laboratory";
import Departments from "../pages/Departments";
import MedicalRecords from "../pages/MedicalRecords";
import Suppliers from "../pages/Suppliers";
import Payments from "../pages/Payments";

import Profile from "../pages/Profile";

import MainLayout from "../layout/MainLayout";
import ProtectedRoute from "../components/common/ProtectedRoute";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />

        <Route element={<ProtectedRoute />}>
          <Route element={<MainLayout />}>
            <Route element={<ProtectedRoute allowedRoles={["ADMIN", "DOCTOR", "RECEPTIONIST", "PATIENT"]} />}>
              <Route path="/dashboard" element={<Dashboard />} />
            </Route>
            <Route path="/profile" element={<Profile />} />
            <Route path="/patients" element={<Patients />} />
            <Route path="/doctors" element={<Doctors />} />
            <Route path="/departments" element={<Departments />} />
            <Route path="/medical-records" element={<MedicalRecords />} />
            <Route path="/suppliers" element={<Suppliers />} />
            <Route path="/payments" element={<Payments />} />
            <Route path="/appointments" element={<Appointments />} />
            <Route path="/billing" element={<Billing />} />
            <Route path="/pharmacy" element={<Pharmacy />} />
            <Route path="/prescriptions" element={<Prescriptions />} />
            <Route path="/laboratory" element={<Laboratory />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
