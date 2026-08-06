import apiClient from "./apiClient";

export interface NotificationResponse {
  id: number;
  title: string;
  message: string;
  createdAt: string;
  isRead: boolean;
}

export const getPatientNotifications = () =>
  apiClient.get<NotificationResponse[]>("/api/notifications/patient").then((r) => r.data);
