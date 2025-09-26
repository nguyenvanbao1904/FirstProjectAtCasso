import axios from "axios";

const BASE_URL = process.env.REACT_APP_API_BASE_URL;
const redirectUri = "http://localhost:3000/success";

export const endpoints = {
  fiService: {
    all_fi_service: "/fi-service",
    my_fi_service: "/fi-service/my-fi-service",
  },
  token: {
    grant: "/grant/token",
    exchange: "/grant/exchange",
    update_grant: "/grant/token/update",
  },
  payment: {
    payment: "/payment",
    check: "/payment/check",
  },
  transactions: "/transactions",
  balance: "/balance",
  subscribe_server_event: `${BASE_URL}/subscribe`,
};

export const publicApis = axios.create({
  baseURL: BASE_URL,
});

async function startUpdateMode(fiServiceId) {
  try {
    const res = await publicApis.post(endpoints.token.update_grant, {
      scopes: "transaction,qrpay,balance",
      redirectUri,
      fiServiceId,
      language: "vi",
    });
    const grantToken = res.data?.data?.grantToken;
    if (grantToken) {
      const oauthUrl = `https://dev.link.bankhub.dev?grantToken=${grantToken}&redirectUri=${encodeURIComponent(
        redirectUri
      )}&iframe=false`;
      window.location.href = oauthUrl;
    }
  } catch (err) {
    alert("Không thể khởi tạo liên kết ngân hàng, vui lòng thử lại!");
  }
}

publicApis.interceptors.response.use(
  (response) => response,
  (error) => {
    const err = error.response?.data;
    const fiServiceId = error.config?.meta?.fiServiceId;

    if (err?.error === "GRANT_LOGIN_REQUIRED") {
      console.warn("Grant login required, chuyển sang Update Mode...");
      startUpdateMode(fiServiceId);
    } else {
      console.error("API error:", {
        status: error.response?.status,
        url: error.config?.url,
        data: err,
      });
    }

    return Promise.reject(error);
  }
);
