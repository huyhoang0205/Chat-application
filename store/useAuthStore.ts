import { create } from "zustand";

type AuthState = {
  isInitialized: boolean;
  
  setIsInitialized: (val: boolean) => void;
};

export const useAuthStore = create<AuthState>((set) => ({
  isInitialized: false,

  setIsInitialized: (val) => set({ isInitialized: val }),
}));