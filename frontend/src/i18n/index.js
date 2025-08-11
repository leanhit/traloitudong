import { createI18n } from "vue-i18n"
// import pluralRules from "./rules/pluralization"
// import numberFormats from "./rules/numbers.js"
// import datetimeFormats from "./rules/datetime.js"
import en from "./locales/en.json" // <--- add this
import vi from "./locales/vi.json" // <--- add this
import de from "./locales/de.json" // <--- add this
import po from "./locales/po.json" // <--- add this


let currentLocalte = localStorage.getItem('restaurentLocale');
if(currentLocalte === (undefined || null)){
  currentLocalte="en";
}

export default createI18n({
    locale: currentLocalte,//import.meta.env.VITE_DEFAULT_LOCALE,
    // fallbackLocale: import.meta.env.VITE_FALLBACK_LOCALE,
    legacy: false,
    globalInjection: true,
    messages: {
      en, // <--- add this
      vi,
      de,
      po // <--- add this
    },
    // pluralRules,
    // numberFormats,
    // datetimeFormats
  })