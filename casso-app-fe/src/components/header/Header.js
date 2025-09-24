import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Link } from "react-router-dom";

const Header = () => {
  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container>
        <Navbar.Brand as={Link} to="/dashboard">
          Casso-App
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/banking">
              Liên kết ngân hàng
            </Nav.Link>
            <Nav.Link as={Link} to="/transaction">
              Lịch sử giao dịch
            </Nav.Link>
            <Nav.Link as={Link} to="/qrcode">
              Tạo mã thanh toán
            </Nav.Link>
            <Nav.Link as={Link} to="/balance">
              Truy vấn số dư
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
