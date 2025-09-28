  /** @title Check Time */
  const checkTime = async (bp, event) => {
    const now = new Date()
    const hour = now.getHours()

    // Giờ từ 19h đến 6h sáng hôm sau là buổi tối
    if (hour >= 19 || hour < 6) {
      event.state.temp.isEvening = true
    } else {
      event.state.temp.isEvening = false
    }
  }

  return checkTime