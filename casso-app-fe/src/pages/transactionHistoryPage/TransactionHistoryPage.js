import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  Container,
  Row,
  Col,
  Card,
  Table,
  Button,
  Form,
  Spinner,
} from "react-bootstrap";
import { toast } from "react-toastify";

import { fetchLinkedBanks } from "../../redux/features/linkedBanks/linkedBanksThunks";
import { selectBanks } from "../../redux/features/linkedBanks/linkedBanksSelector";
import { endpoints, publicApis } from "../../configs/apiConfig";

const TransactionHistoryPage = () => {
  const dispatch = useDispatch();
  const { banks, loading } = useSelector(selectBanks);

  const [selectedBank, setSelectedBank] = useState({});
  const [transactions, setTransactions] = useState([]);
  const [loadingTx, setLoadingTx] = useState(false);

  useEffect(() => {
    dispatch(fetchLinkedBanks());
  }, [dispatch]);

  const handleLoadTransactions = async () => {
    if (!selectedBank?.id) return;
    try {
      setLoadingTx(true);
      const res = await publicApis.post(endpoints.transactions, {
        fiServiceId: selectedBank.id,
      });
      setTransactions(res.data?.data || []);
    } catch (err) {
      console.error("Error load transactions:", err);
      setTransactions([]);
      toast.error("Tải giao dịch thất bại, vui lòng thử lại sau!");
    } finally {
      setLoadingTx(false);
    }
  };

  return (
    <Container className="mt-4">
      <Row className="mb-3">
        <Col>
          <h2>Lịch sử giao dịch</h2>
        </Col>
        <Col>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Ngân hàng</Form.Label>
              {loading ? (
                <Spinner animation="border" />
              ) : (
                <Form.Select
                  value={selectedBank?.id || ""}
                  onChange={(e) => {
                    const bank = banks.find(
                      (b) => String(b.id) === e.target.value
                    );
                    setSelectedBank(bank || {});
                  }}
                >
                  <option key="default" value="">
                    -- Chọn ngân hàng --
                  </option>
                  {banks.map((bank) => (
                    <option key={bank.id || bank.title} value={bank.id}>
                      {bank.title}
                    </option>
                  ))}
                </Form.Select>
              )}
            </Form.Group>
          </Form>
        </Col>
        <Col className="d-flex align-items-end">
          <Button
            variant="primary"
            onClick={handleLoadTransactions}
            disabled={!selectedBank?.id || loadingTx}
          >
            {loadingTx ? (
              <Spinner size="sm" animation="border" />
            ) : (
              "Tải giao dịch"
            )}
          </Button>
        </Col>
      </Row>

      <Card className="shadow-sm">
        <Card.Body>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>Mã tham chiếu</th>
                <th>Số tiền</th>
                <th>Nội dung</th>
                <th>Tài khoản</th>
              </tr>
            </thead>
            <tbody>
              {transactions.length > 0 ? (
                transactions.map((tx, idx) => (
                  <tr key={idx}>
                    <td>
                      {tx.transactionDateTime
                        ? new Date(tx.transactionDateTime).toLocaleString(
                            "vi-VN"
                          )
                        : "-"}
                    </td>
                    <td>{tx.reference}</td>
                    <td
                      style={{
                        color: tx.amount > 0 ? "green" : "red",
                        fontWeight: "bold",
                      }}
                    >
                      {tx.amount.toLocaleString("vi-VN")} đ
                    </td>
                    <td>{tx.description}</td>
                    <td>{tx.accountNumber}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={5} className="text-center">
                    {loadingTx ? "Đang tải..." : "Chưa có dữ liệu"}
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default TransactionHistoryPage;
