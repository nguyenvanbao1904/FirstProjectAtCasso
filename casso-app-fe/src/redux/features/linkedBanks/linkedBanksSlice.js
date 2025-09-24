import { createSlice } from "@reduxjs/toolkit";
import { fetchLinkedBanks } from "./linkedBanksThunks";

const linkedBanksSlice = createSlice({
  name: "linkedBanks",
  initialState: { banks: [], loading: false, error: null },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchLinkedBanks.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchLinkedBanks.fulfilled, (state, action) => {
        state.loading = false;
        state.banks = action.payload;
      })
      .addCase(fetchLinkedBanks.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default linkedBanksSlice.reducer;
