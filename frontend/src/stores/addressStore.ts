// stores/addressStore.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export const useAddressStore = defineStore('address', () => {
    const provinces = ref<any[]>([]);
    const districts = ref<any[]>([]);
    const wards = ref<any[]>([]);

    const loading = ref(false);

    // Fetch toàn bộ danh sách tỉnh
    const fetchProvinces = async () => {
        if (provinces.value.length || loading.value) return; // Nếu đã có dữ liệu hoặc đang tải thì không gọi lại
        loading.value = true;

        try {
            const res = await axios.get('https://provinces.open-api.vn/api/p/')
            provinces.value = res.data;
        } catch (err) {
            ElMessage.error('Không thể tải tỉnh/thành phố')
        } finally {
            loading.value = false
        }
    }

    const fetchDistricts = async (provinceCode: string) => {
        if (districts.value.length || loading.value) return // Nếu đã có dữ liệu hoặc đang tải thì không gọi lại
        loading.value = true
        try {
            const res = await axios.get(
                `https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`
            )
            districts.value = res.data.districts || []
        } catch (err) {
            ElMessage.error('Không thể tải quận/huyện')
        } finally {
            loading.value = false
        }
    }

    const fetchWards = async (districtCode: string) => {
        if (wards.value.length || loading.value) return // Nếu đã có dữ liệu hoặc đang tải thì không gọi lại
        loading.value = true
        try {
            const res = await axios.get(
                `https://provinces.open-api.vn/api/d/${districtCode}?depth=2`
            )
            wards.value = res.data.wards || []
        } catch (err) {
            ElMessage.error('Không thể tải phường/xã')
        } finally {
            loading.value = false
        }
    }

    const getFullAddress = async (provinceCode: string, districtCode: string, wardCode: string, detail: string) => {
        try {
            const [provinceRes, districtRes, wardRes] = await Promise.all([
                axios.get('https://provinces.open-api.vn/api/p'),
                axios.get(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`),
                axios.get(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`),
            ]);

            provinces.value = provinceRes.data;
            districts.value = districtRes.data.districts;
            wards.value = wardRes.data.wards;

            const provinceName = provinces.value.find(p => p.code == provinceCode)?.name || '';
            const districtName = districts.value.find(d => d.code == districtCode)?.name || '';
            const wardName = wards.value.find(w => w.code == wardCode)?.name || '';

            return [detail, wardName, districtName, provinceName].filter(Boolean).join(', ');
        } catch (e) {
            console.error('Lỗi khi tải địa chỉ', e);
            return '';
        }
    };

    const clearAll = () => {
        districts.value = []
        wards.value = []
    }

    return {
        provinces,
        districts,
        wards,
        fetchProvinces,
        fetchDistricts,
        fetchWards,
        getFullAddress,
        clearAll,
    }
})