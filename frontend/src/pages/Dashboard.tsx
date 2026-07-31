import { Grid } from "@mui/material";

import DashboardHeader from "../components/dashboard/DashboardHeader";
import StatCard from "../components/dashboard/StatCard";
import PatientChart from "../components/dashboard/PatientChart";
import RevenueChart from "../components/dashboard/RevenueChart";
import RecentPatients from "../components/dashboard/RecentPatients";
import AppointmentTable from "../components/dashboard/AppointmentTable";
import NotificationPanel from "../components/dashboard/NotificationPanel";

import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import LocalHospitalIcon from "@mui/icons-material/LocalHospital";
import EventAvailableIcon from "@mui/icons-material/EventAvailable";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";

export default function Dashboard() {
  return (
    <>
      <DashboardHeader />

      {/* KPI Cards */}
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Total Patients"
            value="3,248"
            icon={<PeopleAltIcon />}
            color="#1976D2"
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Doctors"
            value="125"
            icon={<LocalHospitalIcon />}
            color="#2E7D32"
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Appointments"
            value="287"
            icon={<EventAvailableIcon />}
            color="#F57C00"
          />
        </Grid>

        <Grid size={{ xs: 12, sm: 6, lg: 3 }}>
          <StatCard
            title="Revenue"
            value="₹12.4 L"
            icon={<CurrencyRupeeIcon />}
            color="#8E24AA"
          />
        </Grid>
      </Grid>

      {/* Charts */}
      <Grid container spacing={3} sx={{ mt: 3 }}>
        <Grid size={{ xs: 12, lg: 7 }}>
          <PatientChart />
        </Grid>

        <Grid size={{ xs: 12, lg: 5 }}>
          <RevenueChart />
        </Grid>
      </Grid>

      {/* Recent Patients & Today's Appointments */}
      <Grid container spacing={3} sx={{ mt: 3 }}>
        <Grid size={{ xs: 12, lg: 6 }}>
          <RecentPatients />
        </Grid>

        <Grid size={{ xs: 12, lg: 6 }}>
          <AppointmentTable />
        </Grid>
      </Grid>

      {/* Notifications */}
      <Grid container spacing={3} sx={{ mt: 3, mb: 3 }}>
        <Grid size={{ xs: 12 }}>
          <NotificationPanel />
        </Grid>
      </Grid>
    </>
  );
}