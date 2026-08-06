import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import type { UserRole } from "../../types/auth";

interface ProtectedRouteProps {
  allowedRoles?: UserRole[];
}

export default function ProtectedRoute({ allowedRoles }: ProtectedRouteProps) {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    return <Navigate to={user.role === "PHARMACIST" ? "/prescriptions" : "/dashboard"} replace />;
  }

  return <Outlet />;
}
