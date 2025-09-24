import axios from "axios";

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

export const endpoints = {
  fiService: {
    all_fi_service: "/fi-service",
    my_fi_service: "/fi-service/my-fi-service",
  },
  token: {
    grant: "/grant/token",
    exchange: "/grant/exchange",
  },
  payment: {
    payment: "/payment",
    check: "/payment/check",
  },
  transactions: "/transactions",
  balance: "/balance",
};

export const publicApis = axios.create({
  baseURL: BASE_URL,
});
