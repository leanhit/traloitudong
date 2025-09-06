  // Lấy nội dung tin nhắn người dùng gửi đến
  const text = event.payload.text

  // Regex tìm số điện thoại bắt đầu bằng 0 hoặc +84, theo sau là 8-9 chữ số
  const regex = /(0|\+84)[0-9]{8,9}/g

  // Tìm số trong tin nhắn
  const match = text.match(regex)

  if (match) {
    const phone = match[0] // lấy số đầu tiên tìm được
    session.slots['dt-zalo'] = { value: phone } // Gán số vào slot phone_number
    bp.logger.info(`Đã bắt được số điện thoại: ${phone}`)
  } else {
    bp.logger.info(`Không tìm thấy số điện thoại trong tin nhắn: ${text}`)
  }