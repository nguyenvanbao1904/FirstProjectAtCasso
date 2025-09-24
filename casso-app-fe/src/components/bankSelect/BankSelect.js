import { Form, Spinner } from "react-bootstrap";

const BankSelect = ({ banks, loading, selectedBank, setSelectedBank }) => {
  return (
    <Form.Group className="mb-3">
      <Form.Label>Ngân hàng</Form.Label>
      {loading ? (
        <Spinner animation="border" />
      ) : (
        <Form.Select
          value={selectedBank?.id || ""}
          onChange={(e) => {
            const bank = banks.find((b) => String(b.id) === e.target.value);
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
  );
};

export default BankSelect;
