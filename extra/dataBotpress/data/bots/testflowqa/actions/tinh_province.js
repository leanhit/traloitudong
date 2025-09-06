  // Lấy nội dung tin nhắn
  const text = event.payload.text
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')

  // Danh sách tỉnh/thành: dạng không dấu
  const provinces = {
    'ha noi': ['ha noi', 'hanoi', 'hn'],
    'ho chi minh': ['ho chi minh', 'tp hcm', 'hcm', 'tphcm', 'sai gon', 'saigon'],
    'hai phong': ['hai phong', 'haiphong', 'hp'],
    'da nang': ['da nang', 'danang', 'dn'],
    'bac ninh': ['bac ninh', 'bacninh'],
    'hai duong': ['hai duong', 'haidong', 'hd'],
    'hung yen': ['hung yen', 'hungyen', 'hy']
    // ... thêm tất cả 63 tỉnh
  }

  // Tạo regex từ tất cả biến thể
  const allVariants = Object.values(provinces)
    .flat()
    .join('|')
  const regex = new RegExp(`\\b(${allVariants})\\b`, 'g')

  // Tìm khớp
  const match = text.match(regex)

  if (match) {
    // Tìm tỉnh gốc từ biến thể
    let foundProvince = null
    for (const [province, variants] of Object.entries(provinces)) {
      if (variants.includes(match[0])) {
        foundProvince = province
        break
      }
    }

    session.slots['location'] = { value: foundProvince }
    bp.logger.info(`Đã bắt được tỉnh/thành: ${foundProvince}`)
  } else {
    bp.logger.info(`Không tìm thấy tỉnh/thành trong tin nhắn: ${text}`)
  }