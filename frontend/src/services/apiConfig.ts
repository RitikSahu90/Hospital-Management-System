const configuredUrl = import.meta.env.VITE_API_URL;

// Development calls Spring Boot directly; the production Nginx container proxies /api.
export const API_BASE_URL = configuredUrl ?? (import.meta.env.DEV ? "http://localhost:8080" : "");
