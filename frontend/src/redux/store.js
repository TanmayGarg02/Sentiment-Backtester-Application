import { configureStore } from "@reduxjs/toolkit";
import sentimentReducer from "./sentimentSlice";
import backtestReducer from "./backtestSlice";

export const store = configureStore({
  reducer: {
    sentiment: sentimentReducer,
    backtest: backtestReducer,
  },
});
