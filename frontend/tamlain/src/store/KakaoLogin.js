import { createSlice } from "@reduxjs/toolkit";

const initialLoginState = {
  code: "",
};

const loginSlice = createSlice({
  name: "login",
  initialState: initialLoginState,
  reducers: {
    kakaoLogin(state, action) {
      state.code = action.payload;
    },
  },
});

export const loginActions = loginSlice.actions;

export default loginSlice.reducer;
