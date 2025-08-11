const supportedLocales = [
    {
        label: 'English',
        value: 'en',
        flag: '/images/flags/us_flag.jpg',
    },
    {
        label: 'Tiếng Việt',
        value: 'vi',
        flag: '/images/flags/vietnam_flag.png',
    },
];

const localCurrency = localStorage.getItem('restaurantCurrency');
const currentLanguage = localStorage.getItem('restaurentLocale');
const taxPercent = 10.5;

const BOT_TEMPLATES = [
    { id: 'empty-bot', name: 'Empty Bot (Bot Trống)' },
    { id: 'welcome-bot', name: 'Welcome Bot (Bot Chào mừng)' },
    // Thêm các template khác mà bạn muốn hỗ trợ ở đây
    // Ví dụ: { id: 'faq-bot', name: 'FAQ Bot (Bot Hỏi đáp)' },
    // { id: 'ecommerce-bot', name: 'E-commerce Bot (Bot Thương mại điện tử)' },
];

export {
    supportedLocales,
    localCurrency,
    currentLanguage,
    taxPercent,
    BOT_TEMPLATES,
};
