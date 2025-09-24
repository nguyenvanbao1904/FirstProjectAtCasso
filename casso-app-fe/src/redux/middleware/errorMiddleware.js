import { isRejectedWithValue } from "@reduxjs/toolkit";
import { toast } from "react-toastify";

export const errorHandlingMiddleware = (store) => (next) => (action) => {
  if (isRejectedWithValue(action)) {
    const errorMessage =
      action.payload?.message ||
      action.payload?.error ||
      action.error?.message ||
      "Có lỗi xảy ra!";

    toast.error(errorMessage, {
      position: "top-right",
      autoClose: 3000,
    });
  }

  return next(action);
};
