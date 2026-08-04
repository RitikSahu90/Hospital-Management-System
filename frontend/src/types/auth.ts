export type UserRole = "ADMIN" | "DOCTOR" | "RECEPTIONIST" | "PHARMACIST" | "PATIENT";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  role?: UserRole;
}

export interface AuthUser {
  username: string;
  role: UserRole;
}
