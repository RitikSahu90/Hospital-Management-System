import { createContext, useContext, useEffect, useMemo, useState } from "react";
import type { ReactNode } from "react";
import { clearToken, getToken, setToken } from "../services/authService";
import type { AuthUser, UserRole } from "../types/auth";

interface AuthContextType {
  user: AuthUser | null;
  login: (token: string, username: string, role: UserRole) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isAuthenticatedState, setIsAuthenticatedState] = useState(false);

  useEffect(() => {
    const token = getToken();
    if (token) {
      const username = localStorage.getItem("username") || "user";
      const role = (localStorage.getItem("role") as UserRole | null) || "PATIENT";
      setUser({ username, role });
      setIsAuthenticatedState(true);
    }
  }, []);

  const login = (token: string, username: string, role: UserRole) => {
    setToken(token);
    localStorage.setItem("username", username);
    localStorage.setItem("role", role);
    setUser({ username, role });
    setIsAuthenticatedState(true);
  };

  const logout = () => {
    clearToken();
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    setUser(null);
    setIsAuthenticatedState(false);
  };

  const value = useMemo(
    () => ({
      user,
      login,
      logout,
      isAuthenticated: isAuthenticatedState,
    }),
    [user, isAuthenticatedState]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
