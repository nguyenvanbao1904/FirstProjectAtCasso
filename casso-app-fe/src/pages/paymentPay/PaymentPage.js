import { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLinkedBanks } from "../../redux/features/linkedBanks/linkedBanksThunks";
import { selectBanks } from "../../redux/features/linkedBanks/linkedBanksSelector";
import { endpoints, publicApis } from "../../configs/apiConfig";
import { v4 as uuidv4 } from "uuid";

import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";
import Alert from "react-bootstrap/Alert";
import { QRCodeCanvas } from "qrcode.react";
import { toast } from "react-toastify";

const PaymentPage = () => {
  const dispatch = useDispatch();
  const { banks, loading, error } = useSelector(selectBanks);

  const [selectedBank, setSelectedBank] = useState(null);
  const [amount, setAmount] = useState("");
  const [qrCode, setQrCode] = useState(null);
  const [loadingQR, setLoadingQR] = useState(false);
  const [referenceNumber, setReferenceNumber] = useState(null);

  const intervalRef = useRef(null);

  // Load danh sách ngân hàng
  useEffect(() => {
    dispatch(fetchLinkedBanks());
  }, [dispatch]);

  // Theo dõi trạng thái thanh toán
  useEffect(() => {
    if (!referenceNumber) return;

    const checkPayment = async () => {
      try {
        const rs = await publicApis.get(
          `${endpoints.payment.check}/${referenceNumber}`
        );
        const status = rs?.data?.data;

        if (status === "SUCCESS") {
          toast.success("Thanh toán thành công 🎉", { autoClose: 3000 });
          clearInterval(intervalRef.current);
          setReferenceNumber(null);

          setTimeout(() => {
            setQrCode(null);
            setAmount("");
            setSelectedBank(null);
          }, 3000);
        }
      } catch (err) {
        console.error("Error checking payment status", err);
      }
    };

    intervalRef.current = setInterval(checkPayment, 3000);

    return () => clearInterval(intervalRef.current);
  }, [referenceNumber]);

  // Tạo mã QR
  const handleGenerateQR = async () => {
    if (qrCode) {
      toast.warn("Đang chờ thanh toán QR hiện tại!");
      return;
    }
    if (!selectedBank || !amount) {
      toast.warn("Vui lòng chọn ngân hàng và nhập số tiền");
      return;
    }

    setLoadingQR(true);
    try {
      const ref = uuidv4().replace(/-/g, "").slice(0, 9);
      setReferenceNumber(ref);

      const rs = await publicApis.post(endpoints.payment.payment, {
        fiServiceId: selectedBank.id,
        amount,
        description: ref,
        referenceNumber: ref,
      });

      const code = rs?.data?.data?.qrPay?.qrCode;
      if (code) {
        setQrCode(code);
      } else {
        toast.error("Không nhận được QR từ server!");
      }
    } catch (err) {
      console.error("Error generating QR:", err);
      let msg = "Không thể tạo mã QR, vui lòng thử lại!";
      if (err?.response?.data) {
        console.error("Backend error:", err.response.data);
        msg =
          err.response.data?.message || err.response.data?.errorMessage || msg;
      }
      toast.error(msg);
    } finally {
      setLoadingQR(false);
    }
  };

  return (
    <Container className="mt-4">
      <Row>
        <Col md={6}>
          <Card className="p-3 shadow-sm">
            <h4 className="mb-3">Tạo mã thanh toán</h4>
            {error && <Alert variant="danger">{error}</Alert>}

            <Form>
              <Form.Group className="mb-3">
                <Form.Label>Kênh thanh toán</Form.Label>
                {loading ? (
                  <Spinner animation="border" />
                ) : (
                  <Form.Select
                    value={selectedBank?.id || ""}
                    onChange={(e) =>
                      setSelectedBank(
                        banks.find((b) => b.id === e.target.value) || null
                      )
                    }
                  >
                    <option value="">-- Chọn ngân hàng --</option>
                    {banks.map((bank) => (
                      <option key={bank.id} value={bank.id}>
                        {bank.title}
                      </option>
                    ))}
                  </Form.Select>
                )}
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Số tiền</Form.Label>
                <Form.Control
                  type="number"
                  value={amount}
                  placeholder="Nhập số tiền"
                  onChange={(e) => setAmount(e.target.value)}
                />
              </Form.Group>

              <Button
                variant="primary"
                onClick={handleGenerateQR}
                disabled={loadingQR}
              >
                {loadingQR ? (
                  <Spinner animation="border" size="sm" />
                ) : (
                  "Tạo mã QR"
                )}
              </Button>
            </Form>
          </Card>
        </Col>

        <Col md={6}>
          <Card className="p-3 shadow-sm text-center">
            <h4 className="mb-3">Mã QR của bạn</h4>
            {qrCode ? (
              <QRCodeCanvas value={qrCode} size={256} includeMargin level="H" />
            ) : (
              <p>Chưa có mã QR nào được tạo</p>
            )}
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default PaymentPage;
