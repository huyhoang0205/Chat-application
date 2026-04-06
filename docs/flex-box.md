## Các thuộc tính cho Flex Container (Cha)
|Thuộc tính|Tác dụng|Giá trị phổ biến|
|----|-----|----|
|display|Kích hoạt Flexbox.|"flex, inline-flex"|
|flex-direction|Hướng của các item (định nghĩa trục chính).|"row (ngang), column (dọc), row-reverse..."|
|flex-wrap|Có cho phép xuống dòng khi hết chỗ không.|"nowrap (ép trên 1 hàng), wrap (xuống hàng)."|
|justify-content|Căn lề các item theo trục chính (ngang).|"flex-start, center, space-between, space-around."|
|align-items|Căn lề các item theo trục dọc.|"stretch (kéo giãn), center, flex-start, baseline."|
|align-content|Khoảng cách giữa các dòng (khi có nhiều dòng).|"space-between, center, stretch."|
|gap|Khoảng cách giữa các item.|"Ví dụ: 10px, 1rem."

## Các thuộc tính cho Flex Items (Con)
Dùng để điều chỉnh riêng lẻ từng "đứa con" trong hàng.

- **flex-grow**: Khả năng "giãn" ra để lấp đầy chỗ trống (mặc định là 0).

- **flex-shrink**: Khả năng "co" lại khi thiếu chỗ (mặc định là 1).

- **flex-basis**: Kích thước ban đầu của item trước khi giãn/co.

- **flex**: Viết tắt của cả 3 cái trên (Ví dụ: flex: 1 0 auto;).

- **align-self**: Đè lên thuộc tính align-items của cha để tự căn lề cho riêng mình.

- **order**: Thay đổi thứ tự hiển thị mà không cần đổi code HTML.
## Một số lưu ý nhỏ để "Flex" chuyên nghiệp hơn
- Trục chính thay đổi: Khi bạn dùng **flex-direction: column**, các thuộc tính căn lề sẽ bị đảo vai trò. **justify-content** lúc này lại đi căn theo chiều dọc.

- **Margin auto**: Trong Flexbox, **margin**: auto cực kỳ quyền năng. Nếu bạn đặt **margin-left**: auto cho một item, nó sẽ đẩy mình về tận cùng bên phải của container.

- Trình duyệt: Hầu hết các trình duyệt hiện đại (Chrome, Edge, Safari, Firefox) đều hỗ trợ **Flexbox** cực tốt. Bạn không cần lo lắng về tính tương thích trừ khi phải hỗ trợ... Internet Explorer 9 trở xuống.