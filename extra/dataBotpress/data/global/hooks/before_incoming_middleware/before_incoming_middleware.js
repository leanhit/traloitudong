/**
 * Hook này sẽ chạy trước khi Botpress xử lý một tin nhắn đến.
 * Nhiệm vụ của nó là kiểm tra xem tin nhắn có phải là một tệp đính kèm không.
 * Thông tin tệp đính kèm được server gửi dưới dạng một chuỗi JSON trong trường `event.text`.
 */
const getIncomingEvent = bp.events.getIncomingEvent;

let payload;
try {
  // BƯỚC 1: Thử phân tích cú pháp nội dung tin nhắn như một JSON object.
  // Nếu thành công, tin nhắn là một tệp đính kèm. Nếu không, nó là một tin nhắn văn bản bình thường.
  payload = JSON.parse(event.text);
} catch (e) {
  // Nếu việc phân tích cú pháp thất bại, đây không phải là một tệp đính kèm được xử lý bởi webhook.
  // Chúng ta bỏ qua hook này và để Botpress xử lý tin nhắn như bình thường.
  return;
}

// BƯỚC 2: Nếu việc phân tích cú pháp thành công, kiểm tra xem payload có chứa các trường cần thiết không.
if (payload && payload.type && payload.url) {
  const payloadType = payload.type;
  const url = payload.url;

  // Ghi log để theo dõi trong console của Botpress
  bp.logger.info(`Nhận event tùy chỉnh từ Facebook: type=${payloadType}`);

  // BƯỚC 3: Tạo một tin nhắn phản hồi tùy thuộc vào loại tệp đính kèm.
  if (payloadType === 'image' || payloadType === 'gif') {
    // Gửi tin nhắn phản hồi về ảnh/GIF.
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi ảnh/GIF: ${url}` });
  } else if (payloadType === 'video') {
    // Gửi tin nhắn phản hồi về video.
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi video: ${url}` });
  } else if (payloadType === 'audio') {
    // Gửi tin nhắn phản hồi về audio.
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi audio: ${url}` });
  } else if (payloadType === 'file') {
    // Gửi tin nhắn phản hồi về file.
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi file: ${url}` });
  } else {
    // Xử lý các loại tệp đính kèm không xác định.
    bp.events.replyToEvent(event, { type: 'text', text: `Tệp đính kèm không xác định: ${payloadType}` });
  }
}
