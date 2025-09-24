import { useEffect } from "react";
import { publicApis, endpoints } from "../../configs/apiConfig";
import { useLocation, useNavigate } from "react-router-dom";

const HandleCallbackPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const publicToken = queryParams.get("publicToken");

    const exchangePublicToken = async (publicToken) => {
      const fiServiceId = localStorage.getItem("fiServiceId");
      try {
        const res = await publicApis.post(endpoints.token.exchange, {
          publicToken,
          fiServiceId,
        });

        if (res.data?.data?.accessToken) {
          navigate("/banking");
        }
      } catch (err) {
        console.error("Lỗi khi exchange publicToken:", err);
      }
    };

    if (publicToken) {
      exchangePublicToken(publicToken);
    }
  }, [location, navigate]);

  return <h3>Đang xử lý liên kết ngân hàng...</h3>;
};

export default HandleCallbackPage;
