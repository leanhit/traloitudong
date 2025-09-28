const getIncomingEvent = bp.events.getIncomingEvent;

if (event.customData) {
  const payloadType = event.customData.type;
  const url = event.customData.url;

  bp.logger.info(`Nhận event tùy chỉnh từ Facebook: type=${payloadType}`);

  if (payloadType === 'image' || payloadType === 'gif') {
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi ảnh/GIF: ${url}`});
  } else if (payloadType === 'video') {
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi video: ${url}`});
  } else if (payloadType === 'audio') {
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi audio: ${url}`});
  } else if (payloadType === 'file') {
    bp.events.replyToEvent(event, { type: 'text', text: `Bạn vừa gửi file: ${url}`});
  } else {
    bp.events.replyToEvent(event, { type: 'text', text: `Tệp đính kèm không xác định: ${payloadType}`});
  }
}
