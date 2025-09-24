import { createAsyncThunk } from "@reduxjs/toolkit";
import { publicApis, endpoints } from "../../../configs/apiConfig";

export const fetchLinkedBanks = createAsyncThunk(
  "linkedBanks/fetch",
  async (_, { rejectWithValue }) => {
    try {
      const res = await publicApis.get(endpoints.fiService.my_fi_service);
      return res.data.data.fiServices.map((item) => ({
        id: item.id,
        title: item.name,
        description: item.fiFullName,
        btnText: "Xóa",
        imgSrc: item.logo,
      }));
    } catch (err) {
      return rejectWithValue(err.response?.data || err.message);
    }
  }
);
