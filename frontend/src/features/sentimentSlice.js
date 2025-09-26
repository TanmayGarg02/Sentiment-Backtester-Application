import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

// ✅ Thunk to fetch sentiment data
export const fetchSentiment = createAsyncThunk(
  "sentiment/fetchSentiment",
  async (ticker) => {
    const response = await axios.get(`http://localhost:8080/sentiment/${ticker}`);
    return response.data; // backend should send structured JSON
  }
);

const sentimentSlice = createSlice({
  name: "sentiment",
  initialState: {
    data: null,
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchSentiment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchSentiment.fulfilled, (state, action) => {
        state.loading = false;
        state.data = action.payload;
      })
      .addCase(fetchSentiment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });
  },
});

export default sentimentSlice.reducer;
