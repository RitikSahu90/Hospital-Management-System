import axios from "axios";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/auth";
import { API_BASE_URL } from "./apiConfig";

const authApi = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await authApi.post<AuthResponse>("/api/auth/login", credentials);
  return response.data;
};

export const register = async (details: RegisterRequest): Promise<AuthResponse> => {
  const response = await authApi.post<AuthResponse>("/api/auth/register", details);
  return response.data;
};

export const getToken = (): string | null => localStorage.getItem("token");

export const setToken = (token: string): void => localStorage.setItem("token", token);

export const clearToken = (): void => localStorage.removeItem("token");

export const getAuthHeader = (): { Authorization: string } | {} => {
  const token = getToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};

export const isAuthenticated = (): boolean => Boolean(getToken());
