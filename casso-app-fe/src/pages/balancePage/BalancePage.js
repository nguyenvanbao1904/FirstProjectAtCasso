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
  Alert,
} from "react-bootstrap";
import { toast } from "react-toastify";

import { fetchLinkedBanks } from "../../redux/features/linkedBanks/linkedBanksThunks";
import { selectBanks } from "../../redux/features/linkedBanks/linkedBanksSelector";
import { endpoints, publicApis } from "../../configs/apiConfig";

const BalancePage = () => {
  const dispatch = useDispatch();
  const { banks, loading } = useSelector(selectBanks);

  const [selectedBank, setSelectedBank] = useState({});
  const [accounts, setAccounts] = useState([]);
  const [loadingTx, setLoadingTx] = useState(false);

  useEffect(() => {
    dispatch(fetchLinkedBanks());
  }, [dispatch]);

  const handleLoadBalance = async () => {
    if (!selectedBank?.id) return;
    try {
      setLoadingTx(true);
      const res = await publicApis.get(
        `${endpoints.balance}/${selectedBank.id}`
      );
      const accs = res.data?.data?.accounts || [];
      setAccounts(accs);
    } catch (err) {
      console.error("Error load balance:", err);
      setAccounts([]);
      toast.error("Tải số dư thất bại, vui lòng thử lại sau!");
    } finally {
      setLoadingTx(false);
    }
  };

  return (
    <Container className="mt-4">
      <Row className="mb-3">
        <Col>
          <h2>Truy vấn số dư</h2>
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
            onClick={handleLoadBalance}
            disabled={!selectedBank?.id || loadingTx}
          >
            {loadingTx ? <Spinner size="sm" animation="border" /> : "Tải số dư"}
          </Button>
        </Col>
      </Row>

      <Row>
        <Col>
          {accounts.length > 0 ? (
            <Card>
              <Card.Header>Kết quả số dư</Card.Header>
              <Card.Body>
                <Table striped bordered hover responsive>
                  <thead>
                    <tr>
                      <th>Số tài khoản</th>
                      <th>Tên tài khoản</th>
                      <th>Số dư</th>
                      <th>Loại tiền</th>
                    </tr>
                  </thead>
                  <tbody>
                    {accounts.map((acc, idx) => (
                      <tr key={idx}>
                        <td>{acc.accountNumber}</td>
                        <td>{acc.accountName}</td>
                        <td>{acc.balance.toLocaleString()}</td>
                        <td>{acc.currency}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          ) : (
            <Alert variant="secondary">Chưa có dữ liệu số dư</Alert>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default BalancePage;
