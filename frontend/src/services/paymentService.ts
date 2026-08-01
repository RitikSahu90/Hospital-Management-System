import apiClient from "./apiClient";

export type PaymentMethod = "CASH" | "CARD" | "UPI" | "INSURANCE";
export interface Payment { id: number; billId: number; amount: number; paymentMethod: PaymentMethod; paidAt: string; }
export interface PaymentRequest { amount: number; paymentMethod: PaymentMethod; }
export const getPayments = (billId: number) => apiClient.get<Payment[]>(`/api/payments/bill/${billId}`).then((response) => response.data);
export const createPayment = (billId: number, body: PaymentRequest) => apiClient.post<Payment>(`/api/payments/bill/${billId}`, body).then((response) => response.data);