import { configureStore } from "@reduxjs/toolkit";
import sentimentReducer from "./features/sentimentSlice";

export const store = configureStore({
  reducer: {
    sentiment: sentimentReducer,
  },
});
