import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

export const fetchSentiment = createAsyncThunk(
  "sentiment/fetch",
  async (ticker, thunkAPI) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/sentiment/${ticker}`);
      return response.data;
    } catch (err) {
      return thunkAPI.rejectWithValue(err.response?.data || "Error fetching sentiment");
    }
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
        state.error = action.payload;
      });
  },
});

export default sentimentSlice.reducer;
