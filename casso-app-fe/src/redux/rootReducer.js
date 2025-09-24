import { combineReducers } from "@reduxjs/toolkit";
import linkedBanksSlice from "./features/linkedBanks/linkedBanksSlice";

const rootReducer = combineReducers({
  linkedBanks: linkedBanksSlice,
});

export default rootReducer;
