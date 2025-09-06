#!/bin/bash
# Script tự động tạo swap 4GB

SWAP_SIZE="4G"
SWAP_FILE="/swapfile"

echo "👉 Tắt swap cũ..."
sudo swapoff -a

echo "👉 Xoá swapfile cũ (nếu có)..."
sudo rm -f $SWAP_FILE

echo "👉 Tạo swapfile mới dung lượng $SWAP_SIZE..."
sudo fallocate -l $SWAP_SIZE $SWAP_FILE || sudo dd if=/dev/zero of=$SWAP_FILE bs=1M count=4096

echo "👉 Phân quyền an toàn..."
sudo chmod 600 $SWAP_FILE

echo "👉 Định dạng swap..."
sudo mkswap $SWAP_FILE

echo "👉 Kích hoạt swap..."
sudo swapon $SWAP_FILE

echo "👉 Kiểm tra lại dung lượng swap:"
free -h

echo "👉 Đảm bảo swap tự bật sau reboot..."
if ! grep -q "$SWAP_FILE" /etc/fstab; then
    echo "$SWAP_FILE none swap sw 0 0" | sudo tee -a /etc/fstab
fi

echo "✅ Swap $SWAP_SIZE đã được tạo và kích hoạt thành công!"
