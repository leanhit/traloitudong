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
  { id: 'welcome-bot', name: 'Welcome Bot', desc: "Basic bot that showcases some of the bot's functionality" },
  { id: 'small-talk', name: 'Small Talk', desc: 'Includes basic smalltalk examples' },
  { id: 'empty-bot', name: 'Empty Bot', desc: 'Start fresh with a clean flow' },
  { id: 'learn-botpress', name: 'Learn Botpress Basics', desc: 'Learn Botpress basic features' }
]
;

export {
    supportedLocales,
    localCurrency,
    currentLanguage,
    taxPercent,
    BOT_TEMPLATES,
};
