  function action(bp, event, args) {
    // Lấy nội dung tin nhắn khách gửi
    const text = event.payload.text.toLowerCase()

    // Danh sách ngành nghề + từ đồng nghĩa
    const industriesMap = {
      'thời trang': ['thời trang', 'quần áo', 'fashion'],
      'bất động sản': ['bất động sản', 'bds', 'đất', 'nhà đất'],
      'mỹ phẩm': ['mỹ phẩm', 'cosmetic'],
      'cà phê': ['cà phê', 'coffee'],
      spa: ['spa', 'chăm sóc sắc đẹp'],
      'tour du lịch': ['tour du lịch', 'du lịch', 'tour travel'],
      'giày dép': ['giày dép', 'shoes'],
      'túi xách': ['túi xách', 'bag'],
      'điện thoại': ['điện thoại', 'phone'],
      homestay: ['homestay', 'nhà nghỉ', 'căn hộ du lịch', 'villa', 'bnb']
    }

    let found = null
    for (const [key, synonyms] of Object.entries(industriesMap)) {
      if (synonyms.some(s => text.includes(s))) {
        found = key
        break
      }
    }

    if (found) {
      session.slots['product_service_slot'] = { value: found }
      bp.logger.info(`Đã bắt được ngành nghề: ${found}`)

      // Trả lời theo ngành nghề
      event.reply(
        '#builtin_text',
        {},
        {
          text: `Dạ, bên em sẽ hướng dẫn chạy quảng cáo ${found} theo đúng tệp khách hàng, giúp ra đơn nhanh và hiệu quả hơn ạ.`
        }
      )
    } else {
      bp.logger.info(`Không tìm thấy ngành nghề trong tin nhắn: ${text}`)
      session.slots['product_service_slot'] = { value: 'sản phẩm/dịch vụ của anh/chị' }
    }
  }