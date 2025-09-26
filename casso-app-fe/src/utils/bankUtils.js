export const filterBanksByQrpay = (banks = [], isQrpay = false) => {
  if (!Array.isArray(banks)) return [];
  return banks.filter((b) =>
    isQrpay ? b.code?.includes("_qrpay") : !b.code?.includes("_qrpay")
  );
};
