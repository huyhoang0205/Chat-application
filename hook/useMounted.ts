import { useSyncExternalStore } from "react";

const emptySubscribe = () => () => {}; // Không cần lắng nghe gì cả vì trạng thái mount chỉ xảy ra 1 lần

export const useMounted = () => {
  return useSyncExternalStore(
    emptySubscribe,
    () => true, // Client: Trả về true ngay từ đầu
    () => false, // Server (SSR): Trả về false
  );
};
