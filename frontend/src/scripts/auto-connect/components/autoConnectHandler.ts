import { ElLoading, ElMessageBox, ElMessage } from 'element-plus';
import { h } from 'vue';
import {
    CircleCheck,
    CircleClose,
    RefreshRight,
    WarningFilled
} from '@element-plus/icons-vue';
import { fbConnectionApi } from '@/api/fbConnectionApi';

// Function to display the detailed result dialog
// Hàm hiển thị hộp thoại kết quả chi tiết
const showResultDialog = (result) => {
    const renderMessage = () => {
        const listStyle = { textAlign: 'left', padding: '10px 0', listStyleType: 'none' };
        const items = [];

        items.push(h('h4', result.message));

        if (result.connectedPages?.length > 0) {
            items.push(h('li', {}, [
                // ✅ Thêm width và height vào style của icon
                h(CircleCheck, { style: { color: '#67C23A', marginRight: '5px', width: '16px', height: '16px' } }),
                h('strong', ' Kết nối thành công:'),
                h('span', ` ${result.connectedPages.join(', ')}`)
            ]));
        }

        if (result.reactivatedPages?.length > 0) {
            items.push(h('li', {}, [
                // ✅ Thêm width và height vào style của icon
                h(RefreshRight, { style: { color: '#E6A23C', marginRight: '5px', width: '16px', height: '16px' } }),
                h('strong', ' Kích hoạt lại:'),
                h('span', ` ${result.reactivatedPages.join(', ')}`)
            ]));
        }

        if (result.inactivePages?.length > 0) {
            items.push(h('li', {}, [
                // ✅ Thêm width và height vào style của icon
                h(CircleClose, { style: { color: '#909399', marginRight: '5px', width: '16px', height: '16px' } }),
                h('strong', ' Đã gỡ kết nối:'),
                h('span', ` ${result.inactivePages.join(', ')}`)
            ]));
        }

        if (result.errors?.length > 0) {
            items.push(h('li', { style: { marginTop: '10px' } }, [
                // ✅ Thêm width và height vào style của icon
                h(WarningFilled, { style: { color: '#F56C6C', marginRight: '5px', width: '16px', height: '16px' } }),
                h('strong', ' Lỗi xảy ra:')
            ]));
            // ... (phần code xử lý errors giữ nguyên)
        }

        return h('div', {}, items);
    };

    ElMessageBox.confirm(renderMessage(), result.success ? 'Hoàn thành!' : 'Cảnh báo!', {
        confirmButtonText: 'OK',
        showCancelButton: false,
        showClose: false,
        type: result.success ? 'success' : 'warning',
        center: true,
    });
};

// Main processing function
export const sendAddConnections = async (userAccessToken, selectedBotId, refreshDataFn) => {
    const loadingInstance = ElLoading.service({
        text: 'Đang xử lý kết nối, xin vui lòng chờ...',
        background: 'rgba(0, 0, 0, 0.7)',
    });

    try {
        const payload = {
            userAccessToken,
            botId: selectedBotId,
        };
        const result = await fbConnectionApi.addConnections(payload);
        console.log('Auto-connect result:', result);
        
        // ✅ Add a check for a valid response before showing the dialog
        if (result && typeof result === 'object') {
            showResultDialog(result.data);
        } else {
            ElMessage.error('Dữ liệu trả về từ server không hợp lệ.');
        }

    } catch (err) {
        console.error('Error during auto-connect:', err);
        let errorMessage = 'Đã xảy ra lỗi hệ thống, vui lòng thử lại sau!';
        if (err.response && err.response.data && err.response.data.message) {
            errorMessage = err.response.data.message;
        }
        ElMessage.error(errorMessage);
    } finally {
        loadingInstance.close();
        if (refreshDataFn) {
            await refreshDataFn();
        }
    }
};