import { useI18n } from "vue-i18n";
import { ref, computed, watch, onMounted } from "vue";

export default {
  props: ["viewSettings"],
  setup() {
    const { t } = useI18n();
    const BOTPRESS_SERVER_URL = "https://bot.traloitudong.com";

    const botId = ref("");
    const activeSection = ref("");
    const copyStatusVisible = ref(false);
    const testIframeRef = ref<HTMLIFrameElement | null>(null);
    const defaultBotIframeRef = ref<HTMLIFrameElement | null>(null);

    const generatedCode = computed(() => {
      if (!botId.value) return "";
      return `
        <script src="${BOTPRESS_SERVER_URL}/assets/modules/channel-web/inject.js"><\/script>
        <script>
          window.botpressWebChat.init({
            botId: "${botId.value}",
            host: "${BOTPRESS_SERVER_URL}",
            chatId: "bp-web-widget",
            botName: "Trợ lý ảo",
            botConvoDescription: "Hỏi tôi bất kỳ điều gì",
            backgroundColor: "#ffffff",
            textColorOnBackground: "#000000"
          });
        <\/script>`;
    });

    const getBotpressFullHtmlContent = (id: string) => {
      return `
        <!DOCTYPE html>
        <html lang="vi">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Bot Test</title>
            <style>body { margin: 0; padding: 0; overflow: hidden; }</style>
        </head>
        <body>
            <p style="text-align: center; margin-top: 20px;">Đang tải bot...</p>
            ${getBotpressEmbedCodeScripts(id)}
        </body>
        </html>`;
    };

    const getBotpressEmbedCodeScripts = (id: string) => {
      return `
        <script src="${BOTPRESS_SERVER_URL}/assets/modules/channel-web/inject.js"><\/script>
        <script>
          window.botpressWebChat.init({
            botId: "${id}",
            host: "${BOTPRESS_SERVER_URL}",
            chatId: "bp-web-widget",
            botName: "Trợ lý ảo",
            botConvoDescription: "Hỏi tôi bất kỳ điều gì",
            backgroundColor: "#ffffff",
            textColorOnBackground: "#000000"
          });
        <\/script>`;
    };

    const showSection = (type: "code" | "test") => {
      if (!botId.value) {
        alert("Vui lòng nhập Bot ID trước!");
        return;
      }
      activeSection.value = type;
      copyStatusVisible.value = false;
    };

    watch(activeSection, (newSection) => {
      if (newSection === "test" && testIframeRef.value) {
        const iframeContent = getBotpressFullHtmlContent(botId.value);
        const iframeDoc =
          testIframeRef.value.contentDocument ||
          testIframeRef.value.contentWindow?.document;
        if (iframeDoc) {
          iframeDoc.open();
          iframeDoc.write(iframeContent);
          iframeDoc.close();
        }
      }
    });

    const copyEmbedCode = () => {
      if (!generatedCode.value) return;

      navigator.clipboard
        .writeText(generatedCode.value)
        .then(() => {
          copyStatusVisible.value = true;
          setTimeout(() => {
            copyStatusVisible.value = false;
          }, 2000);
        })
        .catch((err) => {
          console.error("Failed to copy text: ", err);
          alert("Không thể sao chép. Vui lòng thử lại hoặc sao chép thủ công.");
        });
    };

    const defaultTestIframe = ref<HTMLIFrameElement | null>(null);

    onMounted(() => {
        if (defaultTestIframe.value) {
        const iframeDoc =
            defaultTestIframe.value.contentDocument ||
            defaultTestIframe.value.contentWindow?.document;

        if (iframeDoc) {
            iframeDoc.open();
            iframeDoc.write(getBotpressFullHtmlContent("traloitudongai"));
            iframeDoc.close();
        }
        }
    });

    return {
      t,
      botId,
      activeSection,
      copyStatusVisible,
      testIframeRef,
      defaultBotIframeRef,
      generatedCode,
      showSection,
      copyEmbedCode,
      getBotpressFullHtmlContent,
      getBotpressEmbedCodeScripts,
      defaultTestIframe
    };
  },
};
