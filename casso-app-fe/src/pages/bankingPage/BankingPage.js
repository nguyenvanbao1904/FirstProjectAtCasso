import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Spinner from "react-bootstrap/Spinner";
import Modal from "react-bootstrap/Modal";
import Alert from "react-bootstrap/Alert";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLinkedBanks } from "../../redux/features/linkedBanks/linkedBanksThunks";
import { selectBanks } from "../../redux/features/linkedBanks/linkedBanksSelector";
import { publicApis, endpoints } from "../../configs/apiConfig";
import { toast } from "react-toastify";

const BankingPage = () => {
  const dispatch = useDispatch();

  const { banks, loading, error } = useSelector(selectBanks);

  // State cục bộ cho allBanks
  const [isShowBank, setIsShowBank] = useState(false);
  const [allBanks, setAllBanks] = useState([]);
  const [isLoadingAllBanks, setIsLoadingAllBanks] = useState(false);
  const [errorAllBanks, setErrorAllBanks] = useState(null);

  const redirectUri = "http://localhost:3000/success";

  useEffect(() => {
    dispatch(fetchLinkedBanks());
  }, [dispatch]);

  const handleChooseBank = async (fiServiceId) => {
    try {
      setIsLoadingAllBanks(true);
      const res = await publicApis.post(endpoints.token.grant, {
        scopes: "transaction,qrpay,balance",
        redirectUri,
        fiServiceId,
        language: "vi",
      });
      const grantToken = res.data?.data?.grantToken;
      if (grantToken) {
        localStorage.setItem("fiServiceId", fiServiceId);
        const oauthUrl = `https://dev.link.bankhub.dev?grantToken=${grantToken}&redirectUri=${encodeURIComponent(
          redirectUri
        )}&iframe=false`;
        window.location.href = oauthUrl;
      }
    } catch (err) {
      alert("Không thể khởi tạo liên kết ngân hàng, vui lòng thử lại!");
    } finally {
      setIsLoadingAllBanks(false);
    }
  };

  const handleShowBank = async () => {
    setIsShowBank(true);
    try {
      setIsLoadingAllBanks(true);
      setErrorAllBanks(null);
      const res = await publicApis.get(endpoints.fiService.all_fi_service);
      const mapped = res.data.data.fiServices.map((item) => ({
        title: item.name,
        description: item.fiFullName,
        btnText: "Chọn ngân hàng",
        imgSrc: item.logo,
        handleClick: () => handleChooseBank(item.id),
      }));
      setAllBanks(mapped);
    } catch (err) {
      setErrorAllBanks("Có lỗi xảy ra khi tải danh sách ngân hàng");
    } finally {
      setIsLoadingAllBanks(false);
    }
  };

  const handleRemoveBank = async (bankId) => {
    if (!window.confirm("Bạn có chắc muốn xoá ngân hàng này không?")) return;

    try {
      await publicApis.delete(`${endpoints.token.grant}/${bankId}`);
      dispatch(fetchLinkedBanks()); // reload danh sách
      toast.success("Xoá ngân hàng thành công!");
    } catch (err) {
      console.error(err);
      toast.error("Không thể xoá ngân hàng, vui lòng thử lại!");
    }
  };

  return (
    <>
      <Container className="mt-4">
        <Row className="align-items-center mb-3">
          <Col>
            <h2>Ngân hàng đã liên kết</h2>
          </Col>
          <Col className="text-end">
            <Button variant="primary" onClick={handleShowBank}>
              Thêm ngân hàng mới
            </Button>
          </Col>
        </Row>

        {error && (
          <Row className="mb-3">
            <Col>
              <Alert variant="danger" dismissible>
                {error}
              </Alert>
            </Col>
          </Row>
        )}

        {loading ? (
          <div className="text-center my-4">
            <Spinner animation="border" />
          </div>
        ) : (
          <Row>
            {banks.length === 0 ? (
              <p>Chưa có ngân hàng nào được liên kết</p>
            ) : (
              banks.map((bank, idx) => (
                <Col key={idx} md={4} className="mb-4">
                  <Card className="h-100 shadow-sm">
                    <Card.Img
                      variant="top"
                      src={bank.imgSrc}
                      style={{ height: "120px", objectFit: "contain" }}
                    />
                    <Card.Body>
                      <Card.Title>{bank.title}</Card.Title>
                      <Card.Text>{bank.description}</Card.Text>
                      <Button
                        variant="outline-danger"
                        onClick={() => handleRemoveBank(bank.id)}
                      >
                        {bank.btnText}
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              ))
            )}
          </Row>
        )}
      </Container>

      <Modal
        size="lg"
        show={isShowBank}
        onHide={() => setIsShowBank(false)}
        aria-labelledby="example-modal-sizes-title-lg"
      >
        <Modal.Header closeButton>
          <Modal.Title id="example-modal-sizes-title-lg">
            Lựa chọn ngân hàng bạn muốn liên kết
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {errorAllBanks && (
            <Alert variant="danger" dismissible className="mb-3">
              {errorAllBanks}
            </Alert>
          )}

          {isLoadingAllBanks ? (
            <div className="text-center my-4">
              <Spinner animation="border" />
            </div>
          ) : (
            <Row>
              {allBanks.length === 0 ? (
                <h5>Không có ngân hàng nào sẵn sàng</h5>
              ) : (
                allBanks.map((bank, idx) => (
                  <Col key={idx} md={4} className="mb-4">
                    <Card className="h-100 shadow-sm">
                      <Card.Img
                        variant="top"
                        src={bank.imgSrc}
                        style={{ height: "100px", objectFit: "contain" }}
                      />
                      <Card.Body>
                        <Card.Title>{bank.title}</Card.Title>
                        <Card.Text>{bank.description}</Card.Text>
                        <Button onClick={bank.handleClick} variant="primary">
                          {bank.btnText}
                        </Button>
                      </Card.Body>
                    </Card>
                  </Col>
                ))
              )}
            </Row>
          )}
        </Modal.Body>
      </Modal>
    </>
  );
};

export default BankingPage;
