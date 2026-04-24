import axios, { AxiosInstance } from 'axios';

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:4040/api/v1';
const AUTH_URL = import.meta.env.VITE_AUTH_URL ?? 'http://localhost:4040/api';

export const api: AxiosInstance = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' },
});

export const authApi: AxiosInstance = axios.create({
  baseURL: AUTH_URL,
  headers: { 'Content-Type': 'application/json' },
});

// Dev B: ajouter ici l'interceptor JWT (request: Authorization Bearer, response: refresh sur 401)
